package edu.mayo.cts2.framework.plugin.service.vsmc.profile.entity

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Ignore
import org.junit.Test

import edu.mayo.cts2.framework.core.xml.DelegatingMarshaller
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.util.ModelUtils
import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId
import gov.nih.nlm.umls.uts.webservice.AtomDTO
import gov.nih.nlm.umls.uts.webservice.ConceptDTO
import gov.nih.nlm.umls.uts.webservice.SourceAtomClusterDTO
import gov.nih.nlm.umls.uts.webservice.TermDTO
import gov.nih.nlm.umls.uts.webservice.TermStringDTO

class UtsEntityReadServiceTestIT extends AbstractTestITBase {

	@Resource
	def UtsEntityReadService service

	def marshaller = new DelegatingMarshaller()
	
	
	void setUp(){
		def concept = new ConceptDTO()
		concept.semanticTypes.add("T056")
		concept.setStatus("R")
		concept.setUi("C12345")
		def sourceConcept = new SourceAtomClusterDTO()
		sourceConcept.setDefaultPreferredName("Name")
		def termString = new TermStringDTO()
		def term = new TermDTO()
		term.setLanguage("ENG")
		term.setLuinormForm("Normal name")
		term.setUi("L6464324")
		this.atom.setUi("4646489")
		termString.setTerm(term)
		this.atom.setConcept(concept)
		this.atom.setSourceConcept(sourceConcept)
	}
	
	@Test
	void TestSetUp() {

		
		assertNotNull service
	}
	
	@Test
	void TestRead() {
		def id = new EntityDescriptionReadId("185465003", "SNOMEDCT", ModelUtils.nameOrUriFromName("SNOMEDCT"))
		
		def result = service.read(id, null)
		
		assertNotNull result
	}
	
	@Test
	void TestReadRxNorm() {
		def id = new EntityDescriptionReadId("205166", "RXNORM", ModelUtils.nameOrUriFromName("RXNORM"))
		
		def result = service.read(id, null)
		
		assertNotNull result
	}
	
	@Test
	void TestReadICD9CM() {
		def id = new EntityDescriptionReadId("M15.0", "ICD10CM", ModelUtils.nameOrUriFromName("ICD10CM-2010"))
		
		def result = service.read(id, null)
		
		assertNotNull result
	}
	
	@Test
	void TestReadValidXml() {
		def id = new EntityDescriptionReadId("99201", "CPT", ModelUtils.nameOrUriFromName("CPT"))
		
		def result = service.read(id, null)
		
		assertNotNull result
		
		marshaller.marshal(result, new StreamResult(new StringWriter()))
	}
	
	@Test
	void TestReadHasDescriptions() {
		def id = new EntityDescriptionReadId("185465003", "SNOMEDCT", ModelUtils.nameOrUriFromName("SNOMEDCT"))
		
		def result = service.read(id, null).namedEntity
		
		assertTrue result.designationCount > 0
	}
	
	@Test
	def void TestGetConceptSemanticTypes(){
		def atom = new AtomDTO()
		def concept = new ConceptDTO()
		concept.semanticTypes.add("T056")
		atom.setConcept(concept)
		def vals = service.getConceptSemanticTypes(atom)
		assertTrue(vals.size() > 0)
	}
	
	@Test 
	void TestSetDesignation() {
		def ed = new NamedEntityDescription()
		def atom = new AtomDTO()
		def term = new TermStringDTO()
		term.setDefaultPreferredName("Test Designation")
		atom.setTermString(term)
		service.setDesignation(ed, atom, true)
		assertEquals(ed.getDesignation(0).getValue().getContent(), "Test Designation")
	}
	


	@Test
	void TestGetConceptStatus(){
		def atom = new AtomDTO()
		def concept = new ConceptDTO()
		concept.setStatus("R")
		atom.setConcept(concept)
		def status = service.getConceptStatus(atom)
	    assertEquals(status, "R")
	}

	void TestGetConceptCUI(){
		def atom = new AtomDTO()
		def concept = new ConceptDTO()
		concept.setUi("C12345")
		atom.setConcept(concept)
		assertTrue(service.getConceptCUI(atom), "C12345")
	}

	void TestIsConceptObsolete(){
		//TODO
		fail()
	}

	@Test
	void TestGetSourceConceptDefaultPreferredName(){
		def atom = new AtomDTO()
		def sourceConcept = new SourceAtomClusterDTO()
		sourceConcept.setDefaultPreferredName("Name")
		atom.setSourceConcept(sourceConcept)
		assertEquals(service.getSourceConceptDefaultPreferredName(atom), "Name")
	}

	@Test
	void TestGetTermStringTermLanguage(){
		def atom = new AtomDTO()
		def termString = new TermStringDTO()
		def term = new TermDTO()
		term.setLanguage("ENG")
		termString.setTerm(term)
		atom.setTermString(termString)
        assertEquals(service.getTermStringTermLanguage(atom), "ENG")
	}

	@Test
	void TestGetTermStringTermLuiNormName(){
		def atom = new AtomDTO()
		def termString = new TermStringDTO()
		def term = new TermDTO()
		term.setLuinormForm("Normal name")
		termString.setTerm(term)
		atom.setTermString(termString)
		assertEquals(service.getTermStringTermLuiNormName(atom), "Normal name")
	}

	@Test
	void TestGetTermStringTermLUI(){
		def atom = new AtomDTO()
		def termString = new TermStringDTO()
		def term = new TermDTO()
		term.setUi("L6464324")
		termString.setTerm(term)
		atom.setTermString(termString)
		assertEquals(service.getTermStringTermLUI(atom), "L6464324")
	}

	@Test
	@Ignore
	void TestSetTermStringTermType(){
		def atom = new AtomDTO()
		def termString = new TermStringDTO()
		def term = new TermDTO()
		
		termString.setTerm(term)
		atom.setTermString(termString)
	    //TODO
		fail()
	}
	
	@Test
	void TestGetAtomUI() {
		def atom = new AtomDTO()
		atom.setUi("12456")
		assertEquals(service.getAtomUi(atom), "12456")
	}

}
