package ca.corefacility.bioinformatics.irida.model.sample.metadata;

import java.util.Objects;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import ca.corefacility.bioinformatics.irida.model.sample.Sample;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Class for storing generic metadata for a {@link Sample}
 */
@Entity
@Audited
@Table(name = "metadata_entry")
@EntityListeners(AuditingEntityListener.class)
public class MetadataEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Lob
	private String value;

	@NotNull
	private String type;

	public MetadataEntry() {
	}

	public MetadataEntry(String value, String type) {
		this.value = value;
		this.type = type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * Merges the passed metadata entry into this metadata entry.
	 * 
	 * @param metadataEntry The new metadata entry.
	 */
	public void merge(MetadataEntry metadataEntry) {
		checkNotNull(metadataEntry, "metadataEntry is null");
		checkArgument(this.getClass().equals(metadataEntry.getClass()),
				"Cannot merge " + metadataEntry + " into " + this);

		this.type = metadataEntry.getType();
		this.value = metadataEntry.getValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(value, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MetadataEntry) {
			MetadataEntry entry = (MetadataEntry) obj;
			return Objects.equals(entry.getValue(), value) && Objects.equals(entry.getType(), type);
		}
		return false;
	}
}
