package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "offer_letter_attachment", schema = "tdms")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferLetterAttachment implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6678077306530869930L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "offer_letter_attachment_id_seq")
	@SequenceGenerator(name = "offer_letter_attachment_id_seq", schema = "tdms", sequenceName = "offer_letter_attachment_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "offer_letter_id")
	private OfferLetter offerLetter;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "category", length = 30)
	private String category;


}
