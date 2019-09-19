package ca.corefacility.bioinformatics.irida.model.sample;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Audited
public class OntologyMetadataField extends MetadataTemplateField {
	@NotNull
	@Column(name = "ontology")
	private String ontology;

	public OntologyMetadataField() {
		super();
	}

	public OntologyMetadataField(String label, String type, String ontology) {
		super(label, type);
		this.ontology = ontology;
	}

	public String getOntology() {
		return ontology;
	}

	public void setOntology(String ontology) {
		this.ontology = ontology;
	}
}
