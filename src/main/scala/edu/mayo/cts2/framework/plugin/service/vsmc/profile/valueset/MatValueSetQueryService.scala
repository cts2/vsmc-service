package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import edu.mayo.cts2.framework.filter.`match`.AttributeResolver
import edu.mayo.cts2.framework.filter.`match`.ContainsMatcher
import edu.mayo.cts2.framework.filter.`match`.ExactMatcher
import edu.mayo.cts2.framework.filter.`match`.ResolvableMatchAlgorithmReference
import edu.mayo.cts2.framework.filter.`match`.ResolvablePropertyReference
import edu.mayo.cts2.framework.filter.`match`.StartsWithMatcher
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.ScalaJSON
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.VsacRestDao
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService

import scala.collection.immutable.Set

import javax.annotation.Resource

@Component
class MatValueSetQueryService
  extends AbstractService
  with ValueSetQueryService {

  val NQF_NUMBER_PROP: String = "nqfnumber"
  val EMEASURE_ID_PROP: String = "emeasureid"
    
  val matchAlgorithms: Set[ResolvableMatchAlgorithmReference] = buildSupportedMatchAlgorithms
  val searchReferences: Set[ResolvablePropertyReference[ScalaJSON]] = buildSearchReferences
    
  @Resource
  var vsacRestDao:VsacRestDao = _

  private def buildSupportedMatchAlgorithms: Set[ResolvableMatchAlgorithmReference] = {
    val contains = ResolvableMatchAlgorithmReference.toResolvableMatchAlgorithmReference(
        StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference, 
        new ContainsMatcher())
    val startsWith = ResolvableMatchAlgorithmReference.toResolvableMatchAlgorithmReference(
        StandardMatchAlgorithmReference.STARTS_WITH.getMatchAlgorithmReference, 
        new StartsWithMatcher())
    val exactMatch = ResolvableMatchAlgorithmReference.toResolvableMatchAlgorithmReference(
        StandardMatchAlgorithmReference.EXACT_MATCH.getMatchAlgorithmReference, 
        new ExactMatcher())
    
    Set(contains,contains,exactMatch)
  }
  
  override
  def getSupportedMatchAlgorithms: java.util.Set[ResolvableMatchAlgorithmReference] = {
    matchAlgorithms
  }
  
  class ScalaJSONAttributeResolver(attributeName:String) extends AttributeResolver[ScalaJSON] {
    
    override
    def resolveAttribute(row:ScalaJSON):java.lang.Iterable[String] = {
      List[String](row.attributeName)
    }
  }
  
  override
  def getSupportedSearchReferences: java.util.Set[ResolvablePropertyReference[ScalaJSON]] = {
     searchReferences
  }

  private def buildSearchReferences: Set[ResolvablePropertyReference[ScalaJSON]] = {
    val resourceName = ResolvablePropertyReference.toPropertyReference(
        StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference, 
        new ScalaJSONAttributeResolver("oid"))

  	Set(resourceName)
  }

  def getSupportedSortReferences: java.util.Set[_ <: PropertyReference] = { new java.util.HashSet[PropertyReference]() }

  def getKnownProperties: java.util.Set[PredicateReference] = { new java.util.HashSet[PredicateReference]() }

  @Transactional
  def getResourceSummaries(query: ValueSetQuery, sort: SortCriteria, page: Page = new Page()): DirectoryResult[ValueSetCatalogEntrySummary] = {
    new ValueSetDirectoryBuilder(
        vsacRestDao.getAllValueSets, 
        matchAlgorithms, 
        searchReferences).addStart(page.getStart).addMaxToReturn(page.getEnd).resolve()
  }

  def getResourceList(p1: ValueSetQuery, p2: SortCriteria, p3: Page): DirectoryResult[ValueSetCatalogEntry] = null

  def count(p1: ValueSetQuery): Int = 0

}