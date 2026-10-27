package in.co.bel.olms.transaction.entity;

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

import in.co.bel.olms.master.entity.ChecklistFileClose;

@Entity
@Table(name = "file_closing_form_det", schema = "tdms")
public class FileClosingFormDet implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5728389923888039571L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_closing_form_det_id_seq")
	@SequenceGenerator(name = "file_closing_form_det_id_seq", schema = "tdms", sequenceName = "file_closing_form_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "checklist_file_close_id")	
	private ChecklistFileClose checklistFileClose;
	
	@ManyToOne
	@JoinColumn(name = "file_closing_form_id")	
	private FileClosingForm fileClosingForm;
	
	public FileClosingForm getFileClosingForm() {
		return fileClosingForm;
	}

	public void setFileClosingForm(FileClosingForm fileClosingForm) {
		this.fileClosingForm = fileClosingForm;
	}
	
	@Column(name="remarks")
	private String  remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "obs_sts", nullable = false, length = 20)
	private String obsSts;

	public String getObsSts() {
		return obsSts;
	}

	public void setObsSts(String obsSts) {
		this.obsSts = obsSts;
	}

	public ChecklistFileClose getChecklistFileClose() {
		return checklistFileClose;
	}

	public void setChecklistFileClose(ChecklistFileClose checklistFileClose) {
		this.checklistFileClose = checklistFileClose;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
