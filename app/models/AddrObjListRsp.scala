package models
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class AddrObjListRsp(values: Option[List[AddrObjRsp]]) {

}

object AddrObjListRsp {
  implicit val addrObjListRspWrites = new Writes[AddrObjListRsp] {
    def writes(addrObjListRsp: AddrObjListRsp) = Json.obj(
      "values" -> addrObjListRsp.values
    )
  }
}