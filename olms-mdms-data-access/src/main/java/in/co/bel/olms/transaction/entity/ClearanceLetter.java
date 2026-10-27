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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;

/**
* ClearanceLetter entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-07-29
*/

@Entity
@Table(name = "clearance_letter", schema = "tdms")
@Data
public class ClearanceLetter implements Serializable{

	private static final long serialVersionUID = -274069469903621727L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_letter_id", nullable = false)
	private OfferLetter offerLetter;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "letter_date", nullable = false)
	private Date letterDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Column(name = "addressed_to", length = 500)
	private String addressedTo;
	
	@Column(name = "copy_to", length = 1000)
	private String copyTo;
	
	@Column(name = "title", length = 300)
	private String title;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="created_by")
	private Employee createdBy;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="signatory")
	private Employee signatory;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "clearanceLetter")
	private List<ClearanceLetterPoint> clearanceLetterPoints;
	
}
