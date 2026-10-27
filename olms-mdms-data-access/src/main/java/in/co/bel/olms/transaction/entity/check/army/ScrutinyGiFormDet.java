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

import in.co.bel.olms.master.entity.checklist.army.ChecklistScrutinyGiForm;


/**
 * Checklist remarks for the scrutiny of the Gi Form details
 * @author Prateek Dwivedi
 * @since 09-05-2024
 */
@Entity
@Table(name = "scrutiny_gi_form_det", schema="tdms_check_army")
public class ScrutinyGiFormDet implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_gi_form_det_id_seq")
	@SequenceGenerator(name = "scrutiny_gi_form_det_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_gi_form_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "scrutiny_gi_form_id")
	private ScrutinyGiForm scrutinyGiForm;
	
	@ManyToOne
	@JoinColumn(name = "checklist_scrutiny_gi_form_id")
	private ChecklistScrutinyGiForm checklistScrutinyGiForm;
	
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

	public ScrutinyGiForm getScrutinyGiForm() {
		return scrutinyGiForm;
	}

	public void setScrutinyGiForm(ScrutinyGiForm scrutinyGiForm) {
		this.scrutinyGiForm = scrutinyGiForm;
	}

	public ChecklistScrutinyGiForm getChecklistScrutinyGiForm() {
		return checklistScrutinyGiForm;
	}

	public void setChecklistScrutinyGiForm(ChecklistScrutinyGiForm checklistScrutinyGiForm) {
		this.checklistScrutinyGiForm = checklistScrutinyGiForm;
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
	
}
