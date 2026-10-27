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
@Table(name = "qar_sqae_part_c", schema = "tdms_army")
@Data
public class QarSqaePartC {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qar_sqae_part_c_id_seq")
	@SequenceGenerator(name = "qar_sqae_part_c_id_seq", schema = "tdms_army", sequenceName = "qar_sqae_part_c_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;

	@Column(name = "location")
	private String location;

	@Column(name = "date")
	private String date;

	@Column(name = "test_rep_encl")
	private String testRepEncl;

	@Column(name = "acceptance_det_encl")
	private String acceptanceDetEncl;

	@Column(name = "srl_no")
	private String srlNo;

	@Column(name = "lab_det_encl")
	private String labDetEncl;

	@Column(name = "reasons")
	private String reasons;

	@Column(name = "deficiences_encl")
	private String deficiencesEncl;

	@Column(name = "dispatch_encl")
	private String dispatchEncl;

	@Column(name = "sectn_officer_sign")
	private String sectnOfficerSign;

	@Column(name = "sectn_officer_name")
	private String sectnOfficerName;

	@Column(name = "sectn_officer_desig")
	private String sectnOfficerDesig;

	@Column(name = "date_of_filling")
	private String dateOfFilling;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date", nullable=false)
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by")
    private Employee submittedBy;

}
