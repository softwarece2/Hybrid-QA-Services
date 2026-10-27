package in.co.bel.olms.transaction.entity.qar.sqae;

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
@Table(name = "qar_sqae_part_d", schema = "tdms_army")
@Data
public class QarSqaePartD {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qar_sqae_part_d_id_seq")
	@SequenceGenerator(name = "qar_sqae_part_d_id_seq", schema = "tdms_army", sequenceName = "qar_sqae_part_d_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;

	@Column(name = "obs_encl")
	private String obsEncl;

	@Column(name = "vigilance_check_date")
	private String vigilanceCheckDate;

	@Column(name = "vigilance_checked_by")
	private String vigilanceCheckedBy;

	@Column(name = "cross_verf_date")
	private String crossVerificationDate;

	@Column(name = "is_cleared")
	private String isCleared;

	@Column(name = "is_authorised")
	private String isAuthorised;

	@Column(name = "obs_cross_ver_encl")
	private String obsCrossVerificationEncl;

	@Column(name = "approver_sign")
	private String approverSign;

	@Column(name = "approver_name")
	private String approverName;

	@Column(name = "approved_date")
	private String approvedDate;

	@Column(name = "in_order")
	private String inOrder;

	@Column(name = "is_carried_out")
	private String isCarriedOut;

	@Column(name = "action_intiated")
	private String actionInitiated;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date", nullable=false)
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by")
    private Employee submittedBy;
}
