package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.InspPhaseActivities;

@Entity
@Table(name = "workload_ol_det", schema = "tdms")
public class WorkloadOlDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9140627249311571409L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workload_ol_det_id_seq")
	@SequenceGenerator(name = "workload_ol_det_id_seq", schema = "tdms", sequenceName = "workload_ol_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "workload_ol_id")
	private WorkLoadOl workLoadOl;
		
	@Column(name = "est_man_days")
	private Double estManDays;
	
	@Column(name = "plan_completion")
	private Date planCompletion;	
	
	
	@Column(name = "actual_man_days")
	private Double actualManDays;
	
	@Column(name = "actual_completion")
	private Date actualCompletion;
	
	@Column(name = "remarks", length = 500)
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name = "insp_phase_activities_id")
	private InspPhaseActivities inspPhaseActivities;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public WorkLoadOl getWorkLoadOl() {
		return workLoadOl;
	}

	public void setWorkLoadOl(WorkLoadOl workLoadOl) {
		this.workLoadOl = workLoadOl;
	}

	
	public Double getActualManDays() {
		return actualManDays;
	}

	public void setActualManDays(Double actualManDays) {
		this.actualManDays = actualManDays;
	}

	public Date getActualCompletion() {
		return actualCompletion;
	}

	public void setActualCompletion(Date actualCompletion) {
		this.actualCompletion = actualCompletion;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public InspPhaseActivities getInspPhaseActivities() {
		return inspPhaseActivities;
	}

	public void setInspPhaseActivities(InspPhaseActivities inspPhaseActivities) {
		this.inspPhaseActivities = inspPhaseActivities;
	}

	public Double getEstManDays() {
		return estManDays;
	}

	public void setEstManDays(Double estManDays) {
		this.estManDays = estManDays;
	}

	public Date getPlanCompletion() {
		return planCompletion;
	}

	public void setPlanCompletion(Date planCompletion) {
		this.planCompletion = planCompletion;
	}

	
	

}
