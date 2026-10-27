package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class WorkLoadOlDto {
	private int id;
	
	private Integer supplyOrderId;
	private Integer offerLetterId;
	private String soDate;
	private String soOrderNo;
	private BigDecimal totalQty;
	private String fileNo;
	private BigDecimal value;
	private String originalDd;
	private Integer noOfItems;
	private String supplierName;
	
	private Integer createdBy;
	private String submittedDate;
	private String planSubmittedDate;
	private String offerDate;
	
	private String otherDetail;
	private String mainEqptItem;
	private String orderIssuedBy;
	
	private Integer totalEstDays;
	private Integer totalActDays;
	
	
	List<WorkloadInspActDetDto> wrkLdDetDtos;
	List<WorkloadInspActDetDto> wrkLdInprogDetDtos;
	List<WorkloadInspActDetDto> wrkLdPostDetDtos;
	
	
}
