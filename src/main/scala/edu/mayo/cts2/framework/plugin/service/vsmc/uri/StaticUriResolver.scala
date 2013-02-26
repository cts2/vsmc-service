package edu.mayo.cts2.framework.plugin.service.vsmc.uri

class StaticUriResolver extends UriResolver {

  def codeSystems = Map(
  "SNOMEDCT" -> ("http://snomed.info", "http://snomed.info/id/"),
  "ICD9CM" -> ("http://id.nlm.nih.gov/sab/ICD9CM", "http://id.nlm.nih.gov/sab/ICD9CM/"),
  "ICD10CM" -> ("http://id.nlm.nih.gov/sab/ICD10CM", "http://id.nlm.nih.gov/sab/ICD10CM/"),
  "AdministrativeSex" -> ("urn:oid:2.16.840.1.114222.4.11.927", "http://id.hl7.org/codesystem/AdministrativeSex/"),
  "SCTUSX" -> ("http://id.nlm.nih.gov/sab/SCTUSX", "http://id.nlm.nih.gov/sab/SCTUSX/"),
  "LOINC" -> ("http://id.nlm.nih.gov/sab/LNC", "http://id.nlm.nih.gov/sab/LNC/"),
  "ICD10PCS" -> ("http://id.nlm.nih.gov/sab/ICD10PCS", "http://id.nlm.nih.gov/sab/ICD10PCS/"),
  "RXNORM" -> ("http://id.nlm.nih.gov/sab/RXNORM", "http://id.nlm.nih.gov/sab/RXNORM/"),
  "DischargeDisposition" -> ("urn:oid:2.16.840.1.114222.4.11.915", "http://id.hl7.org/codesystem/DischargeDisposition/"),
  "CPT" -> ("http://id.nlm.nih.gov/sab/CPT", "http://id.nlm.nih.gov/sab/CPT/"),
  "HCPCS" -> ("http://id.nlm.nih.gov/sab/HCPCS", "http://id.nlm.nih.gov/sab/HCPCS/"),
  "CVX" -> ("urn:oid:2.16.840.1.113883.6.59", "http://id.hl7.org/codesystem/CVX/"),
  "CDT" -> ("urn:oid:2.16.840.1.113883.6.13", "http://id.hl7.org/codesystem/CDT/"),
  "HSLOC" -> ("urn:oid:2.16.840.1.113883.6.259", "http://id.hl7.org/codesystem/HSLOC/"),
  "SOP" -> ("urn:oid:2.16.840.1.113883.221.5", "http://id.hl7.org/codesystem/SOP/"),
  "CDCREC" -> ("urn:oid:2.16.840.1.113883.6.238", "http://id.hl7.org/codesystem/RaceAndEthnicity_CDC/"))

  def UMLS_URI(sab:String) = "http://id.nlm.nih.gov/sab/" + sab
  def UMLS_BASE_URI(sab:String) = UMLS_URI(sab) + "/"

  def idToUri(id: String, idType: IdType.Value): String = {
     codeSystems.getOrElse(id, (UMLS_URI(id), UMLS_BASE_URI(id)))._1
  }

  def idToName(id: String, idType: IdType.Value): String = {
    id
  }

  def idToBaseUri(id: String): String = {
    codeSystems.getOrElse(id, (UMLS_URI(id), UMLS_BASE_URI(id)))._2
  }

}
