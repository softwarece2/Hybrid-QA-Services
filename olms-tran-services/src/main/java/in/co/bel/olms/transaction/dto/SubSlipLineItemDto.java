
package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SubSlipLineItemDto {

	private Integer id;
	private Integer soLineItemId;
	private String soLineItemNo;
	private Integer offerLetterId;
	private String offerLetterNo;
	@NotBlank(message = "Value must not be Empty")
	@NotNull(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s]+$", message = "Only alphanumeric characters allowed")
	private String partNo;
	private BigDecimal offeredQty;
	private BigDecimal orderedQty;
	private BigDecimal pendingQty;
	private Date createdDateTime;
	private Integer createdBy;
	private Date modifiedDateTime;
	private Date offerDate;
	private Integer modifiedBy;
	private String issueLevel;
	private String priority;
	private String tmrFileUrl;
	private String updatedTmrFileUrl;
	private String name;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s]+$", message = "Only alphanumeric characters allowed")
	private String lineItemNo;
	@NotBlank(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,&. characters allowed")
	private String description;
	private String saleOrderNo;
	@NotNull(message = "Value must not be Empty")
	@Pattern(regexp = "^$|^[0-9]+$", message = "Only number characters allowed")
	private Double unitCost;
	private Integer orderQty;
	private Double itemValue;
	private Integer supplyOrderId;
	private Integer uomId;
	private String uomName;
	private Integer itemCategoryId;
	private String itemCategoryName;
	private String warrantyStr;
	private Boolean isUnderWarranty;
	private Integer duration;
	private Integer durationTypeId;
	private String durationTypeStr;
	private String durationTypeName;
	private String account;
	private String status;
	private String underWarrantyStr;
	private String selfCertification;
	private String gcs;
	private String gem;
	private String remarks;
	private Integer deliveredQty;
	private String unitCostStr;
	private String itemValueStr;
	private String hologramRemarks;
	private Boolean isImported;
	private Integer clearedQty;
	private Integer remainingQty;
	private String consigneeName;
	private Integer productId;
	private Integer partNumberId;
	private String partNumberName;
	private String materialDescription;	
	private LineItemLotsRegisterDto registerNumbers;

}
