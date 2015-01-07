package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId
import org.junit.Ignore
import org.junit.Test

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import static org.junit.Assert.*

class VsmcValueSetDefinitionReadServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetDefinitionReadService service

	def marshaller = new DelegatingMarshaller()

	@Test
	void TestSetUp() {
		assertNotNull service
	}
	
	@Test
	void TestRead() {
		def id = new ValueSetDefinitionReadId("20130614", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"))
		
		def result = service.read(id, null)
		
		assertNotNull result
	}

    @Test
    @Ignore
    void TestReadNotFound() {
        def id = new ValueSetDefinitionReadId("__INVALID__", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"))

        def result = service.read(id, null)

        assertNull result
    }
	
	@Test
	void TestReadByTag() {
		def ref = new VersionTagReference("CURRENT")
		def result = service.readByTag(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"), ref, null)
		
		assertNotNull result
	}
	
	@Test
	void TestReadGroupValueSet() {
		def id = new ValueSetDefinitionReadId("20140501", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1525"))
		
		def result = service.read(id, null)
		
		assertNotNull result
		
		def valuesets = []
		
		result.getResource().entry.each {
			println it
			if(it.completeValueSet != null){
				valuesets.add( it.completeValueSet )
			}
		}
		
		assertEquals 5, valuesets.size()
	}
	
	@Test
	void TestHasSourceAndNotation() {
		def id = new ValueSetDefinitionReadId("20130614", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"))
		
		def result = service.read(id, null).getResource()
		
		assertNotNull result.sourceAndNotation
	}
	
	@Test
	void TestHasEntries() {
		def id = new ValueSetDefinitionReadId("20130614", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"))
		
		def result = service.read(id, null).getResource()
		
		assertTrue result.entryCount > 0
	}
	
	@Test
	void TestReadValidXml() {
		def id = new ValueSetDefinitionReadId("20130614", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.2.39"))
		
		def result = service.read(id, null)
		
		assertNotNull result.getResource()
		
		marshaller.marshal(result.getResource(), new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestReadValidXmlGroup() {
		def id = new ValueSetDefinitionReadId("20130614", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1525"))
		
		def result = service.read(id, null)
		
		assertNotNull result.getResource()
		
		marshaller.marshal(result.getResource(), new StreamResult(new StringWriter()))
	}

	
}