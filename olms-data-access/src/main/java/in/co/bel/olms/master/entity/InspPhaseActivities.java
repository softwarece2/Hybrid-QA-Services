package in.co.bel.olms.master.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "insp_phase_activities", schema = "mdms")
public class InspPhaseActivities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8310851927796215653L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insp_phase_activities_id_seq")
	@SequenceGenerator(name = "insp_phase_activities_id_seq", schema = "mdms", sequenceName = "insp_phase_activities_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insp_phase_id", nullable = false)
	private InspPhase inspPhase;
	
	@Column(name = "activity", length = 100)
	private String activity;
	
	@Column(name = "order")
	private Integer order;
	
	@Column(name = "sl_no", length = 10)
	private String slNo;

}
