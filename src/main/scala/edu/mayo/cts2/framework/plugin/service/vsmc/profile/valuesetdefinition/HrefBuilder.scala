package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.core.url.UrlConstructor
import javax.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.ScalaJSON

@Component
class HrefBuilder {

  @Resource
  var urlConstructor: UrlConstructor = _

  @scala.reflect.BeanProperty
  @Value("${snomedCtUrlBase}")
  var snomedCtUrlBase: String = _

  val SNOMEDCT = "SNOMED-CT"
  val UMLS_CODE_SYSTEMS = Set("CPT", "ICD-10-CM", "ICD-9-CM", "RxNorm", "LOINC")

  def createEntityHref(row: ScalaJSON) = {

    def createUmlsUtsUrl = {
      urlConstructor.createEntityUrl(
        csNameToSab(row.codesystemname),
        csNameAndVersionToCsVersionName(row.codesystemname, row.codesystemversion),
        row.code)
    }

    val cs = row.codesystemname

    if (contains(cs, UMLS_CODE_SYSTEMS)) {
      createUmlsUtsUrl
    } else if (contains(cs, Set(SNOMEDCT))) {
      if (StringUtils.isNotBlank(snomedCtUrlBase)) {
        snomedCtUrlBase + "/" + row.code
      } else {
        createUmlsUtsUrl
      }
    } else {
      null
    }
  }

  def contains(cs: String, compare: String): Boolean = {
    contains(cs, Set(compare))
  }

  def contains(cs: String, set: Set[String]): Boolean = {
    !set.forall((s) => {
      !s.replaceAll("-", "").toLowerCase.equals(cs.replaceAll("-", "").toLowerCase)
    })
  }

  def csNameAndVersionToCsVersionName(csName: String, versionId: String) = {
    val version =
      if (StringUtils.isBlank(versionId)) {
        "unknown"
      } else {
        versionId
      }
    csNameToSab(csName) + "-" + StringUtils.replaceChars(version, "/", "-")
  }

  def csNameToSab(csName: String) = {
    csName match {
      case "LOINC" => "LNC"
      case _ => StringUtils.upperCase(StringUtils.remove(csName, '-'))
    }
  }: String

}