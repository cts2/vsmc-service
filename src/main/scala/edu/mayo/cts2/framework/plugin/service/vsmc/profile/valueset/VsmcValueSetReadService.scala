package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import java.lang.Override
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core._
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.{ScalaJSON, VsacRestDao}
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils._
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._

@Component
class VsmcValueSetReadService extends AbstractService with ValueSetReadService {

  @Resource
  var vsacRestDao: VsacRestDao = _

  @Override
  def read(
            identifier: NameOrURI,
            readContext: ResolvedReadContext): ValueSetCatalogEntry = {

    val id = getOid(identifier)

    val versions = vsacRestDao.getValueSetDefinitionVersions(id)

    if (versions.size == 0) {
      null
    } else {
      val current = versions.last

      val valueSetJson = vsacRestDao.getValueSetDefinition(id, current)

      rowToValueSet(valueSetJson)
    }
  }

  private def getOid(nameOrUri: NameOrURI) = {
    if (nameOrUri.getName != null) {
      nameOrUri.getName
    } else {
      val uri = nameOrUri.getUri
      UriUtils.stripUriPrefix(uri)
    }
  }

  private def rowToValueSet(jsonRow: ScalaJSON): ValueSetCatalogEntry = {
    val oid = jsonRow.oid
    val name = jsonRow.name
    val valueSet = new ValueSetCatalogEntry()
    valueSet.setAbout(oidToUri(oid))
    valueSet.setValueSetName(oid)
    valueSet.setFormalName(name)
    valueSet.addAlternateID(oid)

    val description = new EntryDescription()
    description.setValue(ModelUtils.toTsAnyType(name))

    valueSet.setResourceSynopsis(description)

    Seq(
      ("nqfs", jsonRow.nqfs),
      ("cms", jsonRow.cms),
      ("groupingDescription", jsonRow.groupingDescription),
      ("type", jsonRow.`type`),
      ("developer", jsonRow.developer)
    ) foreach {
      (prop) => {
        val name = prop._1
        val value = prop._2

        if(value != null){
          valueSet.addProperty(createProperty(name, value)   )
        }
      }

    }

    valueSet
  }

  private def createProperty(name: String, value: String) = {
    val prop = new Property()

    val predicate = new PredicateReference()
    predicate.setName(name)
    predicate.setNamespace(UriUtils.SVS_NS)
    predicate.setUri(UriUtils.toSvsUri(name))
    prop.setPredicate(predicate)

    val target = new StatementTarget()
    target.setLiteral(ModelUtils.createOpaqueData(value))
    prop.addValue(target)

    prop
  }

  @Override
  def exists(identifier: NameOrURI, readContext: ResolvedReadContext): Boolean = {
    throw new UnsupportedOperationException()
  }

}