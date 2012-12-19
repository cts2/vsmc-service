package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import scala.collection.JavaConversions._

import actors.{Future, Futures}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.core._
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService
import javax.annotation.Resource
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.{ScalaJSON, VsacRestDao}
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils._
import edu.mayo.cts2.framework.model.util.ModelUtils
import scala.actors.Futures._
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset.VsmcValueSetUtils._

@Component
class VsmcValueSetDefinitionQueryService
  extends AbstractService
  with ValueSetDefinitionQueryService {

  @Resource
  var vsacRestDao: VsacRestDao = _

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
    if(query.getRestrictions.getValueSet != null){
      val oid = query.getRestrictions.getValueSet.getName
      val versions = vsacRestDao.getValueSetDefinitionVersions(oid)

      val getValueSetFunctions = versions.foldLeft(Seq[Future[Any]]())(
      {
        (seq, ver) => seq :+ future {
          vsacRestDao.getValueSetDefinition(oid, ver)
        }
      })

      val directoryEntries = Futures.awaitAll(1000*20, getValueSetFunctions:_*).foldLeft(Seq[ValueSetDefinitionDirectoryEntry]()){
        (seq,json) => {
          seq :+ rowToValueSetDefinition(json.asInstanceOf[Option[ScalaJSON]].get)
        }
      }

      new DirectoryResult(directoryEntries, true)
    } else {
      throw new UnsupportedOperationException()
    }
  }

  private def rowToValueSetDefinition(jsonRow: ScalaJSON) : ValueSetDefinitionDirectoryEntry = {
    val oid = jsonRow.oid
    val name = jsonRow.name
    val version = jsonRow.revision

    val valueSetDefinition = new ValueSetDefinitionDirectoryEntry()
    valueSetDefinition.setAbout(oidToUri(oid))
    valueSetDefinition.setDocumentURI(oidAndVersionToUri(oid, version))
    //valueSetDefinition.setValueSetName(oid)
    valueSetDefinition.setResourceName(oid)
    valueSetDefinition.setFormalName(name)

    val description = new EntryDescription()
    description.setValue(ModelUtils.toTsAnyType(name))

    valueSetDefinition.setResourceSynopsis(description)

    valueSetDefinition.setDefinedValueSet(buildValueSetReference(jsonRow, urlConstructor))

    valueSetDefinition
  }


  def getResourceList(p1: ValueSetDefinitionQuery, p2: SortCriteria, p3: Page): DirectoryResult[ValueSetDefinition] = null

  def count(p1: ValueSetDefinitionQuery): Int = 0

}