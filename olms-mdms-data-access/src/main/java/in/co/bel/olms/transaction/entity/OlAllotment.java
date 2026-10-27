package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;



import in.co.bel.olms.master.entity.Employee;
import lombok.Data;



@Data
@Entity
@Table(name="ol_allotment",schema = "tdms")
public class OlAllotment implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ol_allotment_id_seq")
	@SequenceGenerator(name="ol_allotment_id_seq",schema="tdms",sequenceName = "ol_allotment_id_seq",allocationSize = 1)
	
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_datetime", nullable=false)
	private Date createdDate;
	
	@Column(name="is_deligation")
	private Boolean isDeligation;


}
