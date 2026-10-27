package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "insp_detail", schema = "tdms")
public class InspDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5466865019079557844L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_detail_id_seq")
	@SequenceGenerator(name = "insp_detail_id_seq", schema = "tdms", sequenceName = "insp_detail_id_seq", allocationSize = 1)

	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "inspector_id", nullable = false)
	private Integer inspectorId;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dt_insp_commence")
	private Date dtInspCommence;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dt_insp_completed")
	private Date dtInspCompleted;

	public Integer getId() {
		return id;
	}

	public Integer getInspectorId() {
		return inspectorId;
	}

	public Date getDtInspCommence() {
		return dtInspCommence;
	}

	public Date getDtInspCompleted() {
		return dtInspCompleted;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setInspectorId(Integer inspectorId) {
		this.inspectorId = inspectorId;
	}

	public void setDtInspCommence(Date dtInspCommence) {
		this.dtInspCommence = dtInspCommence;
	}

	public void setDtInspCompleted(Date dtInspCompleted) {
		this.dtInspCompleted = dtInspCompleted;
	}
	
	
	

}
