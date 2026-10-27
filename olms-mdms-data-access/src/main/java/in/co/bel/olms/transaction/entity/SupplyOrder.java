package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Account;
import in.co.bel.olms.master.entity.Consignee;
import in.co.bel.olms.master.entity.Employee;
import in.co.bel.olms.master.entity.INoteType;
import in.co.bel.olms.master.entity.Indenter;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.SalesGroup;
import in.co.bel.olms.master.entity.SoStatus;
import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.entity.Supplier;
import in.co.bel.olms.transaction.entity.check.ScrutinySo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * SupplyOrder entity class
 *
 * @author Awadhesh Verma
 * @author Manasa B
 * @version 1.0
 * @since 2023-04-08
 */

@Entity
@Table(name = "supply_order", schema = "tdms")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SupplyOrder implements Serializable {

	private static final long serialVersionUID = 1940072711511468897L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supply_order_id_seq")
	@SequenceGenerator(name = "supply_order_id_seq", schema = "tdms", sequenceName = "supply_order_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sales_grp_id")
	private SalesGroup salesGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indenter_id")
	private Indenter indenter;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "so_consignee_mapping", schema = "tdms", joinColumns = @JoinColumn(name = "supply_order_id"), inverseJoinColumns = @JoinColumn(name = "consignee_id"))
	private List<Consignee> soConsigneeMappings;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id")
	private Organization organization;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inote_type_id")
	private INoteType inoteType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_datetime", nullable = false)
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified_datetime")
	private Date modifiedDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "order_no", length = 250)
	private String orderNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "order_date")
	private Date orderDate;

	@Column(name = "description", length = 500)
	private String description;

	@Column(name = "no_of_items")
	private Integer noOfItems;

	@Column(name = "total_qty")
	private BigDecimal totalQty;

	// File no shall be generated automatically in the database
	@Column(name = "file_no", length = 25, insertable = false, updatable = true)
	private String fileNo;

	@Column(name = "value", precision = 2)
	private BigDecimal value;

	@Temporal(TemporalType.DATE)
	@Column(name = "dd")
	private Date dd;

	@Column(name = "lot_no", length = 2)
	private Integer lotNo;
	
	@Column(name = "indent_no")
	private String indentNo;

	@Temporal(TemporalType.DATE)
	@Column(name = "indent_date")
	private Date indentDate;

	@Column(name = "so_amndmnt_remarks")
	private String soAmndmntRemarks;

	@Column(name = "remarks")
	private String remarks;

	@Temporal(TemporalType.DATE)
	@Column(name = "order_complt_date")
	private Date orderCompletionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "so_type_id")
	private SoType soType;

	@Column(name = "eqpt_group_id")
	private Integer eqptGroup;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "so_status_id")
	private SoStatus soStatus;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "supplyOrder")
	private List<Project> projects;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acct_id")
	private Account accountType;

	@Temporal(TemporalType.DATE)
	@Column(name = "received_date")
	private Date receiptDate;

	@Column(name = "status")
	private String status;
	
	@Column(name="is_notified")
	private Boolean isNotified;
	
	@Column(name = "is_gcc")
	private Boolean isGcc;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cqao_id")
	private Employee cqao;

	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SOLineItem> soLineItems;

	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SupplyOrderAttachment> attachments;

	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<SoApprovalDetails> soApprovalDetails;

	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ItemLots> itemLots;
	
	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DdExtension> ddExtensions;
	
	@JsonIgnore
	@OneToMany(mappedBy = "supplyOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AmendmentsLetter> amendmentsLetters;
	
	@JsonIgnore
	@OneToOne(mappedBy = "supplyOrder", fetch = FetchType.LAZY)
	private Fcl fcl;
	
	@JsonIgnore
	@OneToOne(mappedBy = "supplyOrder", fetch = FetchType.LAZY)
	private Fcm fcm;
	
	@JsonIgnore
	@OneToOne(mappedBy = "supplyOrder", fetch = FetchType.LAZY)
	private ScrutinySo scrutinySo;
	
	
	@OneToOne(mappedBy = "supplyOrder", fetch = FetchType.LAZY)
	private FileClosingForm fileClosingForm;

	@Column(name = "is_registration_no")
	private Boolean isRegistrationNo;
	
	@Column(name = "drawing_no")
	private String drawingNo;
	
	@Column(name = "reference_no")
	private String referenceNo;
	
	@Column(name = "is_active")
	private Boolean isActive;
	
	@Column(name = "is_del")
    private Boolean isDel;

    @Column(name = "del_by")
    private Integer delBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "del_datetime")
    private Date delDatetime;
}
