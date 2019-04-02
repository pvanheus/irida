package ca.corefacility.bioinformatics.irida.model.run;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.corefacility.bioinformatics.irida.model.IridaResourceSupport;
import ca.corefacility.bioinformatics.irida.model.MutableIridaThing;
import ca.corefacility.bioinformatics.irida.model.enums.SequencingRunUploadStatus;
import ca.corefacility.bioinformatics.irida.model.sequenceFile.SequencingObject;
import ca.corefacility.bioinformatics.irida.model.user.User;

/**
 * This class represents a collection of sequence files that have come off one run of a sequencer.
 */
@Entity
@Table(name = "sequencing_run")
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public class SequencingRun extends IridaResourceSupport implements MutableIridaThing, Comparable<SequencingRun> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Lob
	private String description;

	@CreatedDate
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private final Date createdDate;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH }, mappedBy = "sequencingRun")
	private Set<SequencingObject> sequencingObject;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "upload_status")
	private SequencingRunUploadStatus uploadStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "layout_type")
	private LayoutType layoutType;

	// Key/value map of additional properties you could set on a sequence file.
	// This may contain optional sequencer specific properties.
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "property_key", nullable = false)
	@Column(name = "property_value", nullable = false)
	@CollectionTable(name = "sequencing_run_properties", joinColumns = @JoinColumn(name = "sequencing_run_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
			"sequencing_run_id", "property_key" }, name = "UK_SEQUENCING_RUN_PROPERTY_KEY"))
	private Map<String, String> optionalProperties;

	@Column(name = "sequencer_type")
	private String sequencerType;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(name = "user_id")
	private User user;

	protected SequencingRun() {
		createdDate = new Date();
		optionalProperties = new HashMap<>();
	}

	public SequencingRun(final LayoutType layoutType, final SequencingRunUploadStatus uploadStatus) {
		this();
		this.layoutType = layoutType;
		this.uploadStatus = uploadStatus;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Date getCreatedDate() {
		return createdDate;
	}

	@Override
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@Override
	public void setModifiedDate(final Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLabel() {
		return "SequencingRun " + createdDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdDate, description);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SequencingRun)) {
			return false;
		}
		final SequencingRun other = (SequencingRun) obj;
		return Objects.equals(this.description, other.description) && Objects.equals(this.createdDate,
				other.createdDate);
	}

	public SequencingRunUploadStatus getUploadStatus() {
		return uploadStatus;
	}

	public LayoutType getLayoutType() {
		return layoutType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Add one optional property to the map of properties
	 *
	 * @param key   The key of the property to add
	 * @param value The value of the property to add
	 */
	@JsonAnySetter
	public void addOptionalProperty(String key, String value) {
		optionalProperties.put(key, value);
	}

	/**
	 * Get the Map of optional properties
	 *
	 * @return A {@code Map<String,String>} of all the optional propertie
	 */
	@JsonAnyGetter
	public Map<String, String> getOptionalProperties() {
		return optionalProperties;
	}

	/**
	 * Get an individual optional property
	 *
	 * @param key The key of the property to read
	 * @return A String of the property's value
	 */
	public String getOptionalProperty(String key) {
		return optionalProperties.get(key);
	}

	/**
	 * Get the sequencer type
	 *
	 * @return Name of the sequencer type
	 */
	public String getSequencerType() {
		return sequencerType;
	}

	public void setSequencerType(String sequencerType) {
		this.sequencerType = sequencerType;
	}

	/**
	 * The type of layout for the run. Single/Paired end
	 */
	public static enum LayoutType {
		SINGLE_END,
		PAIRED_END
	}

	@Override
	public int compareTo(SequencingRun sequencingRun) {
		return 0;
	}
}
