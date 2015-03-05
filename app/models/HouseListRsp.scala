package models

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class HouseListRsp(values:  Option[List[HouseRsp]]) {

}

object HouseListRsp{
  implicit val houseListRspWrites = new Writes[HouseListRsp] {
    def writes(houseListRsp: HouseListRsp) = Json.obj(
      "values" -> houseListRsp.values
    )
  }
}