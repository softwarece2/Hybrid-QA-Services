package in.co.bel.olms.transaction.dto.check.army;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScrutinyQaclDto {
	
	private Integer id;

	private Integer offerLetterId;

	private String offerLetterNo;
	
	private Integer createdBy;

	private String createdDatetime;

	private Integer modifiedBy;

	private String modifiedDatetime;

	private Integer inspectedBy;

	private String inspectedDate;

	private Integer signedBy;

	private String signedDate;
	
	private String submittedDate;

	private Boolean isReturnOL;
	
	private String orderNo;
	
	private Date orderDate;
	
	private String description;
	
	private String olNumber;
	
	private Date offerDate;
	
	private List<ScrutinyQaclDetDto> qaclDetDtos;
	
	private String qaoRemarks;

	private Integer soStatusId;
	
	private String soStatus;
	
	private Date qaoSubmittedDate;
	
	private List<String> qalFileUploads;
	
	private BigDecimal offeredQty;
 
}
