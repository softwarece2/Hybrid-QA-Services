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
@Table(name = "sections", schema = "mdms" )

public class Sections implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -6620290616879668591L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator="sections_id_seq")
	@SequenceGenerator(name="sections_id_seq",schema="sections_id_seq",sequenceName = "sections_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "sec_name", nullable = false, length = 50)
	private String secName;

	public Integer getId() {
		return id;
	}

	public String getSecName() {
		return secName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSecName(String secName) {
		this.secName = secName;
	}
	
	
	
}
