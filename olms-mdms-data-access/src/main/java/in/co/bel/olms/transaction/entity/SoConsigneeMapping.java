package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import in.co.bel.olms.master.entity.Consignee;
import lombok.Data;

@Data
@Entity
@Table(name = "so_consignee_mapping", schema = "tdms")
public class SoConsigneeMapping implements Serializable {

	private static final long serialVersionUID = 5317682902599981690L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supply_order_id", nullable = false)
	private SupplyOrder supplyOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "so_line_item_id")
	@Cascade(CascadeType.REMOVE)
	private SOLineItem soLineitem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consignee_id", nullable = false)
	private Consignee consignee;

}
