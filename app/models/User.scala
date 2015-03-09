package models

import scalikejdbc._

case class User(userid: String, apikey: String) {

}

object User {
  def fromRs(rs: WrappedResultSet) =  User(rs.string("userid"),rs.string("apikey"))
}