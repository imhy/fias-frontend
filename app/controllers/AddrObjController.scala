package controllers

import play.api._
import play.api.mvc._
import models._
import scala.concurrent._
import play.api.libs.json._


object AddrObjController  extends Controller{
def read = Action {
    val addrObjRsp: AddrObjRsp = AddrObjRsp("54", Some("630055"), "ул", "Российская", 7, "sdfs-sdfsdf-ererer")
    val baseRsp: BaseRsp[AddrObjRsp] = BaseRsp(200,Some("hello1"),Some(addrObjRsp))
    Ok(Json.toJson(baseRsp))
  }

  def list = Action(BodyParsers.parse.json) { request =>
    DbConnect.listUsers()
    val aoReq = request.body.validate[AddrObjReq]
    aoReq.fold(
      errors => {
       val baseRsp: BaseRsp[AddrObjRsp] = BaseRsp(500,Some(JsError.toFlatJson(errors).toString()),None)
       Ok(Json.toJson(baseRsp))
      },
      addrObjReq => {  
        checkUser(addrObjReq.apiKey) match {
          case None => val baseRsp: BaseRsp[AddrObjRsp] = BaseRsp(400,None,None)
                       Ok(Json.toJson(baseRsp))
          case Some(v) => 
           val listAddrObj: List[AddrObjRsp] = listAo(addrObjReq)
           val addrObjListRsp: AddrObjListRsp = AddrObjListRsp(Some(listAddrObj))
           val baseRsp: BaseRsp[AddrObjListRsp] = BaseRsp(200,None,Some(addrObjListRsp))
           Ok(Json.toJson(baseRsp))
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
      case Some(l) => listAoByLevel(aor) 
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