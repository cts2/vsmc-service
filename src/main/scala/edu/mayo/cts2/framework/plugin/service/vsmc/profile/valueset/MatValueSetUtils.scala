package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import scala.collection.JavaConversions.iterableAsScalaIterable
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import org.springframework.context.annotation.ScopedProxyMode
import scala.collection.JavaConversions._
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import org.apache.commons.collections.CollectionUtils
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.core.url.UrlConstructor
import edu.mayo.cts2.framework.model.core.SourceReference
import edu.mayo.cts2.framework.model.core.SourceAndRoleReference
import edu.mayo.cts2.framework.model.core.RoleReference
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import org.apache.commons.lang.StringUtils

object MatValueSetUtils {

  def buildValueSetDefinitionReference(
    name: String, about: String,
    defName: String, defDocUri: String,
    urlConstructor: UrlConstructor) = {
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
  }: ValueSetDefinitionReference

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