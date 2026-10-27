package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "emp_designation", schema = "mdms")

public class EmpDesignation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2317509411426593329L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "designation", length = 20, nullable = false)
	private String designation;
	
	@Column(name = "designation_hindi", length = 20, nullable = false)
	private String designationHindi;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDesignationHindi() {
		return designationHindi;
	}

	public void setDesignationHindi(String designationHindi) {
		this.designationHindi = designationHindi;
	}
	
	

}
