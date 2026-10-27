package in.co.bel.olms.master.entity;

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



/**
 * The persistent class for the Inspector table.
 * 
 * @author Manasa B
 */
@Entity
@Table(name = "inspector", schema = "mdms")
public class Inspector implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6503433953177315170L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="inspector_id_seq")
    @SequenceGenerator(name="inspector_id_seq",schema = "mdms",sequenceName = "inspector_id_seq",allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "insp_name", length = 100, nullable = false)
	private String inspName;
	
	@ManyToOne
	@JoinColumn(name = "insp_designation_id", nullable = false)	
	private EmpDesignation inspDesignation;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getInspName() {
		return inspName;
	}

	public void setInspName(String inspName) {
		this.inspName = inspName;
	}

	public EmpDesignation getInspDesignation() {
		return inspDesignation;
	}

	public void setInspDesignationId(EmpDesignation inspDesignation) {
		this.inspDesignation = inspDesignation;
	}
	
}
