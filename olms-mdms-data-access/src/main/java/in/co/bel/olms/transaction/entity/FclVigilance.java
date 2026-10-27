package in.co.bel.olms.transaction.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "fcl_vigilance", schema = "tdms")
public class FclVigilance implements Serializable{
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcl_vigilance_id_seq")
	@SequenceGenerator(name = "fcl_vigilance_id_seq", schema = "tdms", sequenceName = "fcl_vigilance_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="vigilance")
	private String vigilance;
	
	@ManyToOne
	@JoinColumn(name = "fcl_id")	
	private Fcl fcl;
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getVigilance() {
		return vigilance;
	}

	public void setVigilance(String vigilance) {
		this.vigilance = vigilance;
	}

	public Fcl getFcl() {
		return fcl;
	}

	public void setFcl(Fcl fcl) {
		this.fcl = fcl;
	}
	

}
