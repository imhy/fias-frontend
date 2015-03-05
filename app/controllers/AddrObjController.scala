package controllers

import play.api._
import play.api.mvc._
import models._
import scala.concurrent._
import play.api.libs.json._


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
          case Some(v) =>
           
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
  
 
  
  def checkUser(apiKey: String) : Option[String] = {
    if (apiKey.equalsIgnoreCase("apikey")) Some("username") else None
  }
 
  //
  

  def listAo(aor : AddrObjReq): List[AddrObjRsp] = {
    aor.level match {
      case None => DbConnect.listChild(aor.parent, aor.name)
      case Some("region") => DbConnect.listRegion(aor.name)
      case Some("locality") => DbConnect.listChild(aor.parent, aor.name)
      case Some("street") => DbConnect.listChild(aor.parent, aor.name)
      case Some(u) => throw new IllegalArgumentException("Unknown address level: " + u) 
    }
  }
  
  def listAoByLevel(aor : AddrObjReq): List[AddrObjRsp] = {
    aor.level.get match {
      case "region" => DbConnect.listRegion(aor.name)
      case "locality" => DbConnect.listChild(aor.parent, aor.name)
      case "street" => DbConnect.listChild(aor.parent, aor.name)
      case u => throw new IllegalArgumentException("Unknown address level: " + u) 
    }
  }
  
 
  
   
  
  
  def authAction[A](action: Action[JsValue]) = 
    Action.async(BodyParsers.parse.json){ request =>
    val reqResult = request.body.validate[AddrObjReq]
      action(request)
  }
  
  def a2 = authAction[JsValue](list)
} 