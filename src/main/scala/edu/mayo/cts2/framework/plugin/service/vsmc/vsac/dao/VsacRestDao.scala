package edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao;

import scala.collection.JavaConversions._

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import JSON._
import dispatch._
import net.minidev.json.JSONObject

@Component
class VsacRestDao {

  @scala.reflect.BeanProperty
  @Value("${utsUsername}")
  var username: String = _

  @scala.reflect.BeanProperty
  @Value("${utsPassword}")
  var password: String = _

  def getValueSet(oid:String) = {
    val json = getJson(
      "https://vsac.nlm.nih.gov/vsac/pc/vs/valueset/"+oid)

    parseJSON(json)
  }

  def getValueSetDefinition(oid:String, version:String) = {
    val json = getJson(
        "https://vsac.nlm.nih.gov/vsac/pc/vs/valueset/"+oid+"/def/"+version)
        
    parseJSON(json)
  }

  def getValueSetDefinitionVersions(oid:String) = {
    val json = getJson(
        "https://vsac.nlm.nih.gov/vsac/pc/vs/valueset/"+oid+"/def-versions")
        
    parseJSON(json).rows.foldLeft(Seq[String]())(_ :+ _.name.toString).sortWith(_ < _)
  }
  
  def getAllValueSets: Seq[ScalaJSON] = {
    val json = postJson("https://vsac.nlm.nih.gov/vsac/pc/vs/search", allValueSetsQueryParams)

    parseJSON(json).rows
  }

  private def allValueSetsQueryParams() = {
    Map(
      "query" -> "***ListAll***",
      "cms" -> null,
      "category" -> null,
      "developer" -> null,
      "mu" -> null)
  }

  def getMembersOfValueSet(oid: String, version: String, rows: Int, page: Int): ScalaJSON = {

    val url = "https://vsac.nlm.nih.gov/vsac/pc/code/codes"
    val params =
      Map(
        "oid" -> oid,
        "def" -> version,
        "_search" -> "false",
        "rows" -> rows.toString,
        "page" -> page.toString,
        "sidx" -> "code",
        "sord" -> "asc")

    val json = getJson(url, params)

    parseJSON(json)
  }

  private def getJson(urlString: String, queryParams: Map[String, String] = Map()): String = {
    val result = Http(url(urlString).GET.secure.as_!(username, password) <<? queryParams OK as.String).either

    result() match {
      case Right(content) => content
      case Left(StatusCode(404)) => throw new RuntimeException("Not Found");
      case Left(StatusCode(code)) => throw new RuntimeException("Error: " + code);
    }
  }

  private def postJson(urlString: String, params: Map[String, String]): String = {
    val queryJson = new JSONObject(params).toJSONString

    val result = Http(url(urlString).POST.secure.as_!(username, password) << queryJson OK as.String).either

    result() match {
      case Right(content) => content
      case Left(StatusCode(404)) => throw new RuntimeException("Not Found");
      case Left(StatusCode(code)) => throw new RuntimeException("Error: " + code);
    }
  }
}
