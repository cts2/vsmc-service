package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import scala.collection.JavaConversions._
import scala.collection.JavaConversions.iterableAsScalaIterable

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService

@Component
class VsmcValueSetDefinitionQueryService
  extends AbstractService
  with ValueSetDefinitionQueryService {

  def getSupportedMatchAlgorithms: java.util.Set[_ <: MatchAlgorithmReference] = { 
    val set = new java.util.HashSet[MatchAlgorithmReference]() 
    set.add(StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference)
    set.add(StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference)
    set
  }

  def getSupportedSearchReferences: java.util.Set[_ <: PropertyReference] = { 
    val set = new java.util.HashSet[PropertyReference]() 
    set.add(StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference)
    set.add(StandardModelAttributeReference.RESOURCE_SYNOPSIS.getPropertyReference)
    
    set
  }

  def getSupportedSortReferences: java.util.Set[_ <: PropertyReference] = { new java.util.HashSet[PropertyReference]() }

  def getKnownProperties: java.util.Set[PredicateReference] = { new java.util.HashSet[PredicateReference]() }

  @Transactional
  def getResourceSummaries(query: ValueSetDefinitionQuery, sort: SortCriteria, page: Page = new Page()): DirectoryResult[ValueSetDefinitionDirectoryEntry] = {
   null
  }


  def getResourceList(p1: ValueSetDefinitionQuery, p2: SortCriteria, p3: Page): DirectoryResult[ValueSetDefinition] = null

  def count(p1: ValueSetDefinitionQuery): Int = 0

}