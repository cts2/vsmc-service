package edu.mayo.cts2.framework.plugin.service.vsmc.uri

import org.springframework.beans.factory.FactoryBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger

@Component
class UriResolverFactory extends FactoryBean[UriResolver] {

  val log:Logger = Logger.getLogger(classOf[UriResolverFactory])

  @scala.reflect.BeanProperty
  @Value("${uriResolutionServiceUrl}")
  var uriResolutionServiceUrl: String = _

  def getObject: UriResolver = {
    val uriResolver =
    if (StringUtils.isNotBlank(uriResolutionServiceUrl)){
      new RestUriResolver(uriResolutionServiceUrl)
    } else {
      new StaticUriResolver()
    }

    log.info("Using URI Resolver: " + uriResolver.toString)

    uriResolver
  }

  def getObjectType: Class[_] = classOf[UriResolver]

  def isSingleton: Boolean = true
}
