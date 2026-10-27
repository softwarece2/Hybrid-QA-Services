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

import in.co.bel.olms.master.entity.checklist.ChecklistOlIG;

@Entity
@Table(name = "insp_ol_ig_det", schema = "tdms_check")
public class InspOlIgDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4901890491724831344L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_ol_ig_det_id_seq")
	@SequenceGenerator(name = "insp_ol_ig_det_id_seq", schema = "tdms_check", sequenceName = "insp_ol_ig_det_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name="remarks")
	private String  remarks;
	
	@ManyToOne
	@JoinColumn(name = "checklist_ol_ig_id")	
	private ChecklistOlIG checklistOlIG;



	@Column(name = "obs_sts", nullable = false, length = 20)
	private String obsSts;
	

	@ManyToOne
	@JoinColumn(name = "insp_ol_ig")
	private InspOlIg inspOlIg;

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

	public InspOlIg getInspOlIg() {
		return inspOlIg;
	}

	public void setInspOlIg(InspOlIg inspOlIg) {
		this.inspOlIg = inspOlIg;
	}
	public ChecklistOlIG getChecklistOlIG() {
		return checklistOlIG;
	}

	public void setChecklistOlIG(ChecklistOlIG checklistOlIG) {
		this.checklistOlIG = checklistOlIG;
	}

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}
	

}
