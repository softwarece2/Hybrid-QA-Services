package in.co.bel.olms.master.entity.checklist;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "checklist_ol", schema = "mdms")
public class ChecklistOlIG implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2686497698255475406L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "sl_no", length = 10, nullable = false)
	private String slno;
	 
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

	public String getSlno() {
		return slno;
	}

	public String getReference() {
		return reference;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSlno(String slno) {
		this.slno = slno;
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
