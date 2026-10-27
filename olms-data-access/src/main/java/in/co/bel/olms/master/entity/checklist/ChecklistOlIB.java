package in.co.bel.olms.master.entity.checklist;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "checklist_ol_ib", schema = "mdms")
public class ChecklistOlIB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1675976146001154576L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "checklist_ol_ib_id_seq")
	@SequenceGenerator(name = "checklist_ol_ib_id_seq", schema = "mdms", sequenceName = "checklist_ol_ib_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "sl_no", length = 10)
	private String slNo;
	
	@Column(name = "reference", length = 300, nullable = false)
	private String reference;
	
	@Column(name = "split")
	private Boolean split;
	
	@Column(name = "obs_type", nullable = false, length = 20)
	private String obsType;
	
	
	public Boolean getSplit() {
		return split;
	}
	public void setSplit(Boolean split) {
		this.split = split;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
