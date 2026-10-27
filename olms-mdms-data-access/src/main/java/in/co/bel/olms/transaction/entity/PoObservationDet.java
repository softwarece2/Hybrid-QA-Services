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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.transaction.entity.check.ScrutinySoDet;
import in.co.bel.olms.transaction.entity.check.army.ScrutinyTenderDet;


/**
 * 
 * @author Manasa B
 * @since 11-02-2023
 */

@Entity
@Table(name = "po_observation_det", schema = "tdms")
public class PoObservationDet implements Serializable {

	/*
	 * 
	 */
	private static final long serialVersionUID = 2592281675127639111L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "po_observation_det_id_seq")
	@SequenceGenerator(name = "po_observation_det_id_seq", schema = "tdms", sequenceName = "po_observation_det_id_seq", allocationSize = 1)

	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "po_observation_id")	
	private PoObservation poObservation;
	
	@Column(name = "observation", length = 500)
	private String observation;
	
	@Column(name = "remedial_action", length = 500)
	private String remedialAction;
	
	@Column(name = "remarks", length = 500)
	private String remarks;

	@Column(name = "is_closed", length = 500)
	private Boolean isClosed;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "scrutiny_so_det_id")	
	private ScrutinySoDet scrutinySoDet;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "scrutiny_tender_det_id")	
	private ScrutinyTenderDet scrutinyTenderDet;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PoObservation getPoObservation() {
		return poObservation;
	}

	public void setPoObservation(PoObservation poObservation) {
		this.poObservation = poObservation;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getRemedialAction() {
		return remedialAction;
	}

	public void setRemedialAction(String remedialAction) {
		this.remedialAction = remedialAction;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getIsClosed() {
		return isClosed;
	}

	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public ScrutinySoDet getScrutinySoDet() {
		return scrutinySoDet;
	}

	public void setScrutinySoDet(ScrutinySoDet scrutinySoDet) {
		this.scrutinySoDet = scrutinySoDet;
	}

	public ScrutinyTenderDet getScrutinyTenderDet() {
		return scrutinyTenderDet;
	}

	public void setScrutinyTenderDet(ScrutinyTenderDet scrutinyTenderDet) {
		this.scrutinyTenderDet = scrutinyTenderDet;
	}

	
}
