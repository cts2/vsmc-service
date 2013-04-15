package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import java.lang.Override

import scala.collection.JavaConversions._

import org.springframework.stereotype.Component

import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.{URIAndEntityName, EntryDescription, SourceAndNotation, VersionTagReference}
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.{ScalaJSON, VsacRestDao}
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.valuesetdefinition._
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils._
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset.VsmcValueSetUtils._
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import org.apache.commons.collections.CollectionUtils
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.IdType
import java.io.IOException

@Component
class VsmcValueSetDefinitionReadService extends AbstractService with ValueSetDefinitionReadService {

  val MAX_SPECIFIC_ENTITIES: Int = 100;

  val GROUPING: String = "Grouping";

  @Resource
  var hrefBuilder: HrefBuilder = _

  @Resource
  var vsacRestDao: VsacRestDao = _

  /**
   * This is incomplete... this is only here to map the 'CURRENT' tag to a CodeSystemVersionName.
   */
  @Override
  def readByTag(
                 valueSet: NameOrURI,
                 tag: VersionTagReference, readContext: ResolvedReadContext): LocalIdValueSetDefinition = {

    if (tag.getContent() == null || !tag.getContent().equals("CURRENT")) {
      throw new RuntimeException("Only 'CURRENT' tag is supported")
    }

    val valueSetName = valueSet.getName()

    val versions = vsacRestDao.getValueSetDefinitionVersions(valueSetName)

    if (versions == null || CollectionUtils.isEmpty(versions)) {
      return null;
    }

    val versionId = vsacRestDao.getValueSetDefinitionVersions(valueSetName)(0)

    new LocalIdValueSetDefinition(versionId, null)
  }

  @Override
  def existsByTag(valueSet: NameOrURI,
                  tag: VersionTagReference, readContext: ResolvedReadContext): Boolean = {
    readByTag(valueSet, tag, readContext) != null
  }

  @Override
  def read(
            identifier: ValueSetDefinitionReadId,
            readContext: ResolvedReadContext): LocalIdValueSetDefinition = {

    val oid = identifier.getValueSet.getName
    val version = identifier.getName

    val definition =
    try {
      rowToValueSetDefinition(vsacRestDao.getValueSetDefinition(oid, version))
    } catch {
      case ioe: Exception => null
    }

      if (definition != null) {
        new LocalIdValueSetDefinition(version, definition)
      } else {
        null
      }
    }

  private def rowToValueSetDefinition(jsonRow: ScalaJSON): ValueSetDefinition = {
    val oid = jsonRow.oid
    val name = jsonRow.name
    val version = jsonRow.revision
    val grouping = jsonRow.`type`.equals(GROUPING)

    //We still get JSON back even if we asked for the wrong thing, but
    //it will be all null. Check it here.
    if (name.toString == null || version.toString == null) {
      return null
    }

    val valueSetDefinition = new ValueSetDefinition()
    valueSetDefinition.setAbout(oidToUri(oid))
    valueSetDefinition.setDocumentURI(oidAndVersionToUri(oid, version))
    valueSetDefinition.setSourceAndNotation(sourceAndNotation)

    valueSetDefinition.setFormalName(name)

    val description = new EntryDescription()
    description.setValue(ModelUtils.toTsAnyType(name))

    valueSetDefinition.setResourceSynopsis(description)

    valueSetDefinition.setDefinedValueSet(buildValueSetReference(jsonRow, urlConstructor))

    if (grouping) {
      getGroups(oid, version).foreach {
        entry => {
          entry.setEntryOrder(valueSetDefinition.getEntryCount + 1)
          valueSetDefinition.addEntry(entry)
        }
      }
    } else {
      val entry = new ValueSetDefinitionEntry()
      entry.setOperator(SetOperator.UNION)
      entry.setEntryOrder(1)
      entry.setEntityList(getEntries(oid, version))
      valueSetDefinition.addEntry(entry)
    }

    valueSetDefinition
  }

  private def getGroups(oid: String, version: String) = {
    vsacRestDao.getGroupingInfo(oid, version).rows.foldLeft(Seq[ValueSetDefinitionEntry]())(
      (seq, row) => {
        seq :+ buildCompleteValueSetEntry(row)
      }
    )
  }

  private def getEntries(oid: String, version: String) = {
    vsacRestDao.getMembersOfValueSet(oid, version, MAX_SPECIFIC_ENTITIES, 1).rows.foldLeft(new SpecificEntityList())(
      (list, row) => {
        list.addReferencedEntity(buildEntityEntry(row))
        list
      }
    )
  }

  private def buildEntityEntry(json: ScalaJSON) = {
    val csName = uriResolver.idToName(json.codesystemname, IdType.CODE_SYSTEM)
    val code = json.code

    val baseUri = uriResolver.idToBaseUri(csName)

    val entry = new URIAndEntityName()
    entry.setName(code)
    entry.setNamespace(csName)
    entry.setUri(baseUri + code)
    entry.setHref(hrefBuilder.createEntityHref(json))

    entry;
  }

  private def buildCompleteValueSetEntry(json: ScalaJSON) = {
    val oid = json.oid;

    val vsdEntry = new ValueSetDefinitionEntry()
    vsdEntry.setOperator(SetOperator.UNION)

    val valueSetRef = new CompleteValueSetReference();
    valueSetRef.setValueSet(buildValueSetReference(json, urlConstructor))

    vsdEntry.setCompleteValueSet(valueSetRef)

    vsdEntry
  }

  private def sourceAndNotation = {
    val sourceAndNotation = new SourceAndNotation()
    sourceAndNotation.setSourceAndNotationDescription("MAT Authoring Tool Output Zip.")

    sourceAndNotation
  }

  @Override
  def exists(identifier: ValueSetDefinitionReadId, readContext: ResolvedReadContext): Boolean = {
    throw new UnsupportedOperationException()
  }

  def getSupportedTags: java.util.List[VersionTagReference] =
    List[VersionTagReference](CURRENT_TAG)

}