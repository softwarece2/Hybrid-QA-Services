package in.co.bel.olms.transaction.entity.check;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "scrutiny_ol", schema = "tdms")
public class ScrutinyOl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1216915228864976856L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_ol_id_seq")
	@SequenceGenerator(name = "scrutiny_ol_id_seq", schema = "tdms_check", sequenceName = "scrutiny_ol_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	@Column(name = "offer_letter_id")
	private Integer offerLetterId;
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;
	
	@Column(name = "inspected_by")
	private Integer inspectedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "inspected_date")
	private Date inspectedDate;
	
	@Column(name = "signed_by")
	private Integer signedBy;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "signed_date")
	private Date signedDate;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOfferLetterId() {
		return offerLetterId;
	}
	public void setOfferLetterId(Integer offerLetterId) {
		this.offerLetterId = offerLetterId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDatetime() {
		return createdDatetime;
	}
	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public Integer getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}
	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}
	public Integer getInspectedBy() {
		return inspectedBy;
	}
	public void setInspectedBy(Integer inspectedBy) {
		this.inspectedBy = inspectedBy;
	}
	public Date getInspectedDate() {
		return inspectedDate;
	}
	public void setInspectedDate(Date inspectedDate) {
		this.inspectedDate = inspectedDate;
	}
	public Integer getSignedBy() {
		return signedBy;
	}
	public void setSignedBy(Integer signedBy) {
		this.signedBy = signedBy;
	}
	public Date getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(Date signedDate) {
		this.signedDate = signedDate;
	}
	
	
	

	
	

}
