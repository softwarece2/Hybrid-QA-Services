package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "internal_inote_approval_details", schema = "tdms")
@NoArgsConstructor

public class InternalInoteApprovalDetails implements Serializable{

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Employee getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}

	public Employee getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Employee modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Employee getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(Employee approvedBy) {
		this.approvedBy = approvedBy;
	}

	
	

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Employee getNextApprover() {
		return nextApprover;
	}

	public void setNextApprover(Employee nextApprover) {
		this.nextApprover = nextApprover;
	}

	public Boolean getIsRfr() {
		return isRfr;
	}

	public void setIsRfr(Boolean isRfr) {
		this.isRfr = isRfr;
	}

	public InternalINote getInternalInote() {
		return internalInote;
	}

	public void setInternalInote(InternalINote internalInote) {
		this.internalInote = internalInote;
	}

	

	public List<InternalOlAprovlDetAttachment> getInternalOlAprovlDetAttachments() {
		return internalOlAprovlDetAttachments;
	}

	public void setInternalOlAprovlDetAttachments(List<InternalOlAprovlDetAttachment> internalOlAprovlDetAttachments) {
		this.internalOlAprovlDetAttachments = internalOlAprovlDetAttachments;
	}


	public Integer getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Integer approvalStatus) {
		this.approvalStatus = approvalStatus;
	}


	private static final long serialVersionUID = 350290179399100063L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "internal_inote_approval_details_id_seq")
	@SequenceGenerator(name = "internal_inote_approval_details_id_seq", schema = "tdms", sequenceName = "internal_inote_approval_details_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "remarks", nullable = false)
	private String remarks;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by")
	private Employee modifiedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	private Employee approvedBy;

	 @ManyToOne
	   @JoinColumn(name = "internal_inote_id", nullable = false) 
	    private InternalINote internalInote;
	
	@Column(name = "emp_desgn")
	private String designation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "next_approver_id")
	private Employee nextApprover;

	@Column(name = "is_rfr")
	private Boolean isRfr;
	
	@JoinColumn(name="approval_status")
	private Integer approvalStatus;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "internalInoteApprovalDetails")
	private List<InternalOlAprovlDetAttachment> internalOlAprovlDetAttachments;


	
}
