package edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao;

import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import net.minidev.json.JSONObject
import JSON._
import dispatch._
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import org.springframework.beans.factory.annotation.Value

@Component
class VsacRestDao {
  
  @scala.reflect.BeanProperty
  @Value("${utsUsername}")
  var username: String = _

  @scala.reflect.BeanProperty
  @Value("${utsPassword}")
  var password: String = _
  
  def getAllValueSets : Seq[ScalaJSON] = {
	  val json = getJson("https://vsac.nlm.nih.gov/vsac/pc/vs/search", allValueSetsQueryParams)
	
	  parseJSON(json).rows
  }
  
  private def allValueSetsQueryParams() = {
    Map(
        "query"->"***ListAll***",
        "cms"->null,
        "category"->null,
        "developer"->null,
        "mu"->null)
  }

  private def getJson(urlString:String, params: Map[String,String]):String = {
    val queryJson = new JSONObject(params).toJSONString
    println(queryJson)
	val result = Http(url(urlString).POST.secure.as_!(username, password) << queryJson OK as.String).either

	result() match {
	  case Right(content)         => content
	  case Left(StatusCode(404))  => throw new RuntimeException("Not Found");
	  case Left(StatusCode(code)) => throw new RuntimeException("Error: " + code);
	}
}

}
