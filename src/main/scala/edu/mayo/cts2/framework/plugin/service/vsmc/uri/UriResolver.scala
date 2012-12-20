package edu.mayo.cts2.framework.plugin.service.vsmc.uri

trait UriResolver {

  def idToUri(id: String, idType: IdType.Value): String

  def idToName(id: String, idType: IdType.Value): String

  def idToBaseUri(id: String): String

  def idAndVersionToVersionName(id: String, versionId: String, idType: IdType.Value): String

  def idAndVersionToVersionUri(id: String, versionId: String, idType: IdType.Value): String
}

object IdType extends Enumeration {
  type IdType = Value
  val CODE_SYSTEM, VALUE_SET, CODE_SYSTEM_VERSION = Value
}