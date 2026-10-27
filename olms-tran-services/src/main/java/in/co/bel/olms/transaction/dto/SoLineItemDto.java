package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SoLineItemDto {
	
	private Integer id;
	private String name;
	
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z\\s]+$", message = "Only alphanumeric characters allowed")
	private String lineItemNo;
	
//	@NotBlank(message = "Value must not be Empty")
	private Date createdDateTime;
	private Integer createdBy;
	private Date modifiedDateTime;
	private Integer modifiedBy;
	
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9@#,&.\\s]+$", message = "Only alphanumeric and #@,&. characters allowed")
	private String description;
	
	private String saleOrderNo;
	
	private Integer saleOrderId;
	
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z\\s]+$", message = "Only alphanumeric characters allowed")
	private String partNo;
	
//	@NotBlank(message = "Value must not be Empty")
	private BigDecimal unitCost;
	
	@NotNull(message = "Value must not be Empty")
	private BigDecimal orderQty;
	
	private BigDecimal itemValue;
	
	private Integer supplyOrderId;
	private Integer uomId;
	@NotBlank(message = "Value must not be Empty")
	private String uomName;
	private Integer itemCategoryId;
	@NotBlank(message = "Value must not be Empty")
	private String itemCategoryName;
	private String warrantyStr;
	private boolean underWarranty;
	private Integer duration;
	private Integer durationTypeId;
	private String durationTypeStr;
	private String durationTypeName;
	private String account;
	private String status;
	private String underWarrantyStr;
	private String selfCertification;
	private Boolean gcs;
	private String gem;
	private String tmrFileUrl;
	private String updatedTmrFileUrl;
	private String remarks;
	private List<OlTmrFileDto> olTmrFileDtos;
	private BigDecimal remainingQty;
	private String unitCostStr;
	private Boolean isImported;
	private String orderQtyStr;
	private String nsnPartNo;
	private String catPartNo;
	private Integer partNumberId;
	private String partNumberName;
	private String materialDescription;
	private String nsnPartNumber;
	private String catPartNumber;
	private Boolean isRegistrationNo;
	private Integer regType;

	private String drawingNo;
	private Date deliveryDate;
	private String deliveryDt;
	private Integer inspectionAuthorityId;
	private String inspectionAuthorityName;
    private String refNo;
    private Integer orderTypeId;
    private String orderTypeName;

    private String epCode;
    private Integer supplyType;
    private String supplyTypeName;
    private String consignee;
    private Integer registrationNo;
    private String registrationNoName;
    private Integer lotNo;
    private String orderNo;
    private Date orderDate;
    
    private List<Integer> consigneeIds;
	private String orderMasterNo;
	private String orderTypeNo;
	private Date orderTypeDate;
	private Integer deptId;
	private String departmentName;
	private Integer warrPeriodQty;
	private String warrPeriodType;
	private Integer warrPeriodTypeId;
	
    
 }
