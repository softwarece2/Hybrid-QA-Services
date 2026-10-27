package in.co.bel.olms.transaction.entity.check.army;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.transaction.entity.OfferLetter;

/**
 * Checklist remarks for the scrutiny of the Three Tier
 * @author Prateek Dwivedi
 * @since 11-05-2024
 */
@Entity
@Table(name="scrutiny_three_tier", schema="tdms_check_army")
public class ScrutinyThreeTier implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_three_tier_id_seq")
	@SequenceGenerator(name = "scrutiny_three_tier_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_three_tier_id_seq", allocationSize = 1)
	@Column (name="id",unique=true,nullable=false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_datetime",nullable=false)
	private Date createdDatetime;
	
	@OneToOne
	@JoinColumn(name = "offer_letter_id", nullable = false)	
	private OfferLetter offerLetter;
	
	@Column(name="section_officer_id")
	private Integer sectionOfficerId;
	
	@Column(name="submitted_date")
	private Date submittedDate;
	
	@Column(name="modified_datetime")
	private Date modifiedDatetime;

	@Column(name="last_updated_by")
	private Integer lastUpdatedBy;

	@JsonIgnore
	@OneToMany(mappedBy = "scrutinyThreeTier")
	private List<ScrutinyThreeTierDet> scrutinyThreeTierDets;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}

	public Integer getSectionOfficerId() {
		return sectionOfficerId;
	}

	public void setSectionOfficerId(Integer sectionOfficerId) {
		this.sectionOfficerId = sectionOfficerId;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public List<ScrutinyThreeTierDet> getScrutinyThreeTierDets() {
		return scrutinyThreeTierDets;
	}

	public void setScrutinyThreeTierDets(List<ScrutinyThreeTierDet> scrutinyThreeTierDets) {
		this.scrutinyThreeTierDets = scrutinyThreeTierDets;
	}
	
	
}
