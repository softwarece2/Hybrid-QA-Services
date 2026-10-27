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

import in.co.bel.olms.master.entity.checklist.ChecklistScrutinySO;


/**
 * Checklist remarks for the scrutiny of the supply order
 * @author Manasa B
 * @since 17-01-2023
 */

@Entity
@Table(name = "scrutiny_so_det", schema = "tdms_check")
public class ScrutinySoDet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6611818983070415287L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_so_det_id_seq")
	@SequenceGenerator(name = "scrutiny_so_det_id_seq", schema = "tdms_check", sequenceName = "scrutiny_so_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name="remarks")
	private String  remarks;
	
	@ManyToOne
	@JoinColumn(name = "scrutnity_so_id")
	private ScrutinySo scrutinySo;

	
	@ManyToOne
	@JoinColumn(name = "checklist_scrutiny_id")	
	private ChecklistScrutinySO checklistScrutinySO;
	
	@Column(name = "obs_sts", nullable = false, length = 20)
	private String obsSts;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ScrutinySo getScrutinySo() {
		return scrutinySo;
	}

	public void setScrutinySo(ScrutinySo scrutinySo) {
		this.scrutinySo = scrutinySo;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public ChecklistScrutinySO getChecklistScrutinySO() {
		return checklistScrutinySO;
	}

	public void setChecklistScrutinySO(ChecklistScrutinySO checklistScrutinySO) {
		this.checklistScrutinySO = checklistScrutinySO;
	}

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}
	
	
	
}
