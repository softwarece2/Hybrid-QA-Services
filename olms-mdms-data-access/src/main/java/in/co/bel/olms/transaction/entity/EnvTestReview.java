package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "env_test_review", schema = "tdms")
public class EnvTestReview implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -434339333110696985L;
	
	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "firm_ref", length = 10)
	private String firmRef;
	
	@Column(name = "qa_insp_auth", length = 200)
	private String qaInspAuth;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_of_insp")
	private Date dateOfInsp;
	
	@Column(name = "firm_name", length = 200)
	private String firmName;
	
	@Column(name = "order_no", length = 50)
	private String orderNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "order_date")
	private Date orderDate;
	
	@Column(name = "description", length = 300)
	private String description;
	
	@Column(name = "prev_ref", length = 250)
	private String prevRef;
	
	@Column(name = "drg_no", length = 50)
	private String drgNo;
	
	@Column(name = "dispatch_to", length = 250)
	private String dispatchTo;
	
	@Column(name = "sign_on_behalf", length = 200)
	private String signOnBehalf;
	
	@Column(name = "signature")
	private String signature;
	
	@Column(name = "position_held", length = 100)
	private String positionHeld;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "review_date")
	private Date reviewDate;
	
	@Column(name = "remarks", length = 350)
	private String remarks;
	
	@Column(name = "insp_cert_no", length = 50)
	private String inspCertNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirmRef() {
		return firmRef;
	}

	public void setFirmRef(String firmRef) {
		this.firmRef = firmRef;
	}

	public String getQaInspAuth() {
		return qaInspAuth;
	}

	public void setQaInspAuth(String qaInspAuth) {
		this.qaInspAuth = qaInspAuth;
	}

	public Date getDateOfInsp() {
		return dateOfInsp;
	}

	public void setDateOfInsp(Date dateOfInsp) {
		this.dateOfInsp = dateOfInsp;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPrevRef() {
		return prevRef;
	}

	public void setPrevRef(String prevRef) {
		this.prevRef = prevRef;
	}

	public String getDrgNo() {
		return drgNo;
	}

	public void setDrgNo(String drgNo) {
		this.drgNo = drgNo;
	}

	public String getDispatchTo() {
		return dispatchTo;
	}

	public void setDispatchTo(String dispatchTo) {
		this.dispatchTo = dispatchTo;
	}

	public String getSignOnBehalf() {
		return signOnBehalf;
	}

	public void setSignOnBehalf(String signOnBehalf) {
		this.signOnBehalf = signOnBehalf;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPositionHeld() {
		return positionHeld;
	}

	public void setPositionHeld(String positionHeld) {
		this.positionHeld = positionHeld;
	}

	public Date getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(Date reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getInspCertNo() {
		return inspCertNo;
	}

	public void setInspCertNo(String inspCertNo) {
		this.inspCertNo = inspCertNo;
	}
	

	
}
