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
@Table(name = "qar_sqae_part_b", schema = "tdms_army")
@Data
public class QarSqaePartB {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qar_sqae_part_b_id_seq")
	@SequenceGenerator(name = "qar_sqae_part_b_id_seq", schema = "tdms_army", sequenceName = "qar_sqae_part_b_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Integer id;

	@Column(name = "spec_det")
	private String specDet;

	@Column(name = "drawing_det")
	private String drawingDet;

	@Column(name = "test_schedule_det")
	private String testScheduleDet;

	@Column(name = "acce_stores_cost")
	private String acceStoresCost;

	@Column(name = "pre_insp_encl")
	private String preInspEncl;

	@Column(name = "firm_l_no")
	private String firmLNo;

	@Column(name = "not_rcvd_rsn_encl")
	private String notRcvdRsnEncl;

	@Column(name = "sampling_det_encl")
	private String samplingDetEncl;

	@Column(name = "literature_encl")
	private String literatureEncl;

	@Column(name = "lit_aprv_encl")
	private String litAprvEncl;

	@Column(name = "lit_sub_encl")
	private String litSubEncl;

	@Column(name = "lit_verf_encl_")
	private String litVerfEncl;

	@Column(name = "qty_due")
	private String qtyDue;

	@Column(name = "nos_bulk_supply")
	private String nosBulkSupply;

	@Column(name = "nos_copies_supplied")
	private String nosCopiesSupplied;

	@Column(name = "full_qty_ltr_encl")
	private String fullQtyLtrEncl;

	@Column(name = "nos_examined")
	private String nosExamined;

	@Column(name = "raw_mtrl_acp_encl")
	private String rawMtrlAcpEncl;

	@Column(name = "nos_fully_working")
	private String nosFullyWorking;

	@Column(name = "detailed_working_encl")
	private String detailedWorkingEncl;

	@Column(name = "spec_egpt_det_encl")
	private String specEgptDetEncl;

	@Column(name = "warrantee_encl")
	private String warranteeEncl;

	@Column(name = "nos_packages")
	private String nosPackages;

	@Column(name = "marking_det_qty")
	private String markingDetQty;

	@Column(name = "marking_det_encl")
	private String markingDetEncl;

	@Column(name = "firms_cert_as_encl")
	private String firmsCertAsEncl;

	@Column(name = "bond_det_encl")
	private String bondDetEncl;

	@Column(name = "smp_drawn_encl")
	private String smpDrawnEncl;

	@Column(name = "short_cmg_encl")
	private String shortCmgEncl;

	@Column(name = "rsns_for_delay")
	private String rsnsForDelay;

	@Column(name = "regdsl_no")
	private String regdslNo;

	@Column(name = "regdsl_grp")
	private String regdslGrp;

	@Column(name = "regdsl_month")
	private String regdslMonth;

	@Column(name = "insp_sign")
	private String inspSign;

	@Column(name = "insp_name")
	private String inspName;

	@Column(name = "insp_desig")
	private String inspDesig;

	@Column(name = "insp_mark_det")
	private String inspMarkDet;

	@Column(name = "date_of_insp")
	private String dateOfInsp;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="submitted_date", nullable=false)
	private Date submittedDate;
	
	@ManyToOne
	@JoinColumn(name = "submitted_by")
    private Employee submittedBy;

}
