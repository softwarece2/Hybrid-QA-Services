package in.co.bel.olms.transaction.dto;



import lombok.Data;

import java.util.List;

import in.co.bel.olms.transaction.entity.AmendmentsLetter;
import in.co.bel.olms.transaction.entity.DdExtension;
import in.co.bel.olms.transaction.entity.ItemLots;


/**
* SupplyOrderDto class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-16 
*/

@Data
public class SupplyOrderDto {
	
	private int id;
	private Integer salesGroupId;
	private Integer indenterId;
	private Integer consigneeId;
	private Integer inspectionAuthorityId;
	private Integer inoteTypeId;
	private Integer supplierId;
	private String createdDate;
	private String modifiedDate;
	private Integer createdBy;
	private Integer modifiedBy;
	private String orderNo;
	private String orderDate;
	private String description;
	private Integer noOfItems;
	private Integer totalQty;
	private String fileNo;
	private Double value;
	private String dd;
	private String indentNo;
	private String indentDate;
	private String soAmndmntRemarks;
	private String remarks;
	private String orderCompletionDate;
	private Integer soType;
	private Integer eqptGroup;
	private String salesGroupName;
	private String indenterName;
	private String consigneeName;
	private String inspectionAuthName;
	private String inoteTypeName;
	private String supplierName;
	private String receiptDate;
	private List<ItemLots> itemLots;
	private List<DdExtension> ddExtensions;
	private List<AmendmentsLetter> amendmentsLetters;
	
}
