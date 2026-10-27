package in.co.bel.olms.transaction.entity;

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

@Data
@Entity
@Table(name="qar_allotment", schema="tdms")
public class QarAllotment {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "qar_allotment_id_seq")
	@SequenceGenerator(name="qar_allotment_id_seq",schema="tdms",sequenceName = "qar_allotment_id_seq",allocationSize = 1)
	
	@Column(name="id", nullable=false, unique=true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="offer_letter_id", nullable=false)
	private OfferLetter offerletter ; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="alloted_by", nullable=false)
	private Employee allotedBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="alloted_to", nullable=false)
	private Employee allotedTo;
	
	@Column(name="section", nullable=false)
	private String section;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createdDate;
	
}
