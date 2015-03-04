package models
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class AddrObjReq(apiKey: String, level: Option[String], parent: Option[String], name: Option[String], region: Option[String], limit: Option[Int]){

}

object AddrObjReq {
  implicit val addrObjReqReads: Reads[AddrObjReq] = (
    (JsPath \ "apiKey").read[String] and
    (JsPath \ "level").readNullable[String] and
    (JsPath \ "parent").readNullable[String] and
    (JsPath \ "name").readNullable[String] and
    (JsPath \ "region").readNullable[String] and
    (JsPath \ "limit").readNullable[Int]
)(AddrObjReq.apply _)
}