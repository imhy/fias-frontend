package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger

import scalikejdbc._

class HouseRsp(
    val aoguid: String, 
    val houseguid: Option[String], 
    val intguid: Option[String], 
    val postalcode: Option[String], 
    val housenum: Option[String], 
    val eststatus: Option[String], 
    val buildnum: Option[String], 
    val strucnum: Option[String], 
    val strstatus: Option[String]) {
  
    val fullname = buildFullName
    
    def buildFullName(): String = {
      val sb: StringBuilder = new StringBuilder()
      housenum match {
        case Some(n) => {eststatus  match {
          case Some(e) => sb.append(HouseRsp.estMapShrt.getOrElse(e,""))
                          sb.append(" ")
          case _ => ()
          sb.append(n)
        }}
        case _ => ()
      }
      buildnum match {
        case Some(b) => sb.append(" корп.")
                        sb.append(" ")
                        sb.append(b)
        case _ => ()
      }
      strucnum match {
        case Some(s) => {
          strstatus match {
            case Some(st) => sb.append(" ")
                             sb.append(HouseRsp.stMapShrt.getOrElse(st,""))
                             sb.append(" ")
            case _ =>()
            sb.append(s)
          }
        }
        case _ => ()
      }
      sb.toString.trim
    }
}



object HouseRsp{
  val estMap: Map[Int,Option[String]] = Map(0 ->None,1->Some("владение"),2->Some("дом"), 3 -> Some("домовладение"))
  val stMap: Map[Int,Option[String]] = Map(0 ->None,1->Some("строение"),2->Some("сооружение"), 3 -> Some("литер"))
  
  val estMapShrt: Map[String,Option[String]] = Map("владение"->Some("вл."),"дом"->Some("д."), "домовладение" -> Some("домовл."))
  val stMapShrt: Map[String,Option[String]] = Map("строение" -> Some("стр."),"сооружение" -> Some("соор."), "литер" -> Some("лит."))
  
  def fromRs(rs: WrappedResultSet) = {
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
     val step = if(houseInt.intstatus<2) 1 else 2
     housenum match {
      case Some(n) => {
         (for(i <- houseInt.intstart.to(houseInt.intend, step) if i.toString.toLowerCase.startsWith(n.toString.toLowerCase)) yield {
          new HouseRsp(houseInt.aoguid,None,Some(houseInt.intguid),houseInt.postalcode, Some(i.toString()),eststatus,None,None,None)
         }) toList 
      }
      case None => {
         
       
    
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
    "strstatus" -> houseRsp.strstatus,
    "fullname" -> houseRsp.fullname
  )
}
}