package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class WorkLoadDto {
	private int id;
	
	private Integer supplyOrderId;
	private Integer offerLetterId;
	private String soDate;
	private String soOrderNo;
	private BigDecimal totalQty;
	private String fileNo;
	private BigDecimal value;
	private String originalDd;
	private String extendedDd;
	private Integer noOfItems;

	private String supplierName;
	private String qapRef;
	
	private Integer createdBy;
	private String submittedDate;
	private String planSubmittedDate;
	
	private String otherDetail;
	private String mainEqptItem;
	private String orderIssuedBy;
	
	private Integer totalEstDays;
	private Integer totalActDays;
	
	List<WorkloadInspActDetDto> wrkLdDetDtos;
	
	List<WorkloadInspActDetDto> wrkLdPreDetDtos;
	List<WorkloadInspActDetDto> wrkLdInprogDetDtos;
	List<WorkloadInspActDetDto> wrkLdPostDetDtos;
}
