package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import java.util.Set

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._

import org.apache.commons.lang.ObjectUtils
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.EntitySynopsis
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSet
import edu.mayo.cts2.framework.model.valuesetdefinition.ResolvedValueSetHeader
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset.MatValueSetUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.IdType
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriResolver
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResolutionEntityQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import javax.annotation.Resource

@Component
class MatValueSetDefinitionResolutionService extends AbstractService with ValueSetDefinitionResolutionService {

  @Resource
  var hrefBuilder: HrefBuilder = _

  def getSupportedMatchAlgorithms: Set[_ <: MatchAlgorithmReference] = null

  def getSupportedSearchReferences: Set[_ <: PropertyReference] = null

  def getSupportedSortReferences: Set[_ <: PropertyReference] = null

  def getKnownProperties: Set[PredicateReference] = null

  @Transactional
  def resolveDefinition(
    id: ValueSetDefinitionReadId,
    codeSystemVersions: Set[NameOrURI],
    codeSystemVersionTag: NameOrURI,
    query: ResolvedValueSetResolutionEntityQuery,
    sort: SortCriteria,
    readContext: ResolvedReadContext,
    page: Page): ResolvedValueSetResult[EntitySynopsis] = {

   null
  }

 
  def resolveDefinitionAsEntityDirectory(p1: ValueSetDefinitionReadId, p2: Set[NameOrURI], p3: NameOrURI, p4: ResolvedValueSetResolutionEntityQuery, p5: SortCriteria, p6: ResolvedReadContext, p7: Page): ResolvedValueSetResult[EntityDirectoryEntry] = null

  def resolveDefinitionAsCompleteSet(p1: ValueSetDefinitionReadId, p2: Set[NameOrURI], p3: NameOrURI, p4: ResolvedReadContext): ResolvedValueSet = null
}