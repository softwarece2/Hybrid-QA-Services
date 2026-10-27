package in.co.bel.olms.transaction.entity.check.army;

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

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyPreInspection;


/**
 * Checklist remarks for the scrutiny of the Pre Inspection Details
 * @author Prateek Dwivedi
 * @since 11-05-2024
 */
@Entity
@Table(name = "scrutiny_pre_inspection_det", schema="tdms_check_army")
public class ScrutinyPreInspectionDet implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_pre_inspection_det_id_seq")
	@SequenceGenerator(name = "scrutiny_pre_inspection_det_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_pre_inspection_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "scrutiny_pre_inspection_id")
	private ScrutinyPreInspection scrutinyPreInspection;
	
	@ManyToOne
	@JoinColumn(name = "checklist_scrutiny_pre_inspection_id")
	private ChecklistScrutinyPreInspection checklistScrutinyPreInspection;
	
	@Column(name="remarks")
	private String  remarks;
	
	@Column(name="obs_sts")
	private String obsSts;
	
	@Column(name="modified_datetime")
	private Date modifiedDatetime;
	
	@Column(name="created_datetime")
	private Date createdDatetime;
	
	@Column(name="last_updated_by")
	private Integer lastUpdatedBy;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}

	public Date getModifiedDatetime() {
		return modifiedDatetime;
	}

	public void setModifiedDatetime(Date modifiedDatetime) {
		this.modifiedDatetime = modifiedDatetime;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public ScrutinyPreInspection getScrutinyPreInspection() {
		return scrutinyPreInspection;
	}

	public void setScrutinyPreInspection(ScrutinyPreInspection scrutinyPreInspection) {
		this.scrutinyPreInspection = scrutinyPreInspection;
	}

	public ChecklistScrutinyPreInspection getChecklistScrutinyPreInspection() {
		return checklistScrutinyPreInspection;
	}

	public void setChecklistScrutinyPreInspection(ChecklistScrutinyPreInspection checklistScrutinyPreInspection) {
		this.checklistScrutinyPreInspection = checklistScrutinyPreInspection;
	}
	
	
}
