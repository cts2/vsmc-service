package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionResolutionService
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.name.ValueSetDefinitionReadId

class VsmcValueSetDefinitionResolutionServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetDefinitionResolutionService service

	def marshaller = new DelegatingMarshaller()

	@Test
	void TestSetUp() {
		assertNotNull service
	}
	
	@Test
	void TestQuerySize() {
		def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))
		
		def result = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull result
		assertTrue result.entries.size() > 0
	}

    @Test
    void TestQueryAtEnd() {
        def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.464.1003.113.12.1033"))

        def result = service.resolveDefinition(id, null, null, null, null, null, new Page(page: 1, maxToReturn:100))

        assertNotNull result
        assertTrue result.entries.size() > 0
        assertTrue result.entries.size() < 1000

        assertTrue result.atEnd
    }

    @Test
    void TestQueryCorrectSize() {
        def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.464.1003.113.12.1033"))

        def result = service.resolveDefinition(id, null, null, null, null, null, new Page())

        assertNotNull result
        assertEquals result.entries.size(), 50
        assertFalse result.atEnd
    }

	@Test
	void TestValidXml() {
		def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))
		
		def entries = service.resolveDefinition(id, null, null, null, null, null, new Page()).entries
		
		assertTrue entries.size() > 0
		
		entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}
	
	@Test
	void TestHeaderNotNull() {
		def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))
		
		def entries = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull entries.resolvedValueSetHeader
	}
	
	@Test
	void TestHeaderOfGrouping() {
		def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1525"))
		
		def entries = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertEquals 5, entries.resolvedValueSetHeader.resolvedUsingCodeSystem.size()
	}

	@Test
	void TestHeaderValidXml() {
		def id = new ValueSetDefinitionReadId("20121025", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))
		
		def entries = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		marshaller.marshal(entries.resolvedValueSetHeader, new StreamResult(new StringWriter()))
	}


}