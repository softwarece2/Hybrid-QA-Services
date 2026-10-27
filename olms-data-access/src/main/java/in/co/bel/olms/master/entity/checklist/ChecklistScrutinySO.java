package in.co.bel.olms.master.entity.checklist;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Manasa B
 * @since 17-01-2023
 *
 */

@Entity
@Table(name = "checklist_scrutiny_so", schema = "mdms")
public class ChecklistScrutinySO implements Serializable { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8140897351589334893L;

	@Id	
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "split")
	private Boolean split;
	
	@Column(name = "sl_no", nullable = false, length = 10)
	private String slNo;
	
	@Column(name = "reference", nullable = false, length = 300)
	private String reference;
	
	
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

