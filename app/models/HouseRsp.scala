package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import scalikejdbc._

case class HouseRsp(
    aoguid: String, 
    houseguid: Option[String], 
    intguid: Option[String], 
    postalcode: Option[String], 
    housenum: Option[String], 
    eststatus: Option[String], 
    buildnum: Option[String], 
    strucnum: Option[String], 
    strstatus: Option[String]) {
}



object HouseRsp{
  val estMap: Map[Int,Option[String]] = Map(0 ->None,1->Some("Владение"),2->Some("Дом"), 3 -> Some("Домовладение"))
  val stMap: Map[Int,Option[String]] = Map(0 ->None,1->Some("строение"),2->Some("сооружение"), 3 -> Some("литер"))
  
  def fromRs(rs: WrappedResultSet): HouseRsp = {
    val aoguid = rs.string("aoguid") 
    val houseguid = rs.stringOpt("houseguid")
    val postalcode = rs.stringOpt("postalcode")
    val housenum =  rs.stringOpt("housenum")
    val eststatus =  estMap(rs.int("eststatus")) 
    val buildnum =  rs.stringOpt("buildnum")
    val strucnum =  rs.stringOpt("strucnum")
    val strstatus =   stMap(rs.int("strstatus")) 
    
    new HouseRsp(aoguid,houseguid,None,postalcode,housenum,eststatus,buildnum,strucnum,strstatus)
     
  }
  
  def fromInterval(rs: WrappedResultSet): List[HouseRsp] = {
    val aoguid = rs.string("aoguid") 
    val intguid = rs.stringOpt("intguid")
    val postalcode = rs.stringOpt("postalcode")  
    val eststatus =  estMap(2) 
    val intstart = rs.int("intstart")
    val intend = rs.int("intstart")
    val intstatus = rs.int("intstatus")
    val step = if(intstatus==0) 1 else 2
    
     (for(i <- intstart.to(intend, step)) yield {
        new HouseRsp(aoguid,None,intguid,postalcode,Some(i.toString()),eststatus,None,None,None)
     }) toList
  }
  
  implicit val houseRspWrites = new Writes[HouseRsp] {
  def writes(houseRsp: HouseRsp) = Json.obj(
    "aoguid" -> houseRsp.aoguid, 
    "houseguid" -> houseRsp.houseguid, 
    "intguid" -> houseRsp.intguid, 
    "postalcode" -> houseRsp.postalcode, 
    "housenum" -> houseRsp.housenum, 
    "eststatus" -> houseRsp.eststatus, 
    "buildnum" -> houseRsp.buildnum, 
    "strucnum" -> houseRsp.strucnum, 
    "strstatus" -> houseRsp.strstatus
  )
}
}