package in.co.bel.olms.transaction.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.INoteStatus;
import in.co.bel.olms.master.entity.INoteType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="internal_inote", schema="tdms")
public class InternalINote {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "internal_inote_id_seq")
    @SequenceGenerator(name = "internal_inote_id_seq", schema = "tdms", sequenceName = "internal_inote_id_seq", allocationSize = 1)
    @Column(name="id", unique = true, nullable = false)
    private Integer id;

    @Column(name="created_by")
    private Integer createdBy;

    @Column(name="supply_order_no")
    private String supplyOrderNo;

    @Column(name="supply_order_date")
    private Date supplyOrderDate;

    @Column(name="contractor_name")
    private String contractorName;

    @Column(name="contractor_address")
    private String contractorAddress;

    @Column(name="indentor")
    private String indentor;

    @Column(name="indent_no")
    private String indentNo;

    @Column(name="indent_date")
    private Date indentDate;

    @Column(name="consignee")
    private String consignee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="inspection_start")
    private Date inspectionStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="inspection_end")
    private Date inspectionEnd;

    @Column(name="sub_slip_id")
    private Integer olId;

    @Column(name="sub_slip_no")
    private String offerLetterNo;

    @Column(name="sub_slip_date")
    private Date offerLetterDate;

    @Column(name="org_name")
    private String orgName;

    @Column(name="remarks")
    private String remarks;

    @Column(name="emp_name")
    private String empName;

    @Column(name="emp_designation")
    private String empDesigation;

    @Column(name="emp_role")
    private String empRole;

    @Column(name="emp_name_hindi")
    private String empNameHindi;

    @Column(name="emp_designation_hindi")
    private String empDesignationHindi;

    @Column(name="emp_role_hindi")
    private String emproleHindi;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_datetime")
    private Date createdDatetime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modified_datetime")
    private Date modifiedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="inote_type_id")
    private INoteType inoteType;

    @Column(name="description")
    private String description;

    @Column(name="inote_no")
    private String iNoteNo;


    @Column(name="prev_inote_no")
    private String prevINoteNo;


    @Column(name="prev_inote_date")
    private Date prevINoteDate;

    @Temporal(TemporalType.DATE)
    @Column(name="inote_date")
    private Date inoteDate;

    @Column(name="cqao_remarks")
    private String cqaoRemarks;

    @Column(name="amendment")
    private String amendment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="new_dd")
    private Date newDd;

    @Column(name="inspection_place")
    private String inspectionPlace;

    @Column(name="supplied_within_dd", length = 20)
    private String suppliedWithinDd;

    @Column(name="supplied_within_ext_dd", length = 20)
    private String suppliedWithinExtDd;

    @Column(name="supplied_under_franking", length = 20)
    private String suppliedUnderFranking;

    @Column(name="penalty_letter_no", length = 20)
    private String penaltyLetterNo;

    @Column(name="dispatch_mode", length = 20)
    private String dispatchMode;

    @Column(name="verified_by", length = 50)
    private String verifiedBy;

    @Column(name="inspected_by", length = 50)
    private String inspectedBy;

    @Column(name="station", length = 30)
    private String station;

    @Column(name="debit_head", length = 50)
    private String debitHead;

    @Column(name="warranty", length = 100)
    private String warranty;

    @Column(name="rejection_voucher", length = 50)
    private String rejectionVoucher;

    @Column(name="rejection_on", length = 50)
    private String rejectionOn;

    @Column(name="adv_no_date", length = 50)
    private String advNoDate;

    @Column(name="inspection_agency")
    private String inspectionAgency;
    
    @Column(name="delivery_date")
    private String deliveryDate;

    // bi-directional many-to-one association to INoteLineItem table
    // header
    @JsonIgnore
    @OneToMany(mappedBy = "iNote")
    private List<InternalINoteLineItem> iNoteLineItems;
    
    @Column(name="sample_qty")
    private Integer sampleQty;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_datetime")
    private Date submittedDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_by")
    private Employee submittedBy;  
    
	@JsonIgnore
	@OneToMany(mappedBy = "internalInote")
	private Set<InternalInoteApprovalDetails > olApprovalDetails;
    
    @Column(name="project_details")
	private String projectDetails;
    
    @Column(name="inote_type_name")
	private String inoteTypeName;
    
    @Column(name="warranty_no")
	private String warrantyNo;
    
    @Column(name="work_order_no")
	private String workOrderNo;
    
    @Column(name="rf_est_no")
	private String rfEstNo;
    
    @Column(name="ep_code")
	private String epCode;
    
    @Column(name="ordered_qty")
	private BigDecimal orderedQty;
    
    @Column(name="cleared_qty")
	private BigDecimal clearedQty;
        
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merge_sslip_id")
    private MergeSubSlip mergeSubSlip;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_letter_id")
    private OfferLetter offerLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private INoteStatus inoteStatusId;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
}
