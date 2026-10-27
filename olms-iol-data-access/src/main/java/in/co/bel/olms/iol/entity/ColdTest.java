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
import in.co.bel.olms.transaction.entity.LineItemApplicableTest;
import in.co.bel.olms.transaction.entity.OlLineItem;
import in.co.bel.olms.transaction.entity.SubSlipLineItem;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cold_test", schema = "iol")
public class ColdTest implements Serializable {

	private static final long serialVersionUID = 9223372036854775807L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cold_test_id_sq")
	@SequenceGenerator(name = "cold_test_id_sq", schema = "iol", sequenceName = "cold_test_id_sq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;

	@Column(name = "test_type")
	private Integer testType;
	
	@Column(name = "hot_cold")
	private Integer hotCold;
	
//	@Column(name = "device_no")
//	private String deviceNo;
	
	@Column(name = "test_date")
	private String testDate;
	
	@Column(name = "volt")
	private Double volt;

	@Column(name = "ph_curr_r")
	private Double phCurrR;

	@Column(name = "ph_curr_s")
	private Double phCurrS;

	@Column(name = "ph_curr_t")
	private Double phCurrT;

	@Column(name = "ocular_heating")
	private String ocularHeating;

	@Column(name = "commander")
	private String commander;

	@Column(name = "auto_man")
	private String autoMan;

	@Column(name = "rc_time")
	private Double rcTime;

	@Column(name = "rc_time_bid")
	private String rcTimeBid;

	@Column(name = "rc_time_ds")
	private Integer rcTimeDs;

	@Column(name = "work_off_d_time")
	private Double workOffDTime;

	@Column(name = "work_off_d_bid")
	private String workOffDBid;

	@Column(name = "work_off_d_ds")
	private Integer workOffDDs;

	@Column(name = "failure_single")
	private String failureSingle;

	@Column(name = "failure_single_bid")
	private String failureSingleBid;

	@Column(name = "failure_single_ds")
	private String failureSingleDs;

	@Column(name = "mutli_arresting")
	private Double mutliArresting;

	@Column(name = "signal_rotary_trans")
	private Double signalRotaryTrans;

	@Column(name = "fire_contact")
	private String fireContact;

	@Column(name = "fire_contact_bid")
	private String fireContactBid;

	@Column(name = "fire_contact_ds")
	private String fireContactDs;

	@Column(name = "compensation")
	private Double compensation;

	@Column(name = "comp_mg_fire")
	private String compMgFire;

	@Column(name = "volt_soft_clock_p1")
	private Double voltSoftClockP1;

	@Column(name = "volt_soft_anti_clock_p1")
	private Double voltSoftAntiClockP1;

	@Column(name = "volt_soft_clock_p2")
	private Double voltSoftClockP2;

	@Column(name = "volt_soft_anti_clock_p2")
	private Double voltSoftAntiClockP2;

	@Column(name = "volt_hard_clock_p1")
	private Double voltHardClockP1;

	@Column(name = "volt_hard_anti_clock_p1")
	private Double voltHardAntiClockP1;

	@Column(name = "volt_hard_clock_p2")
	private Double voltHardClockP2;

	@Column(name = "volt_hard_anti_clock_p2")
	private Double voltHardAntiClockP2;

	@Column(name = "volt_zero_clock_p1")
	private Double voltZeroClockP1;

	@Column(name = "volt_zero_anti_clock_p1")
	private Double voltZeroAntiClockP1;

	@Column(name = "safe_cont_pt_5v")
	private String safeContPt5v;

	@Column(name = "safe_cont_pt_9v")
	private String safeContPt9v;

	@Column(name = "blocking")
	private String blocking;

	@Column(name = "max_upw_curr_speed")
	private Double maxUpwCurrSpeed;

	@Column(name = "max_upw_curr_p1")
	private Double maxUpwCurrP1;

	@Column(name = "max_down_curr_speed")
	private Double maxDownCurrSpeed;

	@Column(name = "max_down_curr_p2")
	private Double maxDownCurrP2;

	@Column(name = "min_aim_speed_up")
	private Double minAimSpeedUp;

	@Column(name = "min_aim_speed_down")
	private Double minAimSpeedDown;

	@Column(name = "eveness_sight_up")
	private Integer evenessSightUp;

	@Column(name = "eveness_sight_down")
	private Integer evenessSightDown;

	@Column(name = "zero_setting")
	private String zeroSetting;

	@Column(name = "mech_sp_9v_forw")
	private Double mechSp9vForw;

	@Column(name = "mech_sp_9v_backw")
	private Double mechSp9vBackw;

	@Column(name = "mech_sp_14v_forw")
	private Double mechSp14vForw;

	@Column(name = "mech_sp_14v_backw")
	private Double mechSp14vBackw;

	@Column(name = "mech_sp_ph_curr_r")
	private Double mechSpPhCurrR;

	@Column(name = "mech_sp_ph_curr_s")
	private Double mechSpPhCurrS;

	@Column(name = "mech_sp_ph_curr_t")
	private Double mechSpPhCurrT;

	@Column(name = "drift_speed_pl_0_deg")
	private Double driftSpeedPl0Deg;

	@Column(name = "drift_speed_mi_5_deg")
	private Double driftSpeedMi5Deg;

	@Column(name = "drift_speed_pl_15_deg")
	private Double driftSpeedPl15Deg;

	@Column(name = "illumination")
	private String illumination;

	@Column(name = "proj_sp_apds_hef")
	private Double projSpApdsHef;

	@Column(name = "proj_sp_apds_heat")
	private Double projSpApdsHeat;

	@Column(name = "proj_sp_hef_heat")
	private Double projSpHefHeat;

	@Column(name = "proj_sp_heat_hef")
	private Double projSpHeatHef;

	@Column(name = "proj_sp_heat_apds")
	private Double projSpHeatApds;

	@Column(name = "proj_sp_hef_apds")
	private Double projSpHefApds;

	@Column(name = "parallelism")
	private Integer parallelism;

	@Column(name = "torq_handwheel")
	private String torqHandwheel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicable_test_id")
	private LineItemApplicableTest lineItemApplicableTest;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ol_line_item_id")
	private OlLineItem olLineItem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sslip_line_item_id")
	private SubSlipLineItem subSlipLineItem;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "status")
	private Integer status;
		
	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime")
	private Date createdDatetime;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insp_round_id", nullable = false)
	private InspectionRound inspectionRound;
	
}
