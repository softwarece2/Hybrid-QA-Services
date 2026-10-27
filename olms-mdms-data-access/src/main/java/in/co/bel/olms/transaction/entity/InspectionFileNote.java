package in.co.bel.olms.transaction.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="inspection_note", schema="tdms")
public class InspectionFileNote implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOfferLetterId() {
		return offerLetterId;
	}

	public void setOfferLetterId(Integer offerLetterId) {
		this.offerLetterId = offerLetterId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDatetime() {
		return createdDatetime;
	}

	public void setCreatedDatetime(Date createdDatetime) {
		this.createdDatetime = createdDatetime;
	}

	public String getOlNumber() {
		return olNumber;
	}

	public void setOlNumber(String olNumber) {
		this.olNumber = olNumber;
	}

	public Date getOlDate() {
		return olDate;
	}

	public void setOlDate(Date olDate) {
		this.olDate = olDate;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getNoteNo() {
		return noteNo;
	}

	public void setNoteNo(String noteNo) {
		this.noteNo = noteNo;
	}

	public String getSupplyOrderNo() {
		return supplyOrderNo;
	}

	public void setSupplyOrderNo(String supplyOrderNo) {
		this.supplyOrderNo = supplyOrderNo;
	}

	public Date getSupplyOrderDate() {
		return supplyOrderDate;
	}

	public void setSupplyOrderDate(Date supplyOrderDate) {
		this.supplyOrderDate = supplyOrderDate;
	}

	public Date getOriginalDeliverydate() {
		return originalDeliverydate;
	}

	public void setOriginalDeliverydate(Date originalDeliverydate) {
		this.originalDeliverydate = originalDeliverydate;
	}

	public String getTotalLineItemsPo() {
		return totalLineItemsPo;
	}

	public void setTotalLineItemsPo(String totalLineItemsPo) {
		this.totalLineItemsPo = totalLineItemsPo;
	}

	public String getTotalValueLineItemsPo() {
		return totalValueLineItemsPo;
	}

	public void setTotalValueLineItemsPo(String totalValueLineItemsPo) {
		this.totalValueLineItemsPo = totalValueLineItemsPo;
	}

	public String getTotalInotesCcIssued() {
		return totalInotesCcIssued;
	}

	public void setTotalInotesCcIssued(String totalInotesCcIssued) {
		this.totalInotesCcIssued = totalInotesCcIssued;
	}

	public String getCompletedLotSize() {
		return completedLotSize;
	}

	public void setCompletedLotSize(String completedLotSize) {
		this.completedLotSize = completedLotSize;
	}

	public String getNoLineItemsSupplied() {
		return noLineItemsSupplied;
	}

	public void setNoLineItemsSupplied(String noLineItemsSupplied) {
		this.noLineItemsSupplied = noLineItemsSupplied;
	}

	public String getNoLineItemsOl() {
		return noLineItemsOl;
	}

	public void setNoLineItemsOl(String noLineItemsOl) {
		this.noLineItemsOl = noLineItemsOl;
	}

	public String getValueOfferedOl() {
		return valueOfferedOl;
	}

	public void setValueOfferedOl(String valueOfferedOl) {
		this.valueOfferedOl = valueOfferedOl;
	}

	public String getNoLineItemsClrdOl() {
		return noLineItemsClrdOl;
	}

	public void setNoLineItemsClrdOl(String noLineItemsClrdOl) {
		this.noLineItemsClrdOl = noLineItemsClrdOl;
	}

	public String getValueClrdOl() {
		return valueClrdOl;
	}

	public void setValueClrdOl(String valueClrdOl) {
		this.valueClrdOl = valueClrdOl;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public String getInspectorDesignation() {
		return inspectorDesignation;
	}

	public void setInspectorDesignation(String inspectorDesignation) {
		this.inspectorDesignation = inspectorDesignation;
	}

	public String getQapRef() {
		return qapRef;
	}

	public void setQapRef(String qapRef) {
		this.qapRef = qapRef;
	}

	public Boolean getInspDocVerify() {
		return inspDocVerify;
	}

	public void setInspDocVerify(Boolean inspDocVerify) {
		this.inspDocVerify = inspDocVerify;
	}

	public Boolean getInspEt() {
		return inspEt;
	}

	public void setInspEt(Boolean inspEt) {
		this.inspEt = inspEt;
	}

	public Boolean getInspElec() {
		return inspElec;
	}

	public void setInspElec(Boolean inspElec) {
		this.inspElec = inspElec;
	}

	public Boolean getInspFats() {
		return inspFats;
	}

	public void setInspFats(Boolean inspFats) {
		this.inspFats = inspFats;
	}

	public Boolean getInspPdi() {
		return inspPdi;
	}

	public void setInspPdi(Boolean inspPdi) {
		this.inspPdi = inspPdi;
	}

	public Boolean getInspPhysical() {
		return inspPhysical;
	}

	public void setInspPhysical(Boolean inspPhysical) {
		this.inspPhysical = inspPhysical;
	}

	public Boolean getInspEss() {
		return inspEss;
	}

	public void setInspEss(Boolean inspEss) {
		this.inspEss = inspEss;
	}

	public Boolean getInspEmiEmc() {
		return inspEmiEmc;
	}

	public void setInspEmiEmc(Boolean inspEmiEmc) {
		this.inspEmiEmc = inspEmiEmc;
	}

	public Boolean getInspEndurance() {
		return inspEndurance;
	}

	public void setInspEndurance(Boolean inspEndurance) {
		this.inspEndurance = inspEndurance;
	}

	public Date getInspectionStartDate() {
		return inspectionStartDate;
	}

	public void setInspectionStartDate(Date inspectionStartDate) {
		this.inspectionStartDate = inspectionStartDate;
	}

	public Date getInspectionCompleteDate() {
		return inspectionCompleteDate;
	}

	public void setInspectionCompleteDate(Date inspectionCompleteDate) {
		this.inspectionCompleteDate = inspectionCompleteDate;
	}

	public String getInspectorRemrks() {
		return inspectorRemrks;
	}

	public void setInspectorRemrks(String inspectorRemrks) {
		this.inspectorRemrks = inspectorRemrks;
	}

	public String getAcqaoRemarks() {
		return acqaoRemarks;
	}

	public void setAcqaoRemarks(String acqaoRemarks) {
		this.acqaoRemarks = acqaoRemarks;
	}

	public String getDcqaoRemarks() {
		return dcqaoRemarks;
	}

	public void setDcqaoRemarks(String dcqaoRemarks) {
		this.dcqaoRemarks = dcqaoRemarks;
	}

	public String getJcqaoRemarks() {
		return jcqaoRemarks;
	}

	public void setJcqaoRemarks(String jcqaoRemarks) {
		this.jcqaoRemarks = jcqaoRemarks;
	}

	public Date getExtendedDd1() {
		return extendedDd1;
	}

	public void setExtendedDd1(Date extendedDd1) {
		this.extendedDd1 = extendedDd1;
	}

	public Date getExtendedDd2() {
		return extendedDd2;
	}

	public void setExtendedDd2(Date extendedDd2) {
		this.extendedDd2 = extendedDd2;
	}

	public Boolean getIsFormalDd1() {
		return isFormalDd1;
	}

	public void setIsFormalDd1(Boolean isFormalDd1) {
		this.isFormalDd1 = isFormalDd1;
	}

	public Boolean getIsFormalDd2() {
		return isFormalDd2;
	}

	public void setIsFormalDd2(Boolean isFormalDd2) {
		this.isFormalDd2 = isFormalDd2;
	}

	public String getLatestDdExtendedVide() {
		return latestDdExtendedVide;
	}

	public void setLatestDdExtendedVide(String latestDdExtendedVide) {
		this.latestDdExtendedVide = latestDdExtendedVide;
	}

	public String getLotSizeOriginal() {
		return lotSizeOriginal;
	}

	public void setLotSizeOriginal(String lotSizeOriginal) {
		this.lotSizeOriginal = lotSizeOriginal;
	}

	public String getLotSizeAmmended1() {
		return lotSizeAmmended1;
	}

	public void setLotSizeAmmended1(String lotSizeAmmended1) {
		this.lotSizeAmmended1 = lotSizeAmmended1;
	}

	public String getLotSizeAmmended2() {
		return lotSizeAmmended2;
	}

	public void setLotSizeAmmended2(String lotSizeAmmended2) {
		this.lotSizeAmmended2 = lotSizeAmmended2;
	}

	public String getTypeOfDelivery() {
		return typeOfDelivery;
	}

	public void setTypeOfDelivery(String typeOfDelivery) {
		this.typeOfDelivery = typeOfDelivery;
	}

	public Date getCqaoApprovalDate() {
		return cqaoApprovalDate;
	}

	public void setCqaoApprovalDate(Date cqaoApprovalDate) {
		this.cqaoApprovalDate = cqaoApprovalDate;
	}

	public Integer getSupplyOrderId() {
		return supplyOrderId;
	}

	public void setSupplyOrderId(Integer supplyOrderId) {
		this.supplyOrderId = supplyOrderId;
	}

	public List<OfferLetter> getOfferLetters() {
		return offerLetters;
	}

	public void setOfferLetters(List<OfferLetter> offerLetters) {
		this.offerLetters = offerLetters;
	}

	public Set<OlApprovalDetails> getOlApprovalDetails() {
		return olApprovalDetails;
	}

	public void setOlApprovalDetails(Set<OlApprovalDetails> olApprovalDetails) {
		this.olApprovalDetails = olApprovalDetails;
	}

	public String getOlDescription() {
		return olDescription;
	}

	public void setOlDescription(String olDescription) {
		this.olDescription = olDescription;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inspection_note_id_seq")
	@SequenceGenerator(name = "inspection_note_id_seq", schema = "tdms", sequenceName = "inspection_note_id_seq", allocationSize = 1)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	@Column(name="offer_letter_id")
	private Integer offerLetterId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_datetime")
	private Date createdDatetime;
	
	@Column(name="ol_number")
	private String olNumber;
	
	@Column(name="ol_date")
	private Date olDate;
	
	@Column(name="project_decription")
	private String projectDescription;
	
	@Column(name="file_number")
	private String fileNumber;
	
	@Column(name="note_no")
	private String noteNo;
	
	@Column(name="supply_order_no")
	private String supplyOrderNo;
	
	@Column(name="supply_order_date")
	private Date supplyOrderDate;
	
	@Column(name="original_delivery_date")
	private Date originalDeliverydate;
	
	@Column(name="total_line_items_po")
	private String totalLineItemsPo;
	
	@Column(name="total_value_line_items_po")
	private String totalValueLineItemsPo;
	
	@Column(name="total_inotes_cc_issued")
	private String totalInotesCcIssued;
	
	@Column(name="completed_lot_size")
	private String completedLotSize;
	
	@Column(name="no_line_items_supplied")
	private String noLineItemsSupplied;
	
	@Column(name="no_line_items_ol")
	private String noLineItemsOl;	
	
	@Column(name="value_offered_ol")
	private String valueOfferedOl;
	
	@Column(name="no_line_items_clrd_ol")
	private String noLineItemsClrdOl;
	
	@Column(name="value_clrd_ol")
	private String valueClrdOl;
	
	@Column(name="inpector_name")
	private String inspectorName;
	
	@Column(name="inspector_designation")
	private String inspectorDesignation;
	
	@Column(name="qap_ref")
	private String qapRef;
	
	@Column(name="insp_doc_verify")
	private Boolean inspDocVerify;

	@Column(name="insp_et")
	private Boolean inspEt;	
	
	@Column(name="insp_elec")
	private Boolean inspElec;	
	
	@Column(name="insp_fats")
	private Boolean inspFats;
	
	@Column(name="insp_pdi")
	private Boolean inspPdi;
	
	@Column(name="insp_physical")
	private Boolean inspPhysical;

	@Column(name="insp_ess")
	private Boolean inspEss;
	
	@Column(name="insp_emi_emc")
	private Boolean inspEmiEmc;
	
	@Column(name="insp_endurance")
	private Boolean inspEndurance;
	
	@Column(name="inspection_start_date")
	private Date inspectionStartDate;
	
	@Column(name="inspection_complete_date")
	private Date inspectionCompleteDate;
	
	@Column(name="inspector_remarks")
	private String inspectorRemrks;
	
	@Column(name="acqao_remarks")
	private String acqaoRemarks;
	
	@Column(name="dcqao_remarks")
	private String dcqaoRemarks;
	
	@Column(name="jcqao_remarks")
	private String jcqaoRemarks;
	
	@Column(name="extended_dd1")
	private Date extendedDd1;
	
	@Column(name="extended_dd2")
	private Date extendedDd2;
	
	@Column(name="is_formal_dd1")
	private Boolean isFormalDd1;
	
	@Column(name="is_formal_dd2")
	private Boolean isFormalDd2;

	@Column(name="latest_dd_extended_vide")
	private String latestDdExtendedVide;
	
	@Column(name="lot_size_original")
	private String lotSizeOriginal;
	
	@Column(name="lot_size_ammended1")
	private String lotSizeAmmended1;
	
	@Column(name="lot_size_ammended2")
	private String lotSizeAmmended2;
	
	@Column(name="type_of_delivery")
	private String typeOfDelivery;
	
	@Temporal(TemporalType.DATE)
	@Column(name="cqao_approval_date")
	private Date cqaoApprovalDate;
	
	@Column(name="supply_order_id")
	private Integer supplyOrderId;
	
	@Column(name="is_submitted")
	private Boolean isSubmitted;
	
	@Column(name="ol_description")
	private String olDescription;

	public Boolean getIsSubmitted() {
		return isSubmitted;
	}

	public void setIsSubmitted(Boolean isSubmitted) {
		this.isSubmitted = isSubmitted;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "file_note_ol_mapping", schema = "tdms", joinColumns = @JoinColumn(name = "file_note_id"), inverseJoinColumns = @JoinColumn(name = "offer_letter_id"))
	private List<OfferLetter> offerLetters;
	
	@JsonIgnore
	@OneToMany(mappedBy = "inspectionFileNote")
	private Set<OlApprovalDetails> olApprovalDetails;

}
