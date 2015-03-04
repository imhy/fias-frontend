package controllers
import play.api.libs.json._

class BaseReqController {

  def check() = {
    val json: JsValue = null
    
    val nameResult: JsResult[String] = (json \ "name").validate[String]
  
    // Pattern matching
    nameResult match {
      case s: JsSuccess[String] => println("Name: " + s.get)
      case e: JsError => println("Errors: " + JsError.toFlatJson(e).toString()) 
    }
  }
}