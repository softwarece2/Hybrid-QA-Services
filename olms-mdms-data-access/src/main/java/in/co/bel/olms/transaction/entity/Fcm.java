package in.co.bel.olms.transaction.entity;

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

/**
 * 
 * 
 * @author Manasa B
 * @since 31-12-2022
 *
 */
@Entity
@Table(name = "fcm", schema = "tdms")
public class Fcm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2095065261404180916L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcm_id_seq")
	@SequenceGenerator(name = "fcm_id_seq", schema = "tdms", sequenceName = "fcm_id_seq", allocationSize = 1)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	
	@OneToOne
	@JoinColumn(name = "supply_order_id", nullable = false)	
	private SupplyOrder supplyOrder;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_of_receipt")
	private Date dateOfReceipt;
	
	@Column(name = "item_description", length = 500)
	private String itemDescription;
	
	@Column(name = "no_of_items")
	private String noOfItems;
	
	@Column(name = "opa", length = 50)
	private String opa;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "delivery_date")
	private Date deliveryDate;
	
	@Column(name = "no_of_lots")
	private String noOfLots;
	
	@Column(name = "approved_drg")
	private Boolean approvedDrg;
	
	@Column(name = "approved_qap")
	private Boolean approvedQap;
	
	@Column(name = "oem_tech_specs")
	private Boolean oemTechSpecs;
	
	@Column(name = "import_docs")
	private Boolean importDocs;
	
	@Column(name = "spec_inst")
	private Boolean specInst;
	
	@Column(name = "insp_discrepancy")
	private Boolean inspDiscrepancy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "new_drg_date")
	private Date newDrgDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "existing_drg_date")
	private Date existDrgDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fresh_qap_date")
	private Date freshQapDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "existing_qap_date")
	private Date existQapDate;
	
	@Column(name = "qt_test_applicable")
	private Boolean qtTestApplicable;
	
	@Column(name = "detail_bom")
	private Boolean detailBom;
	
	@Column(name = "approved_pil")
	private Boolean approvedPil;
	
	@Column(name = "bill_transport")
	private Boolean billTransport;
	
	@Column(name = "invoice_oem_origin_cert")
	private Boolean invoiceOemOriginCert;
	
	@Column(name = "bill_entry_warehouse")
	private Boolean billEntryWarehouse;
	
	@Column(name = "cert_of_confirmity")
	private Boolean certOfConfirmity;
	
	@Column(name = "warranty")
	private Boolean warranty;
	
	@Column(name = "melt_pouring", length = 500)
	private String meltPouring;
	
	@Column(name = "sample_test_nabl", length = 500)
	private String sampleTestNabl;
	
	@Column(name = "sub_order_det")
	private Boolean subOrderDet;

	@Temporal(TemporalType.DATE)
	@Column(name = "submitted_date")
	private Date submittedDate;
	
	@Column(name = "inpro_insp_pcb", length = 500)
	private String inproInspPcb;
	
	@Column(name = "inpro_insp_unit", length = 500)
	private String inproInspUnit;	
	
	@Column(name = "inpro_insp_npe", length = 500)
	private String inproInspNpe;
	
	@Column(name = "final_qt", length = 500)
	private String finalQt;
	
	@Column(name = "final_fats", length = 500)
	private String finalFats;
	
	@Column(name = "final_packing", length = 500)
	private String finalPacking;
	
	@Column(name = "final_clearance", length = 500)
	private String finalClearance;
	
	@Column(name = "final_vendor_rating", length = 500)
	private String finalVendorRating;
	
	@Column(name = "final_consignee", length = 500)
	private String finalConsignee;
	
	@Column(name = "others", length = 500)
	private String others;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "fcm")
	private List<FcmVendor> fcmVendors;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SupplyOrder getSupplyOrder() {
		return supplyOrder;
	}

	public void setSupplyOrder(SupplyOrder supplyOrder) {
		this.supplyOrder = supplyOrder;
	}

	public Date getDateOfReceipt() {
		return dateOfReceipt;
	}

	public void setDateOfReceipt(Date dateOfReceipt) {
		this.dateOfReceipt = dateOfReceipt;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getOpa() {
		return opa;
	}

	public void setOpa(String opa) {
		this.opa = opa;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Boolean getApprovedDrg() {
		return approvedDrg;
	}

	public void setApprovedDrg(Boolean approvedDrg) {
		this.approvedDrg = approvedDrg;
	}

	public Boolean getApprovedQap() {
		return approvedQap;
	}

	public void setApprovedQap(Boolean approvedQap) {
		this.approvedQap = approvedQap;
	}

	public Boolean getOemTechSpecs() {
		return oemTechSpecs;
	}

	public void setOemTechSpecs(Boolean oemTechSpecs) {
		this.oemTechSpecs = oemTechSpecs;
	}

	public Boolean getImportDocs() {
		return importDocs;
	}

	public void setImportDocs(Boolean importDocs) {
		this.importDocs = importDocs;
	}

	public Boolean getSpecInst() {
		return specInst;
	}

	public void setSpecInst(Boolean specInst) {
		this.specInst = specInst;
	}

	public Boolean getInspDiscrepancy() {
		return inspDiscrepancy;
	}

	public void setInspDiscrepancy(Boolean inspDiscrepancy) {
		this.inspDiscrepancy = inspDiscrepancy;
	}

	public Date getNewDrgDate() {
		return newDrgDate;
	}

	public void setNewDrgDate(Date newDrgDate) {
		this.newDrgDate = newDrgDate;
	}

	public Date getExistDrgDate() {
		return existDrgDate;
	}

	public void setExistDrgDate(Date existDrgDate) {
		this.existDrgDate = existDrgDate;
	}

	public Date getFreshQapDate() {
		return freshQapDate;
	}

	public void setFreshQapDate(Date freshQapDate) {
		this.freshQapDate = freshQapDate;
	}

	public Date getExistQapDate() {
		return existQapDate;
	}

	public void setExistQapDate(Date existQapDate) {
		this.existQapDate = existQapDate;
	}

	public Boolean getQtTestApplicable() {
		return qtTestApplicable;
	}

	public void setQtTestApplicable(Boolean qtTestApplicable) {
		this.qtTestApplicable = qtTestApplicable;
	}

	public Boolean getDetailBom() {
		return detailBom;
	}

	public void setDetailBom(Boolean detailBom) {
		this.detailBom = detailBom;
	}

	public Boolean getApprovedPil() {
		return approvedPil;
	}

	public void setApprovedPil(Boolean approvedPil) {
		this.approvedPil = approvedPil;
	}

	public Boolean getBillTransport() {
		return billTransport;
	}

	public void setBillTransport(Boolean billTransport) {
		this.billTransport = billTransport;
	}

	public Boolean getInvoiceOemOriginCert() {
		return invoiceOemOriginCert;
	}

	public void setInvoiceOemOriginCert(Boolean invoiceOemOriginCert) {
		this.invoiceOemOriginCert = invoiceOemOriginCert;
	}

	public Boolean getBillEntryWarehouse() {
		return billEntryWarehouse;
	}

	public void setBillEntryWarehouse(Boolean billEntryWarehouse) {
		this.billEntryWarehouse = billEntryWarehouse;
	}

	public Boolean getCertOfConfirmity() {
		return certOfConfirmity;
	}

	public void setCertOfConfirmity(Boolean certOfConfirmity) {
		this.certOfConfirmity = certOfConfirmity;
	}

	public Boolean getWarranty() {
		return warranty;
	}

	public void setWarranty(Boolean warranty) {
		this.warranty = warranty;
	}

	public String getMeltPouring() {
		return meltPouring;
	}

	public void setMeltPouring(String meltPouring) {
		this.meltPouring = meltPouring;
	}

	public String getSampleTestNabl() {
		return sampleTestNabl;
	}

	public void setSampleTestNabl(String sampleTestNabl) {
		this.sampleTestNabl = sampleTestNabl;
	}

	public Boolean getSubOrderDet() {
		return subOrderDet;
	}

	public void setSubOrderDet(Boolean subOrderDet) {
		this.subOrderDet = subOrderDet;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public List<FcmVendor> getFcmVendors() {
		return fcmVendors;
	}

	public void setFcmVendors(List<FcmVendor> fcmVendors) {
		this.fcmVendors = fcmVendors;
	}

	public String getInproInspPcb() {
		return inproInspPcb;
	}

	public void setInproInspPcb(String inproInspPcb) {
		this.inproInspPcb = inproInspPcb;
	}

	public String getInproInspUnit() {
		return inproInspUnit;
	}

	public void setInproInspUnit(String inproInspUnit) {
		this.inproInspUnit = inproInspUnit;
	}

	public String getInproInspNpe() {
		return inproInspNpe;
	}

	public void setInproInspNpe(String inproInspNpe) {
		this.inproInspNpe = inproInspNpe;
	}

	public String getFinalQt() {
		return finalQt;
	}

	public void setFinalQt(String finalQt) {
		this.finalQt = finalQt;
	}

	public String getFinalFats() {
		return finalFats;
	}

	public void setFinalFats(String finalFats) {
		this.finalFats = finalFats;
	}

	public String getFinalPacking() {
		return finalPacking;
	}

	public void setFinalPacking(String finalPacking) {
		this.finalPacking = finalPacking;
	}

	public String getFinalClearance() {
		return finalClearance;
	}

	public void setFinalClearance(String finalClearance) {
		this.finalClearance = finalClearance;
	}

	public String getFinalVendorRating() {
		return finalVendorRating;
	}

	public void setFinalVendorRating(String finalVendorRating) {
		this.finalVendorRating = finalVendorRating;
	}

	public String getFinalConsignee() {
		return finalConsignee;
	}

	public void setFinalConsignee(String finalConsignee) {
		this.finalConsignee = finalConsignee;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getNoOfItems() {
		return noOfItems;
	}

	public void setNoOfItems(String noOfItems) {
		this.noOfItems = noOfItems;
	}

	public String getNoOfLots() {
		return noOfLots;
	}

	public void setNoOfLots(String noOfLots) {
		this.noOfLots = noOfLots;
	}

}
