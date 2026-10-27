package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CategoryDto {

	private Integer id;
	private String categoryName;
	private Integer shipId;
	private String shipName;
	private Integer saleOrderId;
	private String saleOrder;
	private String wtIssueDate;
	private String createdDate;
	private String modifiedDate;
	private BigDecimal qty;
	private String epCode;
	private String opNo;
	private String estNo;
	private Date estDate;
	private String revNo;
	private Integer soLiId;
	private String partNo;
	private String drawingNo;
	private String materialDescription;
	private String orderMasterNo;
	private String warrantStaus;
	private Integer statusId;
	private String grp;
	private String soNumber;
	private String inspAuthorityName;
	private Integer registrationNo;

	
}
