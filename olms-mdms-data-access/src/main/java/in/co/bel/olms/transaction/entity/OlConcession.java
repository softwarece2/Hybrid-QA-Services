package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;

/**
 * @author Awadhesh Verma
 * @since 06-07-23
 */

@Entity
@Table(name = "ol_concession", schema = "tdms")
@Data
public class OlConcession implements Serializable {

	private static final long serialVersionUID = 8913497243571065885L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ol_concession_seq_id")
	@SequenceGenerator(name = "ol_concession_seq_id", schema = "tdms", sequenceName = "ol_concession_seq_id", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "concession")
	private String concession;

	@Column(name = "affected_item_ref")
	private String affectedItemRef;

	@Column(name = "concession_details")
	private String concessionDetails;

	@Column(name = "authority")
	private String authority;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "offer_letter_id")
	private OfferLetter offerLetter;

}
