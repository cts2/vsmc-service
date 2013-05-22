package edu.mayo.cts2.framework.plugin.service.vsmc.profile.entity

import java.util.ArrayList
import scala.collection.JavaConversions._
import org.springframework.stereotype.Component
import edu.mayo.cts2.framework.model.command.Page
import edu.mayo.cts2.framework.model.command.ResolvedReadContext
import edu.mayo.cts2.framework.model.core.CodeSystemReference
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference
import edu.mayo.cts2.framework.model.core.EntityReference
import edu.mayo.cts2.framework.model.core.NameAndMeaningReference
import edu.mayo.cts2.framework.model.core.ScopedEntityName
import edu.mayo.cts2.framework.model.core.SortCriteria
import edu.mayo.cts2.framework.model.core.TsAnyType
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.model.core.VersionTagReference
import edu.mayo.cts2.framework.model.directory.DirectoryResult
import edu.mayo.cts2.framework.model.entity.Designation
import edu.mayo.cts2.framework.model.entity.EntityDescription
import edu.mayo.cts2.framework.model.entity.EntityList
import edu.mayo.cts2.framework.model.entity.EntityListEntry
import edu.mayo.cts2.framework.model.entity.NamedEntityDescription
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI
import edu.mayo.cts2.framework.plugin.service.vsmc.profile.AbstractService
import edu.mayo.cts2.framework.plugin.service.vsmc.umls.dao.UtsDao
import edu.mayo.cts2.framework.service.profile.entitydescription.EntityDescriptionReadService
import edu.mayo.cts2.framework.service.profile.entitydescription.name.EntityDescriptionReadId
import gov.nih.nlm.umls.uts.webservice.AtomDTO
import gov.nih.nlm.umls.uts.webservice.Psf
import javax.annotation.Resource
import edu.mayo.cts2.framework.model.entity.types.DesignationRole
import org.apache.commons.lang.StringUtils

@Component
class UtsEntityReadService extends AbstractService
with EntityDescriptionReadService {

  @Resource
  var utsDao: UtsDao = _

  @Override
  def readEntityDescriptions(p1: EntityNameOrURI, p2: SortCriteria, p3: ResolvedReadContext, p4: Page): DirectoryResult[EntityListEntry] = throw new RuntimeException()

  @Override
  def availableDescriptions(p1: EntityNameOrURI, p2: ResolvedReadContext): EntityReference = throw new UnsupportedOperationException()

  @Override
  def readEntityDescriptions(p1: EntityNameOrURI, p2: ResolvedReadContext): java.util.List[EntityListEntry] = throw new UnsupportedOperationException()

  @Override
  def getKnownCodeSystems: java.util.List[CodeSystemReference] = throw new UnsupportedOperationException()

  @Override
  def getKnownCodeSystemVersions: java.util.List[CodeSystemVersionReference] = throw new UnsupportedOperationException()

  @Override
  def read(id: EntityDescriptionReadId, context: ResolvedReadContext = null): EntityDescription = {

    val csv = id.getCodeSystemVersion.getName
    val fn = utsDao.utsContentService.getCodeAtoms _

    val atoms = utsDao.callSecurely(fn(_, _, id.getEntityName.getName, csvNameToSab(csv), new Psf()))

    if (atoms.size == 0) {
      return null;
    }

    val namedEntity = atoms.foldLeft(atomToNamedEntityDescription(atoms.get(0), csv))(
      (entity, atom) => {
        entity
      })

    val ed = new EntityDescription()
    ed.setNamedEntity(namedEntity)

    ed
  }

  private def csvNameToSab(csvName: String) = {
    val sab = StringUtils.substringBefore(csvName, "-")

    if (sab.equals("LOINC")) {
      "LNC"
    } else {
      sab
    }
  }

  private def atomToNamedEntityDescription(atom: AtomDTO, csv: String): NamedEntityDescription = {
    val ed = new NamedEntityDescription()
    val csName = atom.getRootSource

    val name = new ScopedEntityName()
    val code = atom.getCode().getSourceUi()
    name.setName(code)
    name.setNamespace(atom.getRootSource)
    ed.setEntityID(name)

    val baseUri = uriResolver.idToBaseUri(csName)
    ed.setAbout(baseUri + code)
    setDesignation(ed, atom, true)

    val csvRef = new CodeSystemVersionReference();
    val csRef = new CodeSystemReference(csName);
    val nameMeaningRef = new NameAndMeaningReference(csv);

    csvRef.setCodeSystem(csRef)
    csvRef.setVersion(nameMeaningRef)

    ed.setDescribingCodeSystemVersion(csvRef)

    val entityType = new URIAndEntityName()
    entityType.setName("Class")
    entityType.setNamespace("owl")
    entityType.setUri("http://www.w3.org/2002/07/owl#Class")

    ed.addEntityType(entityType)

    ed
  }

  def setDesignation(ed: NamedEntityDescription, atom: AtomDTO, preferred: Boolean = false) = {
    val designation = new Designation()
    designation.setDesignationRole(if (preferred) DesignationRole.PREFERRED else DesignationRole.ALTERNATIVE)
    val anyType = new TsAnyType()
    anyType.setContent(atom.getTermString().getDefaultPreferredName())
    designation.setValue(anyType)
    val designList = new ArrayList[Designation]()
    designList.add(designation)
    ed.setDesignation(designList)
  }

  def getConceptSemanticTypes(atom: AtomDTO): List[java.lang.String] = {
    atom.getConcept().getSemanticTypes().toList
  }

  def getConceptStatus(atom: AtomDTO): String = {
    atom.getConcept().getStatus()
  }

  def getConceptCUI(atom: AtomDTO): String = {
    atom.getConcept().getUi()
  }

  def isConceptObsolete(atom: AtomDTO): Boolean = {
    false
  }

  def getSourceConceptDefaultPreferredName(atom: AtomDTO): String = {
    atom.getSourceConcept().getDefaultPreferredName()
  }

  def getTermStringTermLanguage(atom: AtomDTO): String = {
    atom.getTermString().getTerm().getLanguage()
  }

  def getTermStringTermLuiNormName(atom: AtomDTO): String = {
    atom.getTermString().getTerm().getLuinormForm()
  }

  def getTermStringTermLUI(atom: AtomDTO): String = {
    atom.getTermString().getTerm().getUi()
  }

  def getTermStringTermTyep(atom: AtomDTO): String = {
    null
  }

  def getAtomUi(atom: AtomDTO): String = {
    atom.getUi()
  }

  def exists(p1: EntityDescriptionReadId, p2: ResolvedReadContext): Boolean = throw new RuntimeException()

  def getSupportedVersionTags: java.util.List[VersionTagReference] =
    List[VersionTagReference](CURRENT_TAG)
}