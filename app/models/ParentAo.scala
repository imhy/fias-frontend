package models
import play.api.libs.json._

class ParentAo( val shortname: String, val offname: String, val aoguid: String) {

}

object ParentAo{
  implicit val parentAoWrites = new Writes[ParentAo] {
    def writes(parentAo: ParentAo) = Json.obj(
      "shortname" -> parentAo.shortname,
      "offname" -> parentAo.offname,
      "aoguid" -> parentAo.aoguid
    )
  }
}