package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import javax.persistence.SequenceGenerator;

import lombok.Data;



@Entity
@Table(name="dd_extension",schema="tdms")
@Data
public class DdExtension implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -4838708537131400903L; 
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dd_extension_id_seq")
	@SequenceGenerator(name = "dd_extension_id_seq", schema = "tdms", sequenceName = "dd_extension_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "letter_date")
	private Date letterDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "new_dd_date")
	private Date newDdDate;
	
	@Column(name="remarks")
	private String remarks;
	
	@Column(name="letter_no")
	private String letterNo;


	@Column(name="status")
	private String status;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supply_order_id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "ddExtension", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DdExtensionAttachment> attachments;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="so_line_item_id")
	private SOLineItem soLineItem;
	
	private Integer type;

}
