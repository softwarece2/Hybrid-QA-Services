package in.co.bel.olms.transaction.entity;


import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
* Category entity class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2023-01-07 
*/

@Entity
@Table(name = "category", schema = "tdms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
	
	private static final long serialVersionUID = 1324924301088011454L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "serial", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "category_name", length = 150, nullable = false,unique=true)
	private String categoryName;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createdDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date")
	private Date modifiedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "modified_by")
	private Integer modifiedBy;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ship_id")
	private Ship ship;
	
	@JsonIgnore
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<SaleOrder> saleOrders;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "saleorder_id")
	private SaleOrder saleOrder;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "wt_iss_date")
	private Date wtIssueDate;

	private BigDecimal qty;
	
	
	// add the mapping

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "so_line_item_id")
	private SOLineItem soLineItem;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status")
	private Status serialNoStatus; 
}
