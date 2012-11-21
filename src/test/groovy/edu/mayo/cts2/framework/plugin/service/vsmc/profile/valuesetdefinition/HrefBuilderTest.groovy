package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService

class HrefBuilderTest {

	@Test
	void TestContains() {
		def b = new HrefBuilder()
		assertTrue b.contains("SNOMED-CT", b.SNOMEDCT)
	}
	
	@Test
	void TestContainsNotFound() {
		def b = new HrefBuilder()
		assertFalse b.contains("__INVALID__", b.SNOMEDCT)
	}
	
	@Test
	void TestContainsUmls() {
		def b = new HrefBuilder()
		assertTrue b.contains("ICD10CM", b.UMLS_CODE_SYSTEMS)
	}

	@Test
	void TestContainsUmlsNotFound() {
		def b = new HrefBuilder()
		assertFalse b.contains("__INVALID__", b.UMLS_CODE_SYSTEMS)
	}
}
