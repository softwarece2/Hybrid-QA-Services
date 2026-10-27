package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SoLineItemDto {
	
	private Integer id;
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s]+$", message = "Only alphanumeric characters allowed")
	private String lineItemNo;
	@NotNull(message = "Value must not be null")
	private Date createdDateTime;
	private Integer createdBy;
	private Date modifiedDateTime;
	private Integer modifiedBy;
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s]+$", message = "Only alphanumeric characters allowed")
	private String description;
	
//	@NotBlank(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[a-zA-Z0-9\\s]+$", message = "Only alphanumeric characters allowed")
	private String partNo;
	
//	@NotNull(message = "Value must not be Empty")
//	@Pattern(regexp = "^$|^[0-9]+$", message = "Only number characters allowed")
	private BigDecimal unitCost;
	@NotNull(message = "Value must not be null")
	private BigDecimal orderQty;
	private BigDecimal itemValue;
	private Integer supplyOrderId;
	private Integer uomId;
	private Integer itemCategoryId;
//	@NotBlank(message = "Value must not be null")
	private String itemCategoryName;
	private boolean isUnderWarranty;
	private boolean isGem;
	private boolean isGcs;
	private boolean isSelfCertified;
	private Integer duration;
	private Integer durationTypeId;
	private String durationTypeName;
	private String account;
	private String status;
//	@NotNull(message = "Value must not be null")
	private String gem;
	private String gcs;
//	@NotNull(message = "Value must not be null")
	private String selfCertification;
	private String nsnPartNo;
	private String catPartNo;
	private String drawingNo;
	private Date deliveryDate;
}
