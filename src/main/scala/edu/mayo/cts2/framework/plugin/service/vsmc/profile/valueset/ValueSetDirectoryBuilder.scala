package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import scala.collection.JavaConversions._
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.JSON._
import edu.mayo.cts2.framework.filter.`match`.ResolvablePropertyReference
import edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao.ScalaJSON
import edu.mayo.cts2.framework.filter.`match`.ResolvableMatchAlgorithmReference
import edu.mayo.cts2.framework.filter.directory.AbstractRemovingDirectoryBuilder
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils._
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.model.core.EntryDescription
import edu.mayo.cts2.framework.core.url.UrlConstructor

case class ValueSetDirectoryBuilder(
                                     urlConstructor: UrlConstructor,
                                     allPossibleResults: Seq[ScalaJSON],
                                     matchAlgorithmReferences: Set[ResolvableMatchAlgorithmReference],
                                     resolvablePropertyReferences: Set[ResolvablePropertyReference[ScalaJSON]])
  extends AbstractRemovingDirectoryBuilder[ScalaJSON, ValueSetCatalogEntrySummary](
    allPossibleResults,
    matchAlgorithmReferences,
    resolvablePropertyReferences) {

  override
  def transformResults(rawResults: java.util.List[ScalaJSON]): java.util.List[ValueSetCatalogEntrySummary] = {
    rawResults.foldLeft(Seq[ValueSetCatalogEntrySummary]()) {
      (seq, row) => seq :+ rowToValueSet(row)
    }
  }

  private def rowToValueSet(jsonRow: ScalaJSON): ValueSetCatalogEntrySummary = {
    val oid = jsonRow.oid
    val name = jsonRow.name
    val valueSet = new ValueSetCatalogEntrySummary()
    valueSet.setAbout(oidToUri(oid))
    valueSet.setValueSetName(oid)
    valueSet.setResourceName(oid)
    valueSet.setFormalName(name)
    valueSet.setHref(urlConstructor.createValueSetUrl(oid))

    val description = new EntryDescription()
    description.setValue(ModelUtils.toTsAnyType(name));

    valueSet.setResourceSynopsis(description)

    valueSet
  }
}