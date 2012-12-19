package edu.mayo.cts2.framework.plugin.service.vsmc.uri

import org.apache.commons.lang.StringUtils

object UriUtils {

  val URN_PREFIX = "urn:"
    
  val OID_PREFIX = "oid:"
    
  val UUID_PREFIX = "uuid:"

  val VERSION_SEPARATOR = "/version/"
    
  val OID_URI_PREFIX = URN_PREFIX + OID_PREFIX
  
  val UUID_URI_PREFIX = URN_PREFIX + UUID_PREFIX
  
  val SVS_NS = "SVS"
  
  val SVS_URI = "urn:ihe:iti:svs:2008"

  def oidToUri(oid: String) = OID_URI_PREFIX + oid

  def oidAndVersionToUri(oid: String, version: String) = oidToUri(oid) + VERSION_SEPARATOR + version
  
  def uuidToUri(uuid: String) = UUID_URI_PREFIX + uuid
  
  def toSvsUri(localPart: String) = SVS_URI + "/" + localPart

  def stripUriPrefix(oid: String) = StringUtils.removeStartIgnoreCase(oid, OID_URI_PREFIX)

}