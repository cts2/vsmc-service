package edu.mayo.cts2.framework.plugin.service.vsmc.security

import scala.collection.JavaConversions._
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.beans.factory.annotation.Value

class UtsSecurityProvider extends AbstractUserDetailsAuthenticationProvider {

  val TRUE_RESULT = "<Result>true</Result>"
  val FALSE_RESULT = "<Result>false</Result>"

  @scala.reflect.BeanProperty
  @Value("${utsLicenseCode}")
  var licenseCode: String = _

  @scala.reflect.BeanProperty
  @Value("${utsAuthServiceUrl}")
  var authService: String = _

  val restTemplate = new RestTemplate()

  def isValid(user: String, password: String): Boolean = {

    val map = new LinkedMultiValueMap[String, String]()
    map.add("licenseCode", licenseCode)
    map.add("user", user)
    map.add("password", password)

    val result = restTemplate.postForObject(authService, map, classOf[String])

    result match {
      case TRUE_RESULT => true
      case FALSE_RESULT => false
      case _ => throw new IllegalStateException("Illegal response from UTS AuthService: " + result)
    }
  }

  val AUTHORITIES = Seq(new SimpleGrantedAuthority("ROLE_USER"))

  @Override
  def additionalAuthenticationChecks(userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken) {}

  @Override
  def retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails = {
    val password = authentication.getCredentials.toString

    if (this.isValid(username, password)) {
      new User(username, password, AUTHORITIES);
    } else {
      throw new BadCredentialsException("")
    }
  }

}