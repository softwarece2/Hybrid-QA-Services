package in.co.bel.olms.transaction.entity;

import in.co.bel.olms.master.entity.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "fag_end_notice", schema = "tdms")
public class FagEndNotice implements Serializable{

	private static final long serialVersionUID = -9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fag_end_notice_seq")
	@SequenceGenerator(name = "fag_end_notice_seq", schema = "tdms", sequenceName = "fag_end_notice_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Column(name = "modified_datetime")
	private Date modifiedDateTime;
	
	@Column(name = "last_updated_by")
	private Integer lastUpdatedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supply_order_id")	
	private SupplyOrder supplyOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_letter_id")	
	private OfferLetter offerLetter;
	
	@Column(name = "issuer_signature", length = 100)
	private String issuerSignature;
	
	@Column(name = "issuer_name", length = 100)
	private String issuerName;
	
	@Column(name = "issuer_designation", length = 100)
	private String issuerDesignation;
	
	@Column(name = "order_no", nullable = false)
	private String orderNo;
	
	@Column(name = "order_date", nullable = false)
	private Date orderDate;
	
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@Column(name = "dd")
	private Date dd;

	@Column(name = "ol_number", nullable = false, length = 50)
	private String olNumber;
	
	@Column(name = "offer_date")
	private Date offerDate;
	
	@Column(name = "received_date")
	private Date recievedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_id")
	private Employee employee;

	@Column(name = "empl_id")
	private Integer emplId;


	@Column(name = "created_by")
	private Integer createdBy;

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getEmplId() {
		return emplId;
	}

	public void setEmplId(Integer emplId) {
		this.emplId = emplId;
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

	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}

	public String getIssuerSignature() {
		return issuerSignature;
	}

	public void setIssuerSignature(String issuerSignature) {
		this.issuerSignature = issuerSignature;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getIssuerDesignation() {
		return issuerDesignation;
	}

	public void setIssuerDesignation(String issuerDesignation) {
		this.issuerDesignation = issuerDesignation;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getOlNumber() {
		return olNumber;
	}

	public void setOlNumber(String olNumber) {
		this.olNumber = olNumber;
	}

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	public Date getRecievedDate() {
		return recievedDate;
	}

	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDd() {
		return dd;
	}

	public void setDd(Date dd) {
		this.dd = dd;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	
}
