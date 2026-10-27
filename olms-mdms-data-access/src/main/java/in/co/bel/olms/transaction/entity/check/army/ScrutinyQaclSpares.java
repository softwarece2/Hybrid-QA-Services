package in.co.bel.olms.transaction.entity.check.army;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.transaction.entity.OfferLetter;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "scrutiny_qacl_spares", schema = "tdms_check_army")
public class ScrutinyQaclSpares implements Serializable{
	private static final long serialVersionUID = 6653287768252401777L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scrutiny_qacl_spares_id_seq")
	@SequenceGenerator(name = "scrutiny_qacl_spares_id_seq", schema = "tdms_check_army", sequenceName = "scrutiny_qacl_spares_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	
	@OneToOne
	@JoinColumn(name = "ol_id")	
	private OfferLetter offerLetter;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDatetime;

	@Column(name = "sign_qa_staff_id")
	private Integer signQaStaffId;

	@Temporal(TemporalType.DATE)
	@Column(name = "sign_qa_staff_date")
	private Date signQaStaffDate;
	
	@Column(name = "remarks_qa_team")
	private String remarksQaTeam;

	@Column(name = "remarks_qa_team_sign")
	private Integer remarksQaTeamSign;

	@Temporal(TemporalType.DATE)
	@Column(name = "remarks_qa_team_date")
	private Date remarksQaTeamDate;

	@Column(name = "remarks_qao")
	private String remarksQao;

	@Column(name = "remarks_qao_sign")
	private Integer remarksQaoSign;

	@Temporal(TemporalType.DATE)
	@Column(name = "remarks_qao_date")
	private Date remarksQaoDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;

	@JsonIgnore
	@OneToMany(mappedBy  = "scrutinyQaclSpares")
	private List<ScrutinyQaclSparesDet> scrutinyQaclSparesDets;
	
	@OneToOne
	@JoinColumn(name = "so_status_id")
	private SoStatus soStatus;
}
