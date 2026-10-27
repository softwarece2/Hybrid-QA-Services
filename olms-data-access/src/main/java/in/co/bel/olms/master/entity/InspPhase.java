package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "insp_phase", schema = "mdms")
public class InspPhase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2267220832083992470L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_phase_id_seq")
	@SequenceGenerator(name = "insp_phase_id_seq", schema = "mdms", sequenceName = "insp_phase_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "phase", length = 50, nullable = false)
	private String phase;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}
	
	

}
