package in.co.bel.olms.transaction.entity.check;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.checklist.ChecklistOlIG;

@Entity
@Table(name = "scrutiny_ol_det", schema = "tdms_check")
public class ScrutinyOlDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8611590045501602964L;
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name="remarks")
	private String  remarks;
	
	

	@ManyToOne
	@JoinColumn(name = "scrutiny_ol_id")
	private ScrutinyOl scrutinyOl;
	
	@ManyToOne
	@JoinColumn(name = "checklist_ol")
	private ChecklistOlIG checklistOl;

	@Column(name = "obs_sts", nullable = false, length = 20)
	private String obsSts;
	

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}
	
	@Column(name = "verified")
	private String verified;

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
	public ScrutinyOl getScrutinyOl() {
		return scrutinyOl;
	}

	public void setScrutinyOl(ScrutinyOl scrutinyOl) {
		this.scrutinyOl = scrutinyOl;
	}

	public ChecklistOlIG getChecklistOl() {
		return checklistOl;
	}

	public void setChecklistOl(ChecklistOlIG checklistOl) {
		this.checklistOl = checklistOl;
	}

	public String getVerified() {
		return verified;
	}

	public void setVerified(String verified) {
		this.verified = verified;
	}
	
	

}
