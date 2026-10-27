package in.co.bel.olms.transaction.entity;

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

@Entity
@Table(name = "return_offer_letter", schema = "tdms")
public class ReturnOfferLetter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4542216932051544928L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_offer_letter_id_seq")
	@SequenceGenerator(name = "return_offer_letter_id_seq", schema = "tdms", sequenceName = "return_offer_letter_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "offer_letter_id", nullable = false)
	private OfferLetter offerLetter;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@JsonIgnore
	@OneToMany(mappedBy  = "returnOfferLetter")
	private List<ReturnOfferLetterDet> returnOfferLetterDets;
	
	
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public List<ReturnOfferLetterDet> getReturnOfferLetterDets() {
		return returnOfferLetterDets;
	}

	public void setReturnOfferLetterDets(List<ReturnOfferLetterDet> returnOfferLetterDets) {
		this.returnOfferLetterDets = returnOfferLetterDets;
	}
	
	

}
