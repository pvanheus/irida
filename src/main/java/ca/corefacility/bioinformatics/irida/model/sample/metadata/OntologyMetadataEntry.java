package ca.corefacility.bioinformatics.irida.model.sample.metadata;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Audited
@Table(name = "ontology_metadata_entry")
public class OntologyMetadataEntry extends MetadataEntry {
	String ontologyUri;

	private OntologyMetadataEntry() {
	}

	public OntologyMetadataEntry(String value, String type, String ontologyUri) {
		super(value, type);
		this.ontologyUri = ontologyUri;
	}

	public String getOntologyUri() {
		return ontologyUri;
	}

	public void setOntologyUri(String ontologyUri) {
		this.ontologyUri = ontologyUri;
	}
}
