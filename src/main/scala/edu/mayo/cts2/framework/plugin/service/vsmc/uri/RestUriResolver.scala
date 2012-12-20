package edu.mayo.cts2.framework.plugin.service.vsmc.uri

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import clojure.lang.RT

@Component
class RestUriResolver extends UriResolver {

  RT.loadResourceScript("cts2/uri/UriResolutionService.clj");

  val getUri = RT.`var`("cts2.uri", "getUri")
  val getName = RT.`var`("cts2.uri", "getName")
  val getBaseEntityUri = RT.`var`("cts2.uri", "getBaseEntityUri")
  val getVersionName = RT.`var`("cts2.uri", "getVersionName")
  val getVersionUri = RT.`var`("cts2.uri", "getVersionUri")

  @scala.reflect.BeanProperty
  @Value("${uriResolutionServiceUrl}")
  var uriResolutionServiceUrl: String = _

  def idToUri(id: String, idType: IdType.Value): String = {
    val uri = getUri.invoke(uriResolutionServiceUrl, idType, id)

    if (uri != null) {
      uri.toString
    } else {
      throw new IllegalStateException("No URI Found for: " + id)
    }
  }

  def idToName(id: String, idType: IdType.Value): String = {
    val name = getName.invoke(uriResolutionServiceUrl, idType, id)

    if (name != null) {
      name.toString
    } else {
      throw new IllegalStateException("No Name Found for: " + id)
    }
  }

  def idToBaseUri(id: String): String = {
    val baseEntityUri = getBaseEntityUri.invoke(uriResolutionServiceUrl, id)

    if (baseEntityUri != null) {
      baseEntityUri.toString
    } else {
      throw new IllegalStateException("No BaseEntityURI Found for: " + id)
    }
  }

  def idAndVersionToVersionName(id: String, versionId: String, idType: IdType.Value): String = {
    val versionName = getVersionName.invoke(uriResolutionServiceUrl, idType, id, versionId)

    if (versionName != null) {
      versionName.toString
    } else {
      throw new IllegalStateException("No VersionName Found for: " + id + ", " + versionId)
    }
  }

  def idAndVersionToVersionUri(id: String, versionId: String, idType: IdType.Value): String = {
    val versionUri = getVersionUri.invoke(uriResolutionServiceUrl, idType, id, versionId)

    if (versionUri != null) {
      versionUri.toString
    } else {
      throw new IllegalStateException("No VersionURI Found for: " + id)
    }
  }

}
