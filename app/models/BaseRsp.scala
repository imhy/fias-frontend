package models
import play.api.libs.json._

case class BaseRsp[T](status: Int,msg: Option[String],result: Option[T]) {

}

object BaseRsp {
  implicit def baseRspWrites[T](implicit fmt: Writes[T]): Writes[BaseRsp[T]] = new Writes[BaseRsp[T]] {
    def writes(baseRsp: BaseRsp[T]) = Json.obj(
      "status" -> baseRsp.status,
      "msg" -> baseRsp.msg,
      "result" -> baseRsp.result
    )
  }
}