package models
import play.api.libs.json._
import scalikejdbc._
case class AddrObjRsp(regioncode: String, postalcode: Option[String], shortname: String, offname: String, aolevel : Int, aoguid: String ) {

}

object AddrObjRsp{
  implicit val addrObjRspWrites = new Writes[AddrObjRsp] {
  def writes(addrObjRsp: AddrObjRsp) = Json.obj(
    "regioncode" -> addrObjRsp.regioncode,
    "postalcode" -> addrObjRsp.postalcode,
    "shortname" -> addrObjRsp.shortname,
    "offname" -> addrObjRsp.offname,
    "aolevel" -> addrObjRsp.aolevel,
    "aoguid" -> addrObjRsp.aoguid
  )
}
  
  def fromRs(rs: WrappedResultSet) = 
    new AddrObjRsp(rs.string("regioncode"), rs.stringOpt("postalcode"), rs.string("shortname"), rs.string("offname"), rs.int("aolevel"), rs.string("aoguid"))
  
}