package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger

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
  
  def fromHouseInt(housenum: Option[Int])(intervals: List[HouseInt]): List[HouseRsp] = {
    intervals.map (fromInt(housenum)(_)).flatten
  }
  def fromInt(housenum: Option[Int])(houseInt: HouseInt): List[HouseRsp] = {
    val eststatus =  estMap(2)
     housenum match {
      case Some(n) => {
           List(HouseRsp(houseInt.aoguid,None,Some(houseInt.intguid),houseInt.postalcode, Some(n.toString()),eststatus,None,None,None))
      }
      case None => {
         
        val step = if(houseInt.intstatus<2) 1 else 2
    
        (for(i <- houseInt.intstart.to(houseInt.intend, step)) yield {
        new HouseRsp(houseInt.aoguid,None,Some(houseInt.intguid),houseInt.postalcode, Some(i.toString()),eststatus,None,None,None)
     }) toList   
      }
    }
    
     
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