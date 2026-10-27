package in.co.bel.olms.transaction.dto;


import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* SupplyOrderDto class
*
* @author  Awadhesh Verma
* @author Manasa B
* @version 1.0
* @since   22-02-2023
*/

@Getter
@Setter
@ToString
public class SupplyOrderDto {
	
	private int id;
	private Integer salesGroupId;
	private Integer indenterId;
	private List<Integer> consigneeIds;
	private Integer employeeId;
	private String inspectorName;
	private Integer secOfficerId;
	private String secOfficerName;
	private Integer dcqaoId;
	private String dcqaoName;
	private Integer jcqaoId;
	private String jcqaoName;
	private Integer ccqaoId;
	private String ccqaoName;
	private Integer organizationId;
	private Integer inoteTypeId;
	private Integer supplierId;
	private Date createdDate;
	private Date modifiedDate;
	private Integer createdBy;
	private Integer modifiedBy;
	private String orderNo;
	private Date orderDate;
	private String orderDateStr;
	private String description;
	private Integer noOfItems;
	private Integer totalQty;
	private String fileNo;
	private Double value;
	private Date dd;
	private String ddStr;
	private String indentNo;
	private Date indentDate;
	private Integer lotNo;
	private String soAmndmntRemarks;
	private String remarks;
	private Date orderCompletionDate;
	private Integer soType;
	private String soTypeName;
	private Integer eqptGroup;
	private String salesGroupName;
	private String indenterName;
	private String consigneeName;
	private String inspectionAuthName;
	private String inoteTypeName;
	private String supplierName;
	private String status;
	private String projectName;
	private Date receiptDate;
	private List<String> qapFiles;
	private List<String> drawings;
	private List<String> atpFiles;
	private List<String> attachments;
	private List<String> amendments;
	private List<String> poFiles;
	private List<ItemLotsDto> itemLots;
	private List<DdExtensionDto> ddExtensions;
    private List<AmendmentsLetterDto> amendmentsLetters;
    private List<String> itemLotFiles;
    private List<String> ddFiles;
    private Boolean isFclSubmitted;
    private Boolean isClosed;
    private Boolean isFcmSubmitted;
    private Boolean isScrutinySubmitted;
	private Integer poObservatioId;
	private Boolean isDisplayAlert;
	private List<String> alertMessage;
	private String allotedBy;
	private Integer accountTypeId;
	private List<Integer> poObservatioIds;
	private Boolean isNotified;
	private String amendedLotNo;
	private String acctName;
    private Boolean approvalstatus;
    private Boolean isNotifiedToJc;
    private Boolean isGcc;
    private List<String> indentSlNoList;
    private Boolean isScrutinyTenderSubmitted;
	private Boolean isRegistrationNo;
	private String partNo;
	private String deliveryDate;
	private Integer offeredQty;
	private Integer acceptedQty;
	private String drawingNo;
	private String referenceNo;
	private String organizationName;
	private Integer soStatusId;
	private String soStatusNo;
	private Boolean isActive;
	private Boolean isDel;
	private Integer delBy;
	private Date delDatetime;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupplyOrderDto other = (SupplyOrderDto) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
