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
@Table(name = "uom", schema = "mdms")
public class Uom implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3373103977053340587L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "uom_id_seq")
	@SequenceGenerator(name="uom_id_seq",schema="mdms",sequenceName = "uom_id_seq",allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "unit", length =	20, nullable = false)
	private String unit;
	
	@Column(name="active")
	private Boolean active;
	
	public Integer getId() {
		return id;
	}

	public String getUnit() {
		return unit;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	
	

}
