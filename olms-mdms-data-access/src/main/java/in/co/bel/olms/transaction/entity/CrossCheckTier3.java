package in.co.bel.olms.transaction.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.co.bel.olms.master.entity.Employee;
import lombok.Data;

@Entity
@Table(name = "cross_check_tier_3", schema = "tdms")
@Data
public class CrossCheckTier3 {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cross_check_tier_3_id_seq")
	@SequenceGenerator(name = "cross_check_tier_3_id_seq", schema = "tdms", sequenceName = "cross_check_tier_3_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;

	@Column(name = "officer_sqao")
	private String officerSqao;

	@Column(name = "conducted_at")
	private String conductedAt;

	@Column(name = "conducted_date")
	private String conductedDate;

	@Column(name = "store_nomenclature")
	private String storeNomenclature;

	@Column(name = "instructions")
	private String instructions;

	@Column(name = "smpl_picked_from")
	private String smplPickedFrom;

	@Column(name = "nos_smpls_drawn")
	private String noSmplsDrawn;

	@Column(name = "scope_of_tests")
	private String scopeOfTests;

	@Column(name = "reason_for_omit")
	private String reasonForOmit;

	@Column(name = "results")
	private String results;

	@Column(name = "comp_comments")
	private String compComments;

	@Column(name = "inference")
	private String inference;

	@Column(name = "clarification_det")
	private String clarificationDet;

	@Column(name = "suggestion")
	private String suggestion;

	@Column(name = "recommendation")
	private String recommendation;

	@Column(name = "officer_name")
	private String officerName;

	@Column(name = "officer_sign")
	private String officerSign;

	@Column(name = "check_date")
	private String checkDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date", nullable=false)
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by")
    private Employee submittedBy;

}
