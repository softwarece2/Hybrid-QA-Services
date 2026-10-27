package in.co.bel.olms.transaction.entity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sale_order", schema = "tdms")
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleOrder implements Serializable {

	private static final long serialVersionUID = -4670003458657846435L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_order_id_seq")
	@SequenceGenerator(name = "sale_order_id_seq", schema = "tdms", sequenceName = "sale_order_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "sale_order_no", nullable = false, length = 200)
	private String saleOrderNo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_date")
	private Date modifiedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@ManyToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	@ManyToOne
	@JoinColumn(name = "supply_order_id", referencedColumnName = "id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "saleOrder")
	private List<SOLineItem> soLineItems;
	
	private Boolean active;

}
