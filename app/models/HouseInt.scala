package models
import scalikejdbc._

case class HouseInt( 
    aoguid: String, 
    intguid: String,
    postalcode: Option[String],
    intstatus: Int,
    intstart: Int,
    intend: Int){
  
}
object HouseInt{
  def fromInterval(rs: WrappedResultSet): HouseInt = 
    new HouseInt(rs.string("aoguid"),rs.string("intguid"),rs.stringOpt("postalcode"),rs.int("intstatus"),rs.int("intstart"),rs.int("intend"))
  
}