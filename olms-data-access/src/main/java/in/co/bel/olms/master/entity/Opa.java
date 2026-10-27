package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "opa", schema = "mdms")
public class Opa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opa_id_seq")
	@SequenceGenerator(name = "opa_id_seq", schema = "mdms", sequenceName = "opa_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name="opa_name")
	@Size(min = 2, max = 100, message = "Opa name must be between 2 and 100 characters")
	private String opaName;
	
	@Column(name="opa_short_name")
	@Size(min = 2, max = 100, message = "Opa shortname must be between 2 and 100 characters")
	private String opashortName;
	
	
	@Column(name="opa_address")
	private String opaAddress;
	
	@Column(name="opa_status")
	private Boolean opaStatus;


	public Boolean getOpaStatus() {
		return opaStatus;
	}


	public void setOpaStatus(Boolean opaStatus) {
		this.opaStatus = opaStatus;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getOpaName() {
		return opaName;
	}


	public void setOpaName(String opaName) {
		this.opaName = opaName;
	}


	public String getOpashortName() {
		return opashortName;
	}


	public void setOpashortName(String opashortName) {
		this.opashortName = opashortName;
	}


	public String getOpaAddress() {
		return opaAddress;
	}


	public void setOpaAddress(String opaAddress) {
		this.opaAddress = opaAddress;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	
}
