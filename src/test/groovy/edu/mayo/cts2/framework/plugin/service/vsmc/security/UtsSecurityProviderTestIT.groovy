package edu.mayo.cts2.framework.plugin.service.vsmc.security
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import org.junit.Test
import org.springframework.beans.factory.annotation.Value

import javax.annotation.Resource

import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

class UtsSecurityProviderTestIT extends AbstractTestITBase {

    @Resource
    def UtsSecurityProvider securityProvider

    @Value('${utsUsername}')
    def utsUsername;

    @Value('${utsPassword}')
    def utsPassword;

    @Test
    void TestSecurityBad() {
        assertFalse securityProvider.isValid("not", "valid")
    }

    @Test
    void TestSecurityGood() {
        assertTrue securityProvider.isValid(utsUsername, utsPassword)
    }

}
