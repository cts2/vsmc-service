package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valuesetdefinition
import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQuery
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService
import org.junit.Test

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import static org.junit.Assert.*

class VsmcValueSetDefinitionQueryServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetDefinitionQueryService service

	def marshaller = new DelegatingMarshaller()
	
	@Test
	void TestSetUp() {
		assertNotNull service
	}
	
	@Test
	void TestQueryDefinitionsOfValueSet() {
		def summaries = service.getResourceSummaries(
			{
				getRestrictions : {
					def restrictions = new ValueSetDefinitionQueryServiceRestrictions()
					restrictions.setValueSet(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))
				
					restrictions
				}
			} as ValueSetDefinitionQuery,null,null)
		
		assertEquals 3, summaries.entries.size()
	}

    @Test
    void TestValidXml() {
        def summaries = service.getResourceSummaries(
                {
                    getRestrictions : {
                        def restrictions = new ValueSetDefinitionQueryServiceRestrictions()
                        restrictions.setValueSet(ModelUtils.nameOrUriFromName("2.16.840.1.113883.3.600.1.1519"))

                        restrictions
                    }
                } as ValueSetDefinitionQuery,null,null)

        assertTrue summaries.entries.size() > 0

        summaries.entries.each {
            marshaller.marshal(it, new StreamResult(new StringWriter()))
        }
    }

}
