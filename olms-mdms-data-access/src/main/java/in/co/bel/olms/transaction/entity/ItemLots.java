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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;


@Entity
@Table(name="item_lots",schema="tdms")
@Data
public class ItemLots implements Serializable {

	private static final long serialVersionUID = 4902819882285578729L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_lots_id_seq")
	@SequenceGenerator(name = "item_lots_id_seq", schema = "tdms", sequenceName = "item_lots_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "il_letter_date")
	private Date ilLetterDate;
	
	@Column(name="il_letter_no")
	@Pattern(regexp = "^-?\\d{1,50}$", message = "Must contain only numeric digits")
	@NotBlank(message = "Value must not be Empty")
	private String ilLetterNo;
	
	@Column(name="lot_no")
	@Pattern(regexp = "^-?\\d{1,50}$", message = "Must contain only numeric digits")
	@NotBlank(message = "Value must not be Empty")
	private String lotNo;
	
	@Column(name="status")
	private String status;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="supply_order_id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "itemLots", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ItemLotsAttachment> attachments;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="so_line_item_id")
	private SOLineItem soLineItem;
	
	private Integer type;
}
