package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import in.co.bel.olms.master.entity.Employee;


/**
 * 
 * @author Manasa B
 * @since 13-02-2023
 *
 */
@Entity
@Table(name = "po_observation", schema = "tdms")
public class PoObservation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6173905881822480934L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "po_observation_id_seq")
	@SequenceGenerator(name = "po_observation_id_seq", schema = "tdms", sequenceName = "po_observation_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "section_officer_id")
	private Employee secOfficer;
	
	@ManyToOne
	@JoinColumn(name = "supply_order_id")	 
	private SupplyOrder supplyOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;
		
	@ManyToOne
	@JoinColumn(name = "closure_officer_id")	 
	private Employee closureOfficer;

	
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "opa_emp_id")	 
	private Employee opaEmp;
	
	@Column(name = "status_id")
	private Integer statusId;
 	
	@Temporal(TemporalType.DATE)
	@Column(name = "opa_submited_date")
	private Date opaSubmittedDate;
		
	@Temporal(TemporalType.DATE)
	@Column(name = "closure_date")
	private Date closureDate;
		
	@JsonIgnore
	@OneToMany(mappedBy = "poObservation")
	private List<PoObservationDet> poObservationDets;

	public List<PoObservationDet> getPoObservationDets() {
		return poObservationDets;
	}

	public void setPoObservationDets(List<PoObservationDet> poObservationDets) {
		this.poObservationDets = poObservationDets;
	}

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

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Employee getClosureOfficer() {
		return closureOfficer;
	}

	public void setClosureOfficer(Employee closureOfficer) {
		this.closureOfficer = closureOfficer;
	}

	public Employee getOpaEmp() {
		return opaEmp;
	}

	public void setOpaEmp(Employee opaEmp) {
		this.opaEmp = opaEmp;
	}

	public Employee getSecOfficer() {
		return secOfficer;
	}

	public void setSecOfficer(Employee secOfficer) {
		this.secOfficer = secOfficer;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Date getOpaSubmittedDate() {
		return opaSubmittedDate;
	}

	public void setOpaSubmittedDate(Date opaSubmittedDate) {
		this.opaSubmittedDate = opaSubmittedDate;
	}

	public Date getClosureDate() {
		return closureDate;
	}

	public void setClosureDate(Date closureDate) {
		this.closureDate = closureDate;
	}

	public Integer getStatus() {
		return statusId;
	}

	public void setStatus(Integer statusId) {
		this.statusId = statusId;
	}

	
	
	

}
