package controllers

import models._


trait DbService {
 def listAddrObj(qp: AddrObjReq): List[AddrObjRsp]
}