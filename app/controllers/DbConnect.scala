package controllers

import play.api.Logger
import scalikejdbc._
import scalikejdbc.config._
import models._
import java.util.Date




object DbConnect extends DbService{
  DBs.setupAll()
  
  def findUser(apikey: String): Option[User] = {
   implicit val session: DBSession = ReadOnlyAutoSession
   sql"select userid, apikey from fiasuser where apikey = ${apikey}".map(rs => User.fromRs(rs)).single.apply() 
  }
  
  def listRegion(formalName: Option[String]): List[AddrObjRsp] = {
    implicit val session: DBSession = ReadOnlyAutoSession
    val aolevel = 1
    val stext: String = tt(formalName)
    sql"""select regioncode, postalcode, shortname, offname, aolevel, aoguid from addressobject where livestatus = 1 and aolevel = ${aolevel} and lower(formalname) like ${stext} order by formalname""".map(rs => AddrObjRsp.fromRs(rs)).list.apply()
  }

  def listChild(parent: Option[String], formalName: Option[String]): List[AddrObjRsp] = {
    implicit val session: DBSession = ReadOnlyAutoSession
    val parentguid = checkParentGuid(parent)
    val stext: String = tt(formalName)
    
    sql"""select regioncode, postalcode, shortname, offname, aolevel, aoguid from addressobject where parentguid = ${parentguid} and livestatus = 1 and lower(formalname) like ${stext} order by formalname""".map(rs => AddrObjRsp.fromRs(rs)).list.apply()
  }
   
  def listHouseOnly(parentguid: String, stext: String, date: Date): List[HouseRsp] = {
    implicit val session: DBSession = ReadOnlyAutoSession
    sql"""select aoguid, houseguid, postalcode, housenum, eststatus, buildnum, strucnum, strstatus from house where aoguid = ${parentguid} and enddate > ${date} and lower(housenum) like ${stext} order by housenum""".map(rs => HouseRsp.fromRs(rs)).list.apply()
  }
  
  def listHouseInt(parentguid: String, housenum: Option[Int], date: Date): List[HouseInt] = {
    implicit val session: DBSession = ReadOnlyAutoSession
    housenum match {
      case Some(hn) => 
       val even: Int = if(hn%2==0) 3 else 2
       sql"""select aoguid, intguid, postalcode, intstart, intend, intstatus from houseinterval where aoguid = ${parentguid} and enddate > ${date}  and intend >= ${hn} and intstatus <> ${even}""".map(rs => HouseInt.fromRs(rs)).list.apply()
      
      case None => 
        sql"""select aoguid, intguid, postalcode, intstart, intend, intstatus from houseinterval where aoguid = ${parentguid} and enddate > ${date}""".map(rs => HouseInt.fromRs(rs)).list.apply()
         
    }
  }
  
  def listLocality(regioncode: Option[String], formalName: Option[String]): List[AddrObjRsp] = {
    
    implicit val session: DBSession = ReadOnlyAutoSession
    val stext: String = tt(formalName)
    val region: String = checkRegion(regioncode)
    sql"""select a.regioncode regioncode, a.postalcode postalcode, a.shortname shortname, a.offname offname, a.aolevel aolevel, a.aoguid aoguid, p.shortname pshortname, p.offname poffname, p.aoguid paoguid from addressobject a left join addressobject p on a.parentguid = p.aoguid where p.livestatus = 1 and a.livestatus = 1 and a.regioncode = ${region} and a.aolevel in (4,6) and lower(a.formalname) like ${stext} order by a.formalname""".map(rs => AddrObjRsp.fromRsWithParent(rs)).list.apply()
  }
}