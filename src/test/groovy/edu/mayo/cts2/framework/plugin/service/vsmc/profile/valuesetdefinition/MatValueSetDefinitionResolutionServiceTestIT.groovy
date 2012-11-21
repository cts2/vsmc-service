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

class MatValueSetDefinitionResolutionServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetDefinitionResolutionService service

	def marshaller = new DelegatingMarshaller()

	@Test
	void TestSetUp() {
		assertNotNull service
	}
	
	@Test
	void TestQuerySize() {
		def id = new ValueSetDefinitionReadId("1", ModelUtils.nameOrUriFromName("2.16.840.1.113883.1.11.1"))
		
		def result = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull result
		assertTrue result.entries.size() > 0
	}
	
	@Test
	void TestResolveSNOMEDCT() {
		def id = new ValueSetDefinitionReadId("1", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.02.734"))
		
		def result = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull result
		assertTrue result.entries.size() > 0
	}

	@Test
	void TestEntriesHaveUri() {
		def id = new ValueSetDefinitionReadId("1", ModelUtils.nameOrUriFromName("2.16.840.1.113883.1.11.1"))
		
		def result = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull result
		assertTrue result.entries.size() > 0
		
		result.entries.each {
			assertTrue ! it.uri.startsWith("null")
		}
	}
	
	@Test
	void TestQueryGrouping() {
		def id = new ValueSetDefinitionReadId("1", ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.526.03.695"))
		
		def result = service.resolveDefinition(id, null, null, null, null, null, new Page())
		
		assertNotNull result
		println result.entries.size()
		assertTrue result.entries.size() > 0
	}
	
	@Test
	void TestValidXml() {
		def id = new ValueSetDefinitionReadId("1", ModelUtils.nameOrUriFromName("2.16.840.1.113883.1.11.1"))
		
		def entries = service.resolveDefinition(id, null, null, null, null, null, new Page()).entries
		
		assertTrue entries.size() > 0
		
		entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}

}