package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import java.lang.Override

import scala.collection.JavaConversions._

import org.springframework.stereotype.Component

import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.SourceAndNotation
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.extension.LocalIdValueSetDefinition
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.VsacRestDao
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import javax.annotation.Resource

@Component
class VsmcValueSetDefinitionReadService extends AbstractService with ValueSetDefinitionReadService {

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

    val versionId = vsacRestDao.getValueSetDefinitionVersions(valueSetName)(0)

    if (versionId != null) {
      new LocalIdValueSetDefinition(versionId, null)
    } else {
      null
    }
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