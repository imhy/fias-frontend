package models
import play.api.libs.json._

class ParentAo(val aoguid: String, val shortname: String, val offname: String) {

}

object ParentAo{
  implicit val parentAoWrites = new Writes[ParentAo] {
    def writes(parentAo: ParentAo) = Json.obj(
      "shortname" -> parentAo.shortname,
      "offname" -> parentAo.offname,
      "aoguid" -> parentAo.aoguid
    )
  }
  //def apply(aoguid: String, shortname: String, offname: String) = new ParentAo(aoguid, shortname, offname)
}