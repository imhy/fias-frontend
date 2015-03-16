package controllers

import play.api._
import play.api.mvc._
import models._
import scala.concurrent._
import play.api.libs.json._
import play.api.cache.Cache
import play.api.Play.current


object AddrObjController  extends Controller{

  def list = Action(BodyParsers.parse.json) { request =>
    val aoReq = request.body.validate[AddrObjReq]
    aoReq.fold(
      errors => {
       val baseRsp: BaseRsp[String] = BaseRsp(500,Some(JsError.toFlatJson(errors).toString()),None)
       Ok(Json.toJson(baseRsp))
      },
      addrObjReq => {  
        checkUser(addrObjReq.apiKey) match {
          case None => val baseRsp: BaseRsp[String] = BaseRsp(400,None,None)
                       Ok(Json.toJson(baseRsp))
          case Some(u) =>
           
           addrObjReq.level match {
             case Some("house") => {
               val listHouses = DbConnect.listHouse(addrObjReq.parent, addrObjReq.name)
               val baseRsp = BaseRsp(200,None,Some(HouseListRsp(Some(listHouses))))
               Ok(Json.toJson(baseRsp))
             }
             case _ => {
               val listAddrObj = listAo(addrObjReq)
               val baseRsp = BaseRsp(200,None,Some(AddrObjListRsp(Some(listAddrObj))))
               Ok(Json.toJson(baseRsp))    
             }
           } 
        } 
      }
    )
  }
  
 
  
  def checkUser(apiKey: String) : Option[User] = {
   Cache.getAs[User](apiKey) match {
      case None => DbConnect.findUser(apiKey) match {
        case None => None
        case Some(du) => 
          Cache.set(apiKey, du, 40*60)
          Some(du)
      }
      case u => u 
    }
  }
 
  //
  

  def listAo(aor : AddrObjReq): List[AddrObjRsp] = {
    aor.level match {
      case None => DbConnect.listChild(aor.parent, aor.name)
      case Some("region") => DbConnect.listRegion(aor.name)
      case Some("locality") => DbConnect.listLocality(aor.region, aor.name)
      case Some("street") => DbConnect.listChild(aor.parent, aor.name)
      case Some(u) => throw new IllegalArgumentException("Unknown address level: " + u) 
    }
  }
  
  
  def authAction[A](action: Action[JsValue]) = 
    Action.async(BodyParsers.parse.json){ request =>
    val reqResult = request.body.validate[AddrObjReq]
      action(request)
  }
  
  def a2 = authAction[JsValue](list)
} 