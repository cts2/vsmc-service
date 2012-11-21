package edu.mayo.cts2.framework.plugin.service.vsmc.uri

object UriUtils {

  val URN_PREFIX = "urn:"
    
  val OID_PREFIX = "oid:"
    
  val UUID_PREFIX = "uuid:"
    
  val OID_URI_PREFIX = URN_PREFIX + OID_PREFIX
  
  val UUID_URI_PREFIX = URN_PREFIX + UUID_PREFIX
  
  val SVS_NS = "SVS"
  
  val SVS_URI = "urn:ihe:iti:svs:2008"

  def oidToUri(oid: String) = OID_URI_PREFIX + oid
  
  def uuidToUri(uuid: String) = UUID_URI_PREFIX + uuid
  
  def toSvsUri(localPart: String) = SVS_URI + "/" + localPart

}