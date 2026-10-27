package in.co.bel.olms.iol.ate.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.ObsType;
import in.co.bel.olms.ate.entity.Spec;
import in.co.bel.olms.ate.entity.Test;
import lombok.Data;

@Data
@Entity
@Table(name = "tvne1pa", schema = "iol")
public class Tvne1pa implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tvne_1pa_id_seq")
	@SequenceGenerator(name = "tvne_1pa_id_seq", schema = "iol", sequenceName = "tvne_1pa_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "spec_id", nullable = false)
	private Spec spec;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insp_round_id", nullable = false)
	private InspectionRound inspectionRound;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "obs_type_id", nullable = false)
	private ObsType obsType;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "obs_value")
	private String obsValue;

	@Column(name = "status")
	private String status;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private Test test;
	
	private Boolean activeStatus;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "file_upload_id")
	private AteTestReportUpload file;
	
}
