package in.co.bel.olms.transaction.entity.check;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import in.co.bel.olms.master.entity.checklist.ChecklistOlIB;

@Entity
@Table(name = "insp_ol_ib_det", schema = "tdms_check")

public class InspOlIbDet implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = -2620922020388944938L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_ol_ib_det_id_seq")
	@SequenceGenerator(name = "insp_ol_ib_det_id_seq", schema = "tdms_check", sequenceName = "insp_ol_ib_det_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "remarks")
	private String remarks;
	
	@ManyToOne
	@JoinColumn(name = "insp_ol_ib")
	private InspOlIb inspOlIb;

	@ManyToOne
	@JoinColumn(name = "checklist_ol_ib_id")
	private ChecklistOlIB checklistOlIB;

	@Column(name = "obs_sts", nullable = false, length = 20)
	private String obsSts;
	

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}

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
	

	public InspOlIb getInspOlIb() {
		return inspOlIb;
	}

	public void setInspOlIb(InspOlIb inspOlIb) {
		this.inspOlIb = inspOlIb;
	}

	public ChecklistOlIB getChecklistOlIB() {
		return checklistOlIB;
	}

	public void setChecklistOlIB(ChecklistOlIB checklistOlIB) {
		this.checklistOlIB = checklistOlIB;
	}
	
	

}
