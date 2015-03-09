package controllers

import models._
import hlp.Hlp
import java.util.Date


trait DbService extends Hlp {
 def findUser(apikey: String): Option[User]
 def listRegion(formalName: Option[String]): List[AddrObjRsp]
 def listChild(parent: Option[String], formalName: Option[String]): List[AddrObjRsp]
 def listHouseOnly(parentguid: String, stext: String, date: Date): List[HouseRsp]
 def listHouseInt(parentguid: String, housenum: Option[Int], date: Date): List[HouseInt]
 
 def listHouse(parent: Option[String], housenum: Option[String]): List[HouseRsp] = {
    val parentguid = checkParentGuid(parent)
    val stext: String = tt(housenum)
    val date = new Date()
    val hn = checkHouseNum(housenum)
    val houses = listHouseOnly(parentguid, stext, date)
    val intervals = HouseRsp.fromHouseInt(hn)(listHouseInt(parentguid, hn, date))
    combine(houses, intervals)
  }
}