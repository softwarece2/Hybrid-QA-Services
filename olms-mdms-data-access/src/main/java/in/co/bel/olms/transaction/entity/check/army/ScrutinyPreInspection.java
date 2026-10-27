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

import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.transaction.entity.OfferLetter;

/**
 * Checklist remarks for the scrutiny of the Pre Inspection
 * @author Prateek Dwivedi
 * @since 11-05-2024
 */
@Entity
@Table(name="scrutiny_pre_inspection", schema="tdms_check_army")
public class ScrutinyPreInspection implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_pre_inspection_id_seq")
	@SequenceGenerator(name = "scrutiny_pre_inspection_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_pre_inspection_id_seq", allocationSize = 1)
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
	@OneToMany(mappedBy = "scrutinyPreInspection")
	private List<ScrutinyPreInspectionDet> scrutinyPreInspectionDets;

	@Column(name = "qao_remarks")
	private String qaoRemarks;
	
	@OneToOne
	@JoinColumn(name = "so_status_id")
	private SoStatus soStatus;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "qao_submitted_date")
	private Date qaoSubmittedDate;
	

	@JsonIgnore
	@OneToMany(mappedBy = "scrutinyPreInspection")
	private List<ScrutinyPreInspectionAttachment> scrutinyPreInspectionAttachments;
	

	public List<ScrutinyPreInspectionDet> getScrutinyPreInspectionDets() {
		return scrutinyPreInspectionDets;
	}

	public void setScrutinyPreInspectionDets(List<ScrutinyPreInspectionDet> scrutinyPreInspectionDets) {
		this.scrutinyPreInspectionDets = scrutinyPreInspectionDets;
	}

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

	public String getQaoRemarks() {
		return qaoRemarks;
	}

	public void setQaoRemarks(String qaoRemarks) {
		this.qaoRemarks = qaoRemarks;
	}

	public SoStatus getSoStatus() {
		return soStatus;
	}

	public void setSoStatus(SoStatus soStatus) {
		this.soStatus = soStatus;
	}

	public Date getQaoSubmittedDate() {
		return qaoSubmittedDate;
	}

	public void setQaoSubmittedDate(Date qaoSubmittedDate) {
		this.qaoSubmittedDate = qaoSubmittedDate;
	}

	public List<ScrutinyPreInspectionAttachment> getScrutinyPreInspectionAttachments() {
		return scrutinyPreInspectionAttachments;
	}

	public void setScrutinyPreInspectionAttachments(
			List<ScrutinyPreInspectionAttachment> scrutinyPreInspectionAttachments) {
		this.scrutinyPreInspectionAttachments = scrutinyPreInspectionAttachments;
	}
	

}
