package controllers

import play.api.Logger
import scalikejdbc._
import scalikejdbc.config._
import models._
import java.util.Date



object DbConnect {
DBs.setupAll()
  def listUsers()(implicit session: DBSession = ReadOnlyAutoSession) = sql"select userid from fiasuser".map(_.string(1)).list.apply()

  def listRegion(formalName: Option[String])(implicit session: DBSession = ReadOnlyAutoSession): List[AddrObjRsp] = {
    val aolevel = 1
    val stext: String = tt(formalName)
    sql"""select regioncode, postalcode, shortname, offname, aolevel, aoguid from addressobject where livestatus = 1 and aolevel = ${aolevel} and lower(formalname) like ${stext} order by formalname""".map(rs => AddrObjRsp.fromRs(rs)).list.apply()
  }

  def listChild(parent: Option[String], formalName: Option[String])(implicit session: DBSession = ReadOnlyAutoSession): List[AddrObjRsp] = {
    val parentguid = parent.getOrElse("-")
    if(parentguid.length()!= 36) throw new IllegalArgumentException("parentguid is wrong")
    val stext: String = tt(formalName)
    
    sql"""select regioncode, postalcode, shortname, offname, aolevel, aoguid from addressobject where parentguid = ${parentguid} and livestatus = 1 and lower(formalname) like ${stext} order by formalname""".map(rs => AddrObjRsp.fromRs(rs)).list.apply()
  }
  
  def tt(itext:Option[String]): String = {
    itext match {
      case None => "%"
      case Some(t) => if(t.equalsIgnoreCase("*")) "%" else t.trim().toLowerCase()+"%"
    }
  }
  
  def listHouse(parent: Option[String], housenum: Option[String])(implicit session: DBSession = ReadOnlyAutoSession): List[HouseRsp] = {
    val parentguid = parent.getOrElse("-")
    if(parentguid.length()!= 36) throw new IllegalArgumentException("parentguid is wrong")
    val stext: String = tt(housenum)
    val date = new Date()
    sql"""select aoguid, houseguid, postalcode, housenum, eststatus, buildnum, strucnum, strstatus from house where aoguid = ${parentguid} and enddate > ${date} and lower(housenum) like ${stext} order by housenum""".map(rs => HouseRsp.fromRs(rs)).list.apply()
  }
}