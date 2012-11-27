package edu.mayo.cts2.framework.plugin.service.vsmc.vsac.dao

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test

import edu.mayo.cts2.framework.plugin.service.vsmc.test.AbstractTestITBase


class VsacRestDaoTestIT extends AbstractTestITBase {

	@Resource
	def VsacRestDao dao

	@Test
	void TestSetUp() {
		assertNotNull dao
	}

	@Test
	void TestGetValueSets() {
		def vs = dao.getAllValueSets()
		
		assertTrue 10 < vs.size()
	}
	
	@Test
	void TestGetMembersOfValueSet() {
		def result = dao.getMembersOfValueSet("2.16.840.1.113883.3.600.1.1519", "20121025", 10, 1)

		assertNotNull result
		assertTrue result.contains '"rowCount":5'
	}
}