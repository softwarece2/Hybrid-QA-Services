package in.co.bel.olms.iol.entity;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import in.co.bel.olms.ate.entity.InspectionRound;
import in.co.bel.olms.ate.entity.Test;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.OlStatus;
import lombok.Data;


@Data
@Entity
@Table(name = "insp_test_mapping", schema = "iol")
public class InspTestMapping implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "	insp_test_mapping_id_seq")
	@SequenceGenerator(name = "	insp_test_mapping_id_seq", schema = "iol", sequenceName = "	insp_test_mapping_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insp_id", nullable = false)
	private InspectionRound inspId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "submitted_by", nullable = false)
	private Employee submittedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_status_id", nullable = false)
	private OlStatus testStatusId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test_id", nullable = false)
	private Test testId;

}
