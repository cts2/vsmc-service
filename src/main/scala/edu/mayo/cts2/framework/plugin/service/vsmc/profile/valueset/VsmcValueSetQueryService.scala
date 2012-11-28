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
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.filter.`match`.StateAdjustingPropertyReference
import edu.mayo.cts2.framework.filter.`match`.StateAdjustingPropertyReference.StateUpdater
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import org.apache.commons.lang.StringUtils
import edu.mayo.cts2.framework.service.constant.ExternalCts2Constants

@Component
class VsmcValueSetQueryService
  extends AbstractService
  with ValueSetQueryService {

  val NQF_NUMBER_PROP: String = "nqfnumber"
  val EMEASURE_ID_PROP: String = "emeasureid"
  val QDM_CATEGORY_PROP: String = "qdmcategory"
  val MU_PROP: String = "mu"

  val matchAlgorithms: Set[ResolvableMatchAlgorithmReference] = buildSupportedMatchAlgorithms
  val searchReferences: Set[ResolvablePropertyReference[ScalaJSON]] = buildSearchReferences

  @Resource
  var vsacRestDao: VsacRestDao = _

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

    Set(contains, contains, exactMatch)
  }

  override def getSupportedMatchAlgorithms: java.util.Set[ResolvableMatchAlgorithmReference] = {
    matchAlgorithms
  }

  class ScalaJSONAttributeResolver(
    val attributeName: String,
    val canBeArray: Boolean) extends AttributeResolver[ScalaJSON] {

    override def resolveAttribute(row: ScalaJSON): java.lang.Iterable[String] = {
      val json = row.apply(attributeName)

      if (canBeArray) {
        val tokens = StringUtils.split(json, ' ')
        if (tokens != null) {
          tokens.toList
        } else {
          List[String]()
        }
      } else {
        List[String](json)
      }
    }
  }

  override def getSupportedSearchReferences: java.util.Set[ResolvablePropertyReference[ScalaJSON]] = {
    searchReferences
  }

  private def buildSearchReferences: Set[ResolvablePropertyReference[ScalaJSON]] = {
    val resourceName = ResolvablePropertyReference.toPropertyReference(
      StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference,
      new ScalaJSONAttributeResolver("oid", false))

    val resourceSynopsis = ResolvablePropertyReference.toPropertyReference(
      StandardModelAttributeReference.RESOURCE_SYNOPSIS.getPropertyReference,
      new ScalaJSONAttributeResolver("name", false))

    val nqfNumber =
      createPropertyReference(
        TargetReferenceType.PROPERTY,
        NQF_NUMBER_PROP,
        UriUtils.toSvsUri("NQF Number"),
        "nqfs",
        true)

    val qdmCategory =
      createPropertyReference(
        TargetReferenceType.PROPERTY,
        QDM_CATEGORY_PROP,
        UriUtils.toSvsUri("Quality Data Model Category"),
        QDM_CATEGORY_PROP,
        false)

    val meaningfulUse =
      createPropertyReference(
        TargetReferenceType.PROPERTY,
        MU_PROP,
        UriUtils.toSvsUri("Meaningful Use Measures"),
        MU_PROP,
        false)

    val emeasureId =
      createPropertyReference(
        TargetReferenceType.PROPERTY,
        EMEASURE_ID_PROP,
        UriUtils.toSvsUri("eMeasure Identifier"),
        "measureid",
        true)

    val developer =
      createPropertyReference(
        TargetReferenceType.ATTRIBUTE,
        "source",
        ExternalCts2Constants.buildModelAttributeUri("source"),
        "developerShort",
        false)

    Set(resourceName, resourceSynopsis, nqfNumber, qdmCategory, meaningfulUse, emeasureId, developer)
  }

  def getSupportedSortReferences: java.util.Set[_ <: PropertyReference] = { new java.util.HashSet[PropertyReference]() }

  def getKnownProperties: java.util.Set[PredicateReference] = { new java.util.HashSet[PredicateReference]() }

  @Transactional
  def getResourceSummaries(query: ValueSetQuery, sort: SortCriteria, page: Page = new Page()): DirectoryResult[ValueSetCatalogEntrySummary] = {
    var builder = new ValueSetDirectoryBuilder(
      vsacRestDao.getAllValueSets,
      matchAlgorithms,
      searchReferences).
      addStart(page.getStart).
      addMaxToReturn(page.getEnd)

    if (query != null) {
      builder = builder.restrict(query.getFilterComponent)
    }

    builder.resolve()
  }

  def createPropertyReference(
    referenceType: TargetReferenceType,
    name: String,
    uri: String,
    jsonElement: String,
    canBeArray: Boolean = false): ResolvablePropertyReference[ScalaJSON] = {

    val attributeResolver = new ScalaJSONAttributeResolver(jsonElement, canBeArray);

    val ref = new ResolvablePropertyReference[ScalaJSON](attributeResolver)
    ref.setReferenceType(referenceType)
    ref.setReferenceTarget(new URIAndEntityName())
    ref.getReferenceTarget.setName(name)
    ref.getReferenceTarget.setUri(uri)

    ref
  }

  def getResourceList(p1: ValueSetQuery, p2: SortCriteria, p3: Page): DirectoryResult[ValueSetCatalogEntry] = throw new UnsupportedOperationException()

  def count(p1: ValueSetQuery): Int = throw new UnsupportedOperationException()

}