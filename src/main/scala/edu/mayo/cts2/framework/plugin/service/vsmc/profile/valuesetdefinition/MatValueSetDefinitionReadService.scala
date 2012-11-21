package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import java.lang.Override
import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition
import org.springframework.transaction.annotation.Transactional
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionEntry
import edu.mayo.cts2.framework.model.valuesetdefinition.SpecificEntityList
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.SetOperator
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.IdType
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.ValueSetReference
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset.MatValueSetUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import edu.mayo.cts2.framework.model.valuesetdefinition.CompleteValueSetReference
import org.apache.commons.lang.StringUtils
import edu.mayo.cts2.framework.model.core.StatusReference
import edu.mayo.cts2.framework.model.core.types.EntryState

@Component
class MatValueSetDefinitionReadService extends AbstractService with ValueSetDefinitionReadService {

  val SIZE_LIMIT = 100;
 
  /**
   * This is incomplete... this is only here to map the 'CURRENT' tag to a CodeSystemVersionName.
   */
  @Override
  def readByTag(
    valueSet: NameOrURI,
    tag: VersionTagReference, readContext: ResolvedReadContext): LocalIdValueSetDefinition = {

      null
  }

  @Override
  def existsByTag(valueSet: NameOrURI,
    tag: VersionTagReference, readContext: ResolvedReadContext): Boolean = {
    readByTag(valueSet, tag, readContext) != null
  }

  @Override
  @Transactional
  def read(
    identifier: ValueSetDefinitionReadId,
    readContext: ResolvedReadContext): LocalIdValueSetDefinition = {
   
      null

  }

 

  private def buildSourceAndNotation(): SourceAndNotation = {
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