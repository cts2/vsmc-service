package edu.mayo.cts2.framework.plugin.service.vsmc.profile.valueset

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test
import org.springframework.transaction.annotation.Transactional

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedFilter
import edu.mayo.cts2.framework.model.core.PropertyReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.types.TargetReferenceType
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService

class MatValueSetQueryServiceTestIT extends AbstractTestITBase {

	@Resource
	def ValueSetQueryService service
	
	def marshaller = new DelegatingMarshaller()
	
	@Test
	void TestSetUp() {
		assertNotNull service
	}	
	
	@Test
	void TestQuerySize() {
		assertTrue service.getResourceSummaries(null as ValueSetQuery,null,null).entries.size() > 10
	}
	
	@Test
	void TestMaxToReturn() {
		def summaries = service.getResourceSummaries(null as ValueSetQuery,null,new Page(maxToReturn:5,page:0))
		
		assertEquals 5, summaries.entries.size()
	}
	
	@Test
	void TestIsPartialFalse() {
		def summaries = service.getResourceSummaries(null as ValueSetQuery,null,new Page(maxToReturn:5,page:0))
		
		assertTrue summaries.entries.size() < 50
		
		assertFalse summaries.atEnd
	}
	
	@Test
	void TestQueryContainsFilter() {
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter = new ResolvedFilter(
						matchValue:"02.99",
						propertyReference: StandardModelAttributeReference.RESOURCE_NAME.propertyReference,
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	void TestQueryContainsFilterSynopsis() {
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter = new ResolvedFilter(
						matchValue:"office",
						propertyReference: StandardModelAttributeReference.RESOURCE_SYNOPSIS.propertyReference,
						matchAlgorithmReference: StandardMatchAlgorithmReference.CONTAINS.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	void TestIsPartialTrue() {
		def summaries = service.getResourceSummaries(null as ValueSetQuery,null,new Page(maxToReturn:50,page:0))
		
		assertTrue summaries.entries.size() < 50
		
		assertTrue summaries.atEnd
	}
	
	@Test
	void TestQueryContainsPropertyFilterEmeasure() {
		
		def ref = new PropertyReference()
		ref.referenceTarget = new URIAndEntityName(uri:"some uri", name:"emeasureid")
		ref.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter = new ResolvedFilter(
						matchValue:"172",
						propertyReference: ref,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	void TestQueryContainsPropertyFilterEmeasureWithDuplicateInGroup() {
		
		def ref = new PropertyReference()
		ref.referenceTarget = new URIAndEntityName(uri:"some uri", name:"emeasureid")
		ref.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter = new ResolvedFilter(
						matchValue:"171",
						propertyReference: ref,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	void TestQueryTwoFilters() {
		
		def ref1 = new PropertyReference()
		ref1.referenceTarget = new URIAndEntityName(uri:"some uri", name:"emeasureid")
		ref1.referenceType = TargetReferenceType.PROPERTY
		
		def ref2 = new PropertyReference()
		ref2.referenceTarget = new URIAndEntityName(uri:"some uri", name:"nqfnumber")
		ref2.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter1 = new ResolvedFilter(
						matchValue:"172",
						propertyReference: ref1,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					def filter2 = new ResolvedFilter(
						matchValue:"0453",
						propertyReference: ref2,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter1,filter2] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	void TestQueryTwoFiltersOneInvalid() {
		
		def ref1 = new PropertyReference()
		ref1.referenceTarget = new URIAndEntityName(uri:"some uri", name:"emeasureid")
		ref1.referenceType = TargetReferenceType.PROPERTY
		
		def ref2 = new PropertyReference()
		ref2.referenceTarget = new URIAndEntityName(uri:"some uri", name:"nqfnumber")
		ref2.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter1 = new ResolvedFilter(
						matchValue:"172",
						propertyReference: ref1,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					def filter2 = new ResolvedFilter(
						matchValue:"__INVALID__",
						propertyReference: ref2,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter1,filter2] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 0, summaries.entries.size()
	}
	
	@Test
	@Transactional
	void TestQueryContainsPropertyFilter() {
		
		def ref = new PropertyReference()
		ref.referenceTarget = new URIAndEntityName(uri:"some uri", name:"nqfnumber")
		ref.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
			
					def filter = new ResolvedFilter(
						matchValue:"0453",
						propertyReference: ref,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 1, summaries.entries.size()
	}
	
	@Test
	@Transactional
	void TestQueryContainsPropertyFilterInvalid() {
		
		def ref = new PropertyReference()
		ref.referenceTarget = new URIAndEntityName(uri:"uri", name:"nqfnumber")
		ref.referenceType = TargetReferenceType.PROPERTY
		
		def summaries = service.getResourceSummaries(
			{
				getFilterComponents : {
					def filter = new ResolvedFilter(
						matchValue:"__INVALID__",
						propertyReference: ref,
						matchAlgorithmReference: StandardMatchAlgorithmReference.EXACT_MATCH.matchAlgorithmReference
					)
					[filter] as Set
				}
			} as ValueSetQuery,null,null)
		
		assertEquals 0, summaries.entries.size()
	}
	
	@Test
	void TestValidXml() {
		def entries = service.getResourceSummaries(null as ValueSetQuery,null,null).entries
		
		assertTrue entries.size() > 0
		
		entries.each {
			marshaller.marshal(it, new StreamResult(new StringWriter()))
		}
	}

}
