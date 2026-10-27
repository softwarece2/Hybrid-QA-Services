package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.co.bel.olms.master.entity.Department;
import in.co.bel.olms.master.entity.DurationType;
import in.co.bel.olms.master.entity.ItemCategory;
import in.co.bel.olms.master.entity.OrderType;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.PartNumber;
import in.co.bel.olms.master.entity.RegistrationNoType;
import in.co.bel.olms.master.entity.SoType;
import in.co.bel.olms.master.entity.Uom;
import in.co.bel.olms.master.entity.WarrantType;
import in.co.bel.olms.master.entity.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "so_line_item", schema = "tdms")
public class SOLineItem implements Serializable {

	private static final long serialVersionUID = 1438251916851487616L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "so_line_item_seq_id")
	@SequenceGenerator(name = "so_line_item_seq_id", schema = "tdms", sequenceName = "so_line_item_seq_id", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "line_item_no", length = 100)
	private String lineItemNo;

	@Column(name = "created_datetime", nullable = false)
	private Date createdDateTime;

	@Column(name = "created_by", nullable = false)
	private Integer createdBy;

	@Column(name = "modified_datetime")
	private Date modifiedDateTime;

	@Column(name = "modified_by")
	private Integer modifiedBy;

	@Column(name = "description", length = 300)
	private String description;

	@Column(name = "part_no", length = 20)
	private String partNo;
	
	private String drawingNo;
	
	private Date deliveryDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "insp_auth")
	private Organization inspectionAuthorityId;

	
	@Column(name = "unit_cost")
	private BigDecimal unitCost;

	@Column(name = "order_qty")
	private BigDecimal orderQty;

	@Column(name = "item_value")
	private BigDecimal itemValue;
	
	
	@Column(name = "cat_part_no")
	private String catPartNo;
	
	@Column(name = "nsn_part_no")
	private String nsnPartNo;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "so_id")
	private SupplyOrder supplyOrder;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_order_id")
	private SaleOrder saleOrder;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_id")
	private Uom uom;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_category_id")
	private ItemCategory itemCategory;

	@Column(name = "is_under_warranty")
	private Boolean isUnderWarranty;

	@Column(name = "duration")
	private Integer duration;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "duration_type_id")
	private DurationType durationType;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "part_number_id")
	private PartNumber partNumber;
	
	@Column(name = "tmr_file_name")
	private String tmrFileName;
	
	@Column(name = "remarks", length = 100)
	private String remarks;
	
	@JsonIgnore
	@OneToMany(mappedBy = "soLineItem", fetch = FetchType.LAZY)
	private List<OlLineItem> olLineItems;
	

	@Column(name = "is_gem")
	private Boolean isGem;
	
	
	@Column(name = "is_gcs")
	private Boolean isGcs;

	@Column(name = "is_self_certified")
	private Boolean isSelfCertified;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "gem")
	private String gem;
	

	@Column(name = "gcs")
	private String gcs;
	
	@Column(name = "self_certification")
	private String selfCertification;
	

	@Column(name = "status")
	private String status;

	@Column(name = "is_imported")
	private Boolean isImported;
	
	private String refNo;
	
	private String orderMasterNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_type_id")
	private OrderType orderTypeId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplyType")
	private SoType supplyType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "registration_no")
	private RegistrationNoType registrationNo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status statusId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dept_id")
	private Department deptId;
	
	private String epCode;
//    private String supplyType;
    
//    private Integer RegistrationNo;
    private Integer lotNo;
    private String orderNo;
    private Date orderDate;
    private String orderTypeNo;
    private Date orderTypeDate;
	
	@Column(name = "warr_period_qty")
	private Integer warrPeriodQty;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn (name = "warr_period_type")
	private WarrantType warrPeriodType;
    
	public Integer getId() {
		return id;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}

	public Integer getModifiedBy() {
		return modifiedBy;
	}

	public String getDescription() {
		return description;
	}

	public String getPartNo() {
		return partNo;
	}

	public BigDecimal getUnitCost() {
		return unitCost;
	}

	public BigDecimal getOrderQty() {
		return orderQty;
	}

	public BigDecimal getItemValue() {
		return itemValue;
	}
	
	public String getDrawingNo() {
		return drawingNo;
	}
	
	public Organization getInspectionAuthorityId() {
		return inspectionAuthorityId;
	}
	
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setDrawingNo(String drawingNo) {
		this.drawingNo = drawingNo;
	}
	
	public void setInspectionAuthorityId(Organization inspectionAuthorityId) {
		this.inspectionAuthorityId = inspectionAuthorityId ;
	}
	
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}

	public void setModifiedBy(Integer modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPartNo(String partNo) {
		this.partNo = partNo;
	}

	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

	public void setOrderQty(BigDecimal orderQty) {
		this.orderQty = orderQty;
	}

	public void setItemValue(BigDecimal itemValue) {
		this.itemValue = itemValue;
	}

	public String getLineItemNo() {
		return lineItemNo;
	}

	public void setLineItemNo(String lineItemNo) {
		this.lineItemNo = lineItemNo;
	}

	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}

	public Uom getUom() {
		return uom;
	}

	public void setUom(Uom uom) {
		this.uom = uom;
	}

	public ItemCategory getItemCategory() {
		return itemCategory;
	}

	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	public PartNumber getPartNumber() {
		return partNumber;
	}
	
	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}
	
	public Boolean getIsUnderWarranty() {
		return isUnderWarranty;
	}

	public void setIsUnderWarranty(Boolean isUnderWarranty) {
		this.isUnderWarranty = isUnderWarranty;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public DurationType getDurationType() {
		return durationType;
	}

	public void setDurationType(DurationType durationType) {
		this.durationType = durationType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SaleOrder getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(SaleOrder saleOrder) {
		this.saleOrder = saleOrder;
	}

	public String getGem() {
		return gem;
	}

	public void setGem(String gem) {
		this.gem = gem;
	}

	public String getGcs() {
		return gcs;
	}

	public void setGcs(String gcs) {
		this.gcs = gcs;
	}

	public String getSelfCertification() {
		return selfCertification;
	}

	public void setSelfCertification(String selfCertification) {
		this.selfCertification = selfCertification;
	}

	public String getTmrFileName() {
		return tmrFileName;
	}

	public void setTmrFileName(String tmrFileName) {
		this.tmrFileName = tmrFileName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<OlLineItem> getOlLineItems() {
		return olLineItems;
	}

	public void setOlLineItems(List<OlLineItem> olLineItems) {
		this.olLineItems = olLineItems;
	}

	public Boolean getImported() {
		return isImported;
	}

	public void setImported(Boolean imported) {
		isImported = imported;
	}
	public Boolean getIsImported() {
		return isImported;
	}

	public void setIsImported(Boolean isImported) {
		this.isImported = isImported;
	}
	public String getCatPartNo() {
		return catPartNo;
	}

	public void setCatPartNo(String catPartNo) {
		this.catPartNo = catPartNo;
	}

	public String getNsnPartNo() {
		return nsnPartNo;
	}

	public void setNsnPartNo(String nsnPartNo) {
		this.nsnPartNo = nsnPartNo;
	}
	
	public Boolean getIsGem() {
		return isGem;
	}
	
	public void setIsGem(Boolean isGem) {
		this.isGem = isGem;
	}

	public Boolean getIsGcs() {
		return isGcs;
	}

	public void setIsGcs(Boolean isGcs) {
		this.isGcs = isGcs;
	}

	public Boolean getIsSelfCertified() {
		return isSelfCertified;
	}

	public void setIsSelfCertified(Boolean isSelfCertified) {
		this.isSelfCertified = isSelfCertified;
	}
//	public RegistrationNoType getRegistrationNo() {
//	    return registrationNo;
//	}
//	public void setRegistrationNo(RegistrationNoType registrationNo) {
//	    this.registrationNo = registrationNo;
//	}


}
