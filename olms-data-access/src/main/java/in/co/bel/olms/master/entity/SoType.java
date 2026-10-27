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
@Table(name = "so_type", schema = "mdms")
public class SoType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946434408531616169L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "so_type_id_seq")
	@SequenceGenerator(name="so_status_id_seq",schema="mdms",sequenceName = "so_status_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "so_type", length = 20)
	private String soType;

	public Integer getId() {
		return id;
	}

	public String getSoType() {
		return soType;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSoType(String soType) {
		this.soType = soType;
	}
	
	
	
	

}
