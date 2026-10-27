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
@Table(name = "ol_status", schema = "mdms")
public class OlStatus implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -755879925770489542L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "ol_status_id_seq")
	@SequenceGenerator(name="ol_status_id_seq",schema="mdms",sequenceName = "ol_status_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "status", length = 50, nullable = false)
	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
