package edu.mayo.cts2.framework.plugin.service.vsmc.umls.dao

import java.util.Date

import org.springframework.beans.factory.annotation.Value

import gov.nih.nlm.umls.uts.webservice.UtsFault
import gov.nih.nlm.umls.uts.webservice.UtsWsContentControllerImplService
import gov.nih.nlm.umls.uts.webservice.UtsWsSecurityControllerImplService

class UtsDao {

  val EIGHT_HOURS = 1000 * 60 * 60 * 8;

  @scala.reflect.BeanProperty
  @Value("${utsUsername}")
  var username: String = _

  @scala.reflect.BeanProperty
  @Value("${utsPassword}")
  var password: String = _

  @scala.reflect.BeanProperty
  @Value("${utsUmlsRelease}")
  var umlsRelease: String = _

  @scala.reflect.BeanProperty
  @Value("${utsServiceName}")
  var serviceName: String = _

  private val securityService = (new UtsWsSecurityControllerImplService()).getUtsWsSecurityControllerImplPort();
  val utsContentService = (new UtsWsContentControllerImplService()).getUtsWsContentControllerImplPort();

  private var ticketGrantingTicket: String = _
  private var ticketCreationDate: Date = _

  private def getSecurityTicket(retry: Boolean = false): String = {

    val eightHoursAgo = new Date(new Date().getTime() - EIGHT_HOURS);

    if (retry || ticketCreationDate == null || ticketCreationDate.before(eightHoursAgo)) {
      ticketCreationDate = new Date()
      ticketGrantingTicket = securityService.getProxyGrantTicket(username, password);
    }

    try {
      securityService.getProxyTicket(ticketGrantingTicket, serviceName);
    } catch {
      case e: UtsFault => if (!retry) getSecurityTicket(true) else throw e
    }
  }

  def callSecurely[R](fn: (String, String) => R): R = {
    fn(getSecurityTicket(), umlsRelease)
  }
}
