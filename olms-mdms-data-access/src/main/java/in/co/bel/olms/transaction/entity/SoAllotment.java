package in.co.bel.olms.transaction.entity;

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

import in.co.bel.olms.master.entity.Employee;


/**
 * @author Manasa B
 * @since 05-04-23
 */

@Entity
@Table(name = "so_allotment", schema = "tdms")
public class SoAllotment {


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "so_allotment_id_seq")
	@SequenceGenerator(name = "so_allotment_id_seq", schema = "tdms", sequenceName = "so_allotment_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "supply_order_id", referencedColumnName = "id")
	private SupplyOrder supplyOrder;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alloted_by")
	private Employee allotedBy;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "alloted_to")
	private Employee allotedTo;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}


	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}


	public Employee getAllotedBy() {
		return allotedBy;
	}


	public void setAllotedBy(Employee allotedBy) {
		this.allotedBy = allotedBy;
	}


	public Employee getAllotedTo() {
		return allotedTo;
	}


	public void setAllotedTo(Employee allotedTo) {
		this.allotedTo = allotedTo;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
	
}
