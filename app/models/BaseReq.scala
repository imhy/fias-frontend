package models
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class BaseReq(apiKey: Option[String]) {
  
}

/*object BaseReq {
  implicit val baseReqReads: Reads[BaseReq] = (
    (JsPath \ "apiKey").readNullable[String]
)(BaseReq.apply _)
}*/