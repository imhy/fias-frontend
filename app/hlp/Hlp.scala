package hlp

import models.HouseRsp

trait Hlp {
def checkHouseNum(housenum: Option[String]): Option[Int] = {
    def toInt(s: String):Option[Int] = {
      try {
        Some(s.toInt)
      } catch {
        case e:Exception => None
      }
    }
    housenum match {
      case None => None
      case Some(n) => toInt(n)
    }
  }
 def tt(itext:Option[String]): String = {
    itext match {
      case None => "%"
      case Some(t) => if(t.equalsIgnoreCase("*")) "%" else t.trim().toLowerCase()+"%"
    }
  }
 
 def checkParentGuid(parent: Option[String]): String = {
    val parentguid = parent.getOrElse("-") 
    if(parentguid.length()!= 36) throw new IllegalArgumentException("parentguid is wrong")
    parentguid
  }
 
 def combine(first: List[HouseRsp], second:List[HouseRsp]): List[HouseRsp] = {
   (first:::second).sortWith(compHouse(_,_))
 }
 def compHouse(e1: HouseRsp, e2: HouseRsp): Boolean = {
   compByHousenum(e1, e2) match {
     case Some(h) => h < 0
     case None => compByBuildNum(e1, e2) match {
       case Some(b) => b < 0
       case None => compByStrucNum(e1, e2) match {
         case Some(s) => s < 0
         case None => false
       }
     }
   } 
 }
 
 def compByHousenum(e1: HouseRsp, e2: HouseRsp): Option[Int] = {
   e1.housenum match {
     case Some(n1) => e2.housenum  match {
       case None => Some(1)
       case Some(n2) => if(n1.compareToIgnoreCase(n2)==0) None else Some(n1.compareToIgnoreCase(n2))
     }
     case None => e2.housenum  match {
       case None => None
       case Some(n2) => Some(-1)
     }
   } 
 }
 def compByBuildNum(e1: HouseRsp, e2: HouseRsp): Option[Int] = {
   e1.buildnum match {
     case Some(n1) => e2.buildnum  match {
       case None => Some(1)
       case Some(n2) => if(n1.compareToIgnoreCase(n2)==0) None else Some(n1.compareToIgnoreCase(n2))
     }
     case None => e2.buildnum  match {
       case None => None
       case Some(n2) => Some(-1)
     }
   } 
 }
 def compByStrucNum(e1: HouseRsp, e2: HouseRsp): Option[Int] = {
   e1.strucnum match {
     case Some(n1) => e2.strucnum  match {
       case None => Some(1)
       case Some(n2) => if(n1.compareToIgnoreCase(n2)==0) None else Some(n1.compareToIgnoreCase(n2))
     }
     case None => e2.strucnum  match {
       case None => None
       case Some(n2) => Some(-1)
     }
   } 
 }
}