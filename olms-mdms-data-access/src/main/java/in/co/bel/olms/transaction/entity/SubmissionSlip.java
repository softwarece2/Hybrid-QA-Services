


package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.InspectionLevel;
import in.co.bel.olms.master.entity.Status;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Manasa B
 * @since 24-02-23
 */
@Getter
@Setter
@Entity
@Table(name = "submission_slip", schema = "tdms")
public class SubmissionSlip implements Serializable {

	private static final long serialVersionUID = -6339747765086203767L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "submission_slip_seq_id")
	@SequenceGenerator(name = "submission_slip_seq_id", schema = "tdms", sequenceName = "submission_slip_seq_id", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "ol_number")
	private String olNumber;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_order_id")
	private SaleOrder saleOrder;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "warrant_id")
	private Category warrant;

	
	
	private Boolean active;

	@Temporal(TemporalType.DATE)
	@Column(name = "offer_date")
	private Date offerDate;

//	@Column(name = "issue_level")
//	private String issueLevel;
//	
//	@Column(name = "priority")
//	private String priority;

//	@Column(name="cleared_qty")
//	private Integer clearedQty;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_officer_id")
	private Employee sectionOfficer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dcqao_id")
	private Employee dcqao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "jcqao_id")
	private Employee jcqao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cqao_id")
	private Employee cqao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consignee_id")
	private Consignee consignee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inspection_level_id")
	private InspectionLevel inspectionLevel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_status_id")
	private Status olStatus;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "submissionSlip", cascade = CascadeType.ALL)
	private List<SubSlipLineItem> subSlipLineItems;

	@JsonIgnore
	@OneToMany(mappedBy = "subSlip")
	private Set<SubSlipApprovalDetails> subSlipApprovalDetails;

	@JsonIgnore
	@OneToOne(mappedBy = "offerLetter")
	private ReturnOfferLetter returnOfferLetter;

	@JsonIgnore
	@OneToMany(mappedBy = "offerLetter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OfferLetterAttachment> OlAttachments;

	@JsonIgnore
	@OneToMany(mappedBy = "offerletter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OlAllotment> olAllotments;

	@JsonIgnore
	@OneToMany(mappedBy = "offerLetter", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<QinObsNcForm> qinObsNcForms;

	@Column(name = "stores_description", length = 200)
	private String storesDescription;

	@Column(name = "remarks", length = 200)
	private String remarks;

	@Column(name = "is_notified")
	private Boolean isNotified;

	@Column(name = "inote_approval")
	private Boolean inoteApproval;

	@Column(name = "inote_approval_remarks", length = 1000)
	private String inoteApprovalRemarks;

	@Column(name = "is_internal")
	private Boolean isInternal;

	@Column(name = "lot_number")
	private Integer lotNumber;
	
	 @Column(name = "is_merged" )
	 private Boolean isMerged;
	 
	 @PrePersist
	    public void setDefaultIsMerged() {
	        this.isMerged = false;
	    }

	public ReturnOfferLetter getReturnOfferLetter() {
		return returnOfferLetter;
	}

	public void setReturnOfferLetter(ReturnOfferLetter returnOfferLetter) {
		this.returnOfferLetter = returnOfferLetter;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOlNumber() {
		return olNumber;
	}

	public void setOlNumber(String olNumber) {
		this.olNumber = olNumber;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

//	public List<SubSlipLineItem> getOlLineItems() {
//		return olLineItems;
//	}
//
//	public void setOlLineItems(List<SubSlipLineItem> olLineItems) {
//		this.olLineItems = olLineItems;
//	}

//	public String getIssueLevel() {
//		return issueLevel;
//	}
//
//	public void setIssueLevel(String issueLevel) {
//		this.issueLevel = issueLevel;
//	}
//
//	public String getPriority() {
//		return priority;
//	}
//
//	public void setPriority(String priority) {
//		this.priority = priority;
//	}

	public Employee getSectionOfficer() {
		return sectionOfficer;
	}

	public void setSectionOfficer(Employee sectionOfficer) {
		this.sectionOfficer = sectionOfficer;
	}

	public Employee getDcqao() {
		return dcqao;
	}

	public void setDcqao(Employee dcqao) {
		this.dcqao = dcqao;
	}

	public Employee getJcqao() {
		return jcqao;
	}

	public void setJcqao(Employee jcqao) {
		this.jcqao = jcqao;
	}

	public Employee getCqao() {
		return cqao;
	}

	public void setCqao(Employee cqao) {
		this.cqao = cqao;
	}

	

//	public Integer getClearedQty() {
//		return clearedQty;
//	}
//
//	public void setClearedQty(Integer clearedQty) {
//		this.clearedQty = clearedQty;
//	}

	public Consignee getConsignee() {
		return consignee;
	}

	public void setConsignee(Consignee consignee) {
		this.consignee = consignee;
	}
//
//	public Set<OlApprovalDetails> getOlApprovalDetails() {
//		return olApprovalDetails;
//	}
//
//	public void setOlApprovalDetails(Set<OlApprovalDetails> olApprovalDetails) {
//		this.olApprovalDetails = olApprovalDetails;
//	}

	public List<QinObsNcForm> getQinObsNcForms() {
		return qinObsNcForms;
	}

	public void setQinObsNcForms(List<QinObsNcForm> qinObsNcForms) {
		this.qinObsNcForms = qinObsNcForms;
	}

	public InspectionLevel getInspectionLevel() {
		return inspectionLevel;
	}

	public void setInspectionLevel(InspectionLevel inspectionLevel) {
		this.inspectionLevel = inspectionLevel;
	}

	public String getStoresDescription() {
		return storesDescription;
	}

	public void setStoresDescription(String storesDescription) {
		this.storesDescription = storesDescription;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Status getOlStatus() {
		return olStatus;
	}

	public void setOlStatus(Status olStatus) {
		this.olStatus = olStatus;
	}

	public Boolean getIsNotified() {
		return isNotified;
	}

	public void setIsNotified(Boolean notified) {
		isNotified = notified;
	}

	public List<OfferLetterAttachment> getOlattachments() {
		return OlAttachments;
	}

	public void setOlAttachments(List<OfferLetterAttachment> olAttachments) {
		OlAttachments = olAttachments;
	}

	public List<OlAllotment> getOlAllotments() {
		return olAllotments;
	}

	public void setOlAllotments(List<OlAllotment> olAllotments) {
		this.olAllotments = olAllotments;
	}

	public Boolean getInoteApproval() {
		return inoteApproval;
	}

	public void setInoteApproval(Boolean inoteApproval) {
		this.inoteApproval = inoteApproval;
	}

	public String getInoteApprovalRemarks() {
		return inoteApprovalRemarks;
	}

	public void setInoteApprovalRemarks(String inoteApprovalRemarks) {
		this.inoteApprovalRemarks = inoteApprovalRemarks;
	}

	public Boolean getNotified() {
		return isNotified;
	}

	public void setNotified(Boolean notified) {
		isNotified = notified;
	}

	public Boolean getInternal() {
		return isInternal;
	}

	public void setInternal(Boolean internal) {
		isInternal = internal;
	}

	public Integer getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(Integer lotNumber) {
		this.lotNumber = lotNumber;
	}

	public String getSupplyOrderNo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Category getWarrant() {
		return warrant;
	}

	public void setWarrant(Category warrant) {
		this.warrant = warrant;
	}
	
	
	
}
