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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.SerialNoStatus;
import lombok.Data;
@Entity
@Table(name="line_item_serial_no",schema="tdms")
@Data
public class LineItemSerialNo implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_item_serial_no_id_seq")
	@SequenceGenerator(name = "line_item_serial_no_id_seq", schema = "tdms", sequenceName = "line_item_serial_no_id_seq", allocationSize = 1)
	private Integer id;
	
	@Column(name="serial_no")
	private String serialNo;
		
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ser_no_sts_id")
	private SerialNoStatus status;
	   
	@Column(name="created_datetime")
	private Date createdAt;
	   
	@Column(name="modified_datetime")
	private Date modifiedAt;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="so_line_item_id")
	private SOLineItem soLineItem;	
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="ol_id")
	private OfferLetter olId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "sub_slip_id")
	private SubmissionSlip submissionSlipId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "created_by_id")
	private Employee createdBy;
	
}
