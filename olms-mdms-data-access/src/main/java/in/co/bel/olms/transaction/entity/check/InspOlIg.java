package in.co.bel.olms.transaction.entity.check;

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

@Entity
@Table(name = "insp_ol_ig", schema = "tdms_check")
public class InspOlIg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3504770577322727954L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_ol_ig_id_seq")
	@SequenceGenerator(name = "insp_ol_ig_id_seq", schema = "tdms_check", sequenceName = "insp_ol_ig_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "offer_letter_id")
	private OfferLetter offerLetter;
	
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
	

	@JsonIgnore
	@OneToMany(mappedBy = "inspOlIg")
	private List<InspOlIgDet> inspOlIgDets;
	

	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
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

	public List<InspOlIgDet> getInspOlIgDets() {
		return inspOlIgDets;
	}

	public void setInspOlIgDets(List<InspOlIgDet> inspOlIgDets) {
		this.inspOlIgDets = inspOlIgDets;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}
	
	

}
