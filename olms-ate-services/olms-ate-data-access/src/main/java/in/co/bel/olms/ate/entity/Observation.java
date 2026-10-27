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
import in.co.bel.olms.transaction.entity.OlLineItem;
import lombok.Data;

@Data
@Entity
@Table(name = "observation", schema = "ate")
public class Observation implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "obs_id_seq")
	@SequenceGenerator(name = "obs_id_seq", schema = "ate", sequenceName = "obs_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "obs")
	private String obs;

	@Column(name = "remarks")
	private String remarks;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spec_id", nullable = false)
	private Spec spec;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_line_item_id", nullable = false)
	private OlLineItem olLineItem;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prod_id", nullable = false)
	private Products product;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;

	@Column(name = "obs_value")
	private Double obsValue;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dev_id")
	private LineItemLotsRegister lineItemLotsRegister;

	
	@Column(name = "result")
	private String result;

	@Column(name = "page_no")
	private Integer pageNo;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fai_id")
	private FAI fai;
	
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	private Employee createdBy;

	
	

}
