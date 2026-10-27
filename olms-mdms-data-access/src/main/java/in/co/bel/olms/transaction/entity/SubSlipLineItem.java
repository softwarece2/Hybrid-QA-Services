
package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sslip_line_item", schema = "tdms")
public class SubSlipLineItem implements Serializable {

	private static final long serialVersionUID = 6016740231465257719L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sslip_line_item_id_seq")
	@SequenceGenerator(name = "sslip_line_item_id_seq", schema = "tdms", sequenceName = "sslip_line_item_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "so_line_item_id")
	private SOLineItem soLineItem;
	
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "sub_slip_id")
	private SubmissionSlip submissionSlip;
	
	
	@Column(name = "offered_qty")
	private BigDecimal offeredQty;
	
	@Column(name = "cleared_qty")
	private BigDecimal clearedQty;
	
	@Column(name = "created_datetime")
	private Date createdDateTime;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_datetime")
	private Date modifiedDateTime;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Column(name = "issue_level", length = 5)
	private String issueLevel;
	
	@Column(name = "priority", length = 20)
	private String priority;
	
	@Column(name = "tmr_file_name")
	private String tmrFileName;
	
	@Column(name = "remaining_qty")
	private BigDecimal reaminaingQty;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "olLineItem", cascade = CascadeType.ALL)
	private List<LineItemApplicableTest> lineItemApplicableTests;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "olLineItemId", cascade = CascadeType.ALL)
	private List<LineItemLotsRegister> lineItemLotsRegisters;

	public Integer getId() {
		return id;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}

	public Integer getModofiedBy() {
		return modifiedBy;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public void setModifiedBy(Integer modofiedBy) {
		this.modifiedBy = modofiedBy;
	}

	public SOLineItem getSoLineItem() {
		return soLineItem;
	}

	public void setSoLineItem(SOLineItem soLineItem) {
		this.soLineItem = soLineItem;
	}


	public SubmissionSlip getSubmissionSlip() {
		return submissionSlip;
	}

	public void setSubmissionSlipId(SubmissionSlip submissionSlip) {
		this.submissionSlip = submissionSlip;
	}
	
	public BigDecimal getOfferedQty() {
		return offeredQty;
	}

	public void setOfferedQty(BigDecimal offeredQty) {
		this.offeredQty = offeredQty;
	}

	public BigDecimal getClearedQty() {
		return clearedQty;
	}

	public void setClearedQty(BigDecimal clearedQty) {
		this.clearedQty = clearedQty;
	}

	public String getIssueLevel() {
		return issueLevel;
	}

	public void setIssueLevel(String issueLevel) {
		this.issueLevel = issueLevel;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTmrFileName() {
		return tmrFileName;
	}

	public void setTmrFileName(String tmrFileName) {
		this.tmrFileName = tmrFileName;
	}

	public List<LineItemApplicableTest> getLineItemApplicableTests() {
		return lineItemApplicableTests;
	}

	public void setLineItemApplicableTests(List<LineItemApplicableTest> lineItemApplicableTests) {
		this.lineItemApplicableTests = lineItemApplicableTests;
	}

	public BigDecimal getReaminaingQty() {
		return reaminaingQty;
	}

	public void setReaminaingQty(BigDecimal reaminaingQty) {
		this.reaminaingQty = reaminaingQty;
	}

	public List<LineItemLotsRegister> getLineItemLotsRegisters() {
		return lineItemLotsRegisters;
	}

	public void setLineItemLotsRegisters(List<LineItemLotsRegister> lineItemLotsRegisters) {
		this.lineItemLotsRegisters = lineItemLotsRegisters;
	}
}
