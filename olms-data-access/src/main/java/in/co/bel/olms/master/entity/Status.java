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
@Table(name = "status", schema = "mdms")
public class Status implements Serializable {
	
	
	private static final long serialVersionUID = -8304615035747250551L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_id_seq")
	@SequenceGenerator(name = "status_id_seq", schema = "mdms", sequenceName = "status_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

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

	@Column(name = "status",  nullable = false)
	private String status;

}
