package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.RoleReference
import edu.mayo.cts2.framework.model.core.SourceAndRoleReference
import edu.mayo.cts2.framework.model.core.SourceReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.ScalaJSON
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils

object VsmcValueSetUtils {

  def buildValueSetReference(valueSetRow: ScalaJSON, urlConstructor: UrlConstructor): ValueSetReference = {
    val oid = valueSetRow.oid

    val ref = new ValueSetReference()
    ref.setContent(oid)
    ref.setUri(UriUtils.oidToUri(oid))
    ref.setHref(urlConstructor.createValueSetUrl(oid))

    ref
  }

  def buildValueSetDefinitionReference(
                                        valueSetDefRow: ScalaJSON,
                                        urlConstructor: UrlConstructor): ValueSetDefinitionReference = {
    buildValueSetDefinitionReference(
      valueSetDefRow.oid,
      UriUtils.oidToUri(valueSetDefRow.oid),
      valueSetDefRow.oid.toString + ":" + valueSetDefRow.revision.toString,
      UriUtils.oidToUri(valueSetDefRow.oid) + ":" + valueSetDefRow.revision,
      urlConstructor)
  }

  def buildValueSetDefinitionReference(
                                        name: String,
                                        about: String,
                                        defName: String,
                                        defDocUri: String,
                                        urlConstructor: UrlConstructor): ValueSetDefinitionReference = {
    val currentDefinition = new ValueSetDefinitionReference()

    val valueSetRef = new ValueSetReference(name)
    valueSetRef.setUri(about)
    valueSetRef.setHref(urlConstructor.createValueSetUrl(name))
    currentDefinition.setValueSet(valueSetRef)

    val valueSetDefRef = new NameAndMeaningReference(defName)
    valueSetDefRef.setUri(defDocUri)
    valueSetDefRef.setHref(urlConstructor.createValueSetDefinitionUrl(name, defName))
    currentDefinition.setValueSetDefinition(valueSetDefRef)

    currentDefinition
  }

  def sourceAndRole = {
    val sourceAndRoleRef = new SourceAndRoleReference()

    val roleRef = new RoleReference()
    roleRef.setContent("creator")
    roleRef.setUri("http://purl.org/dc/elements/1.1/creator")
    sourceAndRoleRef.setRole(roleRef)

    val sourceRef = new SourceReference("National Committee for Quality Assurance")
    sourceAndRoleRef.setSource(sourceRef)

    sourceAndRoleRef
  }

}