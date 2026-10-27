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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "fcl", schema = "tdms")
public class Fcl implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2178876758287546033L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcl_id_seq")
	@SequenceGenerator(name = "fcl_id_seq", schema = "tdms", sequenceName = "fcl_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;
	
	@OneToOne
	@JoinColumn(name = "supply_order_id", nullable = false)	
	private SupplyOrder supplyOrder;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date")
	private Date createdDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "fcl", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<FclVigilance> fclVigilances;

	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@Column(name = "so_order_no")
	private String soOrderNo;
	
	@Column(name = "so_date")
	private String soDate;
	
	
	@Column(name = "opa")
	private String opa;
	
	
	@Column(name = "project_name")
	private String projectName;
	
	
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	

	public List<FclVigilance> getFclVigilances() {
		return fclVigilances;
	}

	public void setFclVigilances(List<FclVigilance> fclVigilances) {
		this.fclVigilances = fclVigilances;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public String getSoOrderNo() {
		return soOrderNo;
	}

	public void setSoOrderNo(String soOrderNo) {
		this.soOrderNo = soOrderNo;
	}

	public String getSoDate() {
		return soDate;
	}

	public void setSoDate(String soDate) {
		this.soDate = soDate;
	}

	public String getOpa() {
		return opa;
	}

	public void setOpa(String opa) {
		this.opa = opa;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	
	
}
