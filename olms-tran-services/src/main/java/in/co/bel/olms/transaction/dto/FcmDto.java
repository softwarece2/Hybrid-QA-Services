package in.co.bel.olms.transaction.dto;



import java.util.List;


import lombok.Data;

/**
 * 
 * @author Manasa B
 * @since 17-02-3023
 */

@Data
public class FcmDto {
	
	private int id;
	
	private String name;
	
	private Integer supplyOrderId;
	
	private String soOrderNo;
	
	private String soDate;
		
	private String dateOfReceipt;	
	
	private String itemDescription;	
	
	private String noOfItems;	
	
	private String opa;	
	
	private String deliveryDate;	
	
	private String noOfLots;
	
	
	private Boolean approvedDrg;
	
	
	private Boolean approvedQap;
	
	
	private Boolean oemTechSpecs;
	
	
	private Boolean importDocs;
	
	
	private Boolean specInst;
	
	
	private Boolean inspDiscrepancy;
	
	
	private String newDrgDate;
	
	
	private String existDrgDate;
	
	
	private String freshQapDate;
	
	
	private String existQapDate;
	
	private Boolean qtTestApplicable;
	
	
	private Boolean detailBom;
	
	
	private Boolean approvedPil;
	
	
	private Boolean billTransport;
	
	
	private Boolean invoiceOemOriginCert;
	
	
	private Boolean billEntryWarehouse;
	
	
	private Boolean certOfConfirmity;
	
	
	private Boolean warranty;
	
	
	private String meltPouring;
	
	
	private String sampleTestNabl;
	
	
	private Boolean subOrderDet;

	private String submittedDate;
	
	
	private String inproInspPcb;
	
	
	private String inproInspUnit;	
	
	
	private String inproInspNpe;
	
	
	private String finalQt;
	
	
	private String finalFats;
	
	
	private String finalPacking;
	
	
	private String finalClearance;
	
	
	private String finalVendorRating;
	

	private String finalConsignee;
	
	
	private String others;
	
	
	private List<FcmVendorDto> fcmVendor;
	
}
