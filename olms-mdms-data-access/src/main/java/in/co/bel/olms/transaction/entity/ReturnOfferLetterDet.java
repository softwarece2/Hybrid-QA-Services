package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "return_offer_letter_det", schema = "tdms")
public class ReturnOfferLetterDet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5501098659242204133L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_offer_letter_det_id_seq")
	@SequenceGenerator(name = "return_offer_letter_det_id_seq", schema = "tdms", sequenceName = "return_offer_letter_det_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "return_offer_letter_id")
	private ReturnOfferLetter returnOfferLetter;
	
	@Column(name = "observation", length = 500)
	private String observation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ReturnOfferLetter getReturnOfferLetter() {
		return returnOfferLetter;
	}

	public void setReturnOfferLetter(ReturnOfferLetter returnOfferLetter) {
		this.returnOfferLetter = returnOfferLetter;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}


}
