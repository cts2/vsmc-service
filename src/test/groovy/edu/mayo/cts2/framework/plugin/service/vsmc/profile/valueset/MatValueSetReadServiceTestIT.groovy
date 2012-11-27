package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetReadService

class MatValueSetReadServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetReadService service
	
	def marshaller = new DelegatingMarshaller()
	
	@Test
	void TestSetUp() {
		assertNotNull service
	}	
	
	@Test
	void TestRead() {
		assertNotNull service.read(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1920"), null)
	}
	
	@Test
	void TestReadByOid() {
		//OID should be an alternate ID
		assertNotNull service.read(ModelUtils.nameOrUriFromUri("2.16.840.1.113883.3.600.1920"), null)
	}
	
	@Test
	void TestReadHasAlternateId() {
		def vs = service.read(ModelUtils.nameOrUriFromUri("urn:oid:2.16.840.1.113883.3.600.1920"), null)
		
		assertEquals 1, vs.alternateIDCount
		
		assertEquals "2.16.840.1.113883.3.600.1920", vs.alternateID[0]
	}

	@Test
	void TestReadByUri() {
		assertNotNull service.read(ModelUtils.nameOrUriFromUri("urn:oid:2.16.840.1.113883.3.600.1920"), null)
	}
	
	@Test
	void TestReadCorrectName() {
		def vs = service.read(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1920"), null)
		
		assertEquals "2.16.840.1.113883.3.600.1920", vs.getValueSetName()
	}
	
	@Test
	void TestReadCorrectAbout() {
		def vs = service.read(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1920"), null)
		
		assertEquals "urn:oid:2.16.840.1.113883.3.600.1920", vs.getAbout()
	}
	
}
