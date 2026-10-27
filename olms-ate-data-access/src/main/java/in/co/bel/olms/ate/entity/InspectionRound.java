package in.co.bel.olms.ate.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.transaction.entity.LineItemLotsRegister;
import in.co.bel.olms.transaction.entity.LineItemSerialNo;
import in.co.bel.olms.transaction.entity.OfferLetter;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import in.co.bel.olms.transaction.entity.SubSlipSerialNo;
import lombok.Data;

@Data
@Entity
@Table(name = "insp_round", schema = "ate")
public class InspectionRound implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_round_id_seq")
	@SequenceGenerator(name = "insp_round_id_seq", schema = "ate", sequenceName = "insp_round_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_id")
	private LineItemLotsRegister lineItemLotsRegister;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sslip_serial_no_id")
	private SubSlipSerialNo subSlipSerialNo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "inspDate")
	private Date inspDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitted_by", nullable = false)
	private Employee submittedBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by", nullable = false)
	private Employee approvedBy;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_slip_line_item_id", nullable = false)
	private SubSlipLineItem subSlipLineItem;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_line_item_id", nullable = false)
	private OlLineItem olLineItem;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proudct_id", nullable = false)
	private Products product;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Column(name = "status")
	private String status;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
		
	@Column(name = "round_id")
	private Integer roundId;
	
	
	@Column(name = "is_qa")
	private Boolean isQa;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_id")
	private OfferLetter offerletter;

	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_ser_no_id")
	private LineItemSerialNo lineItemSerialNo;
	


}
