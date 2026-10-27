package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* SupplyOrderDto class
*
* @author  Awadhesh Verma
* @version 1.0
* @since   2022-08-16 
*/

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SupplyOrderDto {
	
	private int id;
	private Integer salesGroupId;
	private Integer indenterId;
	private Integer consigneeId;
	private Integer inspectionAuthorityId;
	private Integer inoteTypeId;
	private Integer supplierId;
	private Date createdDate;
	private Date modifiedDate;
	private Integer createdBy;
	private Integer modifiedBy;
	private String orderNo;
	private Date orderDate;
	private String description;
	private Integer noOfItems;
	private Integer totalQty;
	private String fileNo;
	private Double value;
	private Date dd;
	private String indentNo;
	private Date indentDate;
	private String soAmndmntRemarks;
	private String remarks;
	private Date orderCompletionDate;
	private Integer soType;
	private Integer eqptGroup;
	private String salesGroupName;
	private String indenterName;
	private String consigneeName;
	private String inspectionAuthName;
	private String inoteTypeName;
	private String supplierName;

}
