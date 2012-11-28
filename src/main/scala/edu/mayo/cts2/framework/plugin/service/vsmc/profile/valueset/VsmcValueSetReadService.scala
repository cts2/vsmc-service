package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import java.lang.Override
import scala.collection.JavaConversions._
import org.apache.commons.lang.StringUtils
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.Property
import edu.mayo.cts2.framework.model.core.SourceAndRoleReference
import edu.mayo.cts2.framework.model.core.ValueSetDefinitionReference
import edu.mayo.cts2.framework.model.service.core.NameOrURI
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.uri.UriUtils
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.core.PredicateReference
import edu.mayo.cts2.framework.model.core.StatementTarget
import edu.mayo.cts2.framework.model.util.ModelUtils

@Component
class VsmcValueSetReadService extends AbstractService with ValueSetReadService {

  @Override
  def read(
    identifier: NameOrURI,
    readContext: ResolvedReadContext): ValueSetCatalogEntry = {

    null
  }

  def createProperty(name:String,value:String) = {
    val prop = new Property()
    
     val predicate = new PredicateReference()
     predicate.setName(name)
     predicate.setNamespace(UriUtils.SVS_NS)
     predicate.setUri(UriUtils.toSvsUri(name))
     prop.setPredicate(predicate)
     
     val target = new StatementTarget()
     target.setLiteral( ModelUtils.createOpaqueData(value) )   
     prop.addValue(target)
     
     prop
  }

  @Override
  def exists(identifier: NameOrURI, readContext: ResolvedReadContext): Boolean = {
    throw new UnsupportedOperationException()
  }

}