package in.co.bel.olms.master.entity.checklist.army;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "checklist_pre_inspection", schema = "mdms_army")
public class ChecklistScrutinyPreInspection implements Serializable{

	private static final long serialVersionUID = 9223372036854775807L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_pre_inspection_id_seq")
	@SequenceGenerator(name = "checklist_pre_inspection_id_seq", schema = "mdms_army", sequenceName = "checklist_pre_inspection_id_seq", allocationSize = 1)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "split")
	private Boolean split;
	
	@Column(name = "sl_no", nullable = false, length = 5)
	private String slNo;
	
	@Column(name = "reference", length = 300)
	private String reference;
	
	
	@Column(name = "obs_type", length = 20)
	private String obsType;
	
	
//	@Column(name = "status")
//	private Boolean status;



//	public Boolean getStatus() {
//		return status;
//	}
//
//
//	public void setStatus(Boolean status) {
//		this.status = status;
//	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Boolean getSplit() {
		return split;
	}


	public void setSplit(Boolean split) {
		this.split = split;
	}


	public String getSlNo() {
		return slNo;
	}


	public void setSlNo(String slNo) {
		this.slNo = slNo;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public String getObsType() {
		return obsType;
	}


	public void setObsType(String obsType) {
		this.obsType = obsType;
	}
	
	
}
