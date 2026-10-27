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



@Entity
@Table(name = "workload", schema = "tdms")
public class WorkLoad implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8001475083938260790L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workload_id_seq")
	@SequenceGenerator(name = "workload_id_seq", schema = "tdms", sequenceName = "workload_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supply_order_id")	
	private SupplyOrder supplyOrder;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "plan_submitted_date")
	private Date planSubmittedDate;
	
	@Column(name = "total_est_days")
	private Integer totalEstDays;
	
	

	@Column(name = "total_act_days")
	private Integer totalActDays;
	
	@JsonIgnore
	@OneToMany(mappedBy  = "workLoad")
	private List<WorkloadInspActDet> workLoadDets;

	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}
	
	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}
	public Integer getId() {
		return id;
	}

	

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	

	public List<WorkloadInspActDet> getWorkLoadDets() {
		return workLoadDets;
	}

	public void setWorkLoadDets(List<WorkloadInspActDet> workLoadDets) {
		this.workLoadDets = workLoadDets;
	}

	public Date getPlanSubmittedDate() {
		return planSubmittedDate;
	}

	public void setPlanSubmittedDate(Date planSubmittedDate) {
		this.planSubmittedDate = planSubmittedDate;
	}

	public Integer getTotalEstDays() {
		return totalEstDays;
	}

	public void setTotalEstDays(Integer totalEstDays) {
		this.totalEstDays = totalEstDays;
	}

	public Integer getTotalActDays() {
		return totalActDays;
	}

	public void setTotalActDays(Integer totalActDays) {
		this.totalActDays = totalActDays;
	}
}
