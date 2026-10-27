package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

import lombok.Data;


@Entity
@Table(name="line_item_lots",schema="tdms")
@Data
public class LineItemLots implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1585104965869789630L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "line_item_lots_id_seq")
	@SequenceGenerator(name = "line_item_lots_id_seq", schema = "tdms", sequenceName = "line_item_lots_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="offered_qty")
	private BigDecimal offeredQty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="so_line_item_id")
	private SOLineItem soLineItem;
	
	@Column(name = "financial_year")
	private String financialYear;
	
	@Column(name = "unit_price")
	private Double unitPrice;
	
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;

}
