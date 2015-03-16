package models
import play.api.libs.json._
import scalikejdbc._
class AddrObjRsp(val regioncode: String, val postalcode: Option[String], val shortname: String, val offname: String, val aolevel : Int, val aoguid: String, val parent: Option[ParentAo] ) {

}

object AddrObjRsp{
  implicit val addrObjRspWrites = new Writes[AddrObjRsp] {
  def writes(addrObjRsp: AddrObjRsp) = Json.obj(
    "regioncode" -> addrObjRsp.regioncode,
    "postalcode" -> addrObjRsp.postalcode,
    "shortname" -> addrObjRsp.shortname,
    "offname" -> addrObjRsp.offname,
    "aolevel" -> addrObjRsp.aolevel,
    "aoguid" -> addrObjRsp.aoguid,
    "parent" -> addrObjRsp.parent
  )
}
  
  def fromRs(rs: WrappedResultSet) = 
    new AddrObjRsp(rs.string("regioncode"), rs.stringOpt("postalcode"), rs.string("shortname"), rs.string("offname"), rs.int("aolevel"), rs.string("aoguid"),None)
   def fromRsWithParent(rs: WrappedResultSet) = 
    new AddrObjRsp(rs.string("regioncode"), rs.stringOpt("postalcode"), rs.string("shortname"), rs.string("offname"), rs.int("aolevel"), rs.string("aoguid"),Some(new ParentAo(rs.string("pshortname"), rs.string("poffname"),rs.string("paoguid"))))
  
}