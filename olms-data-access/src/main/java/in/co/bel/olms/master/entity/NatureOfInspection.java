package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "nature_of_inspection", schema = "mdms")
public class NatureOfInspection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1912856888487614230L;
	
	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE,generator = "nature_of_inspection_id_seq")
	@SequenceGenerator(name="nature_of_inspection_id_seq",schema="mdms",sequenceName = "nature_of_inspection_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "inspection_nature", length = 50, nullable = false)
	private String inspectionNature;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInspectionNature() {
		return inspectionNature;
	}

	public void setInspectionNature(String inspectionNature) {
		this.inspectionNature = inspectionNature;
	}
	
	
	

}
