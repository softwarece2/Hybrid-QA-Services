package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ReturnOfferLetterDto {

	private Integer id;

	private Integer offerLetterId;
	
	private String offerLetterNo;
	
    private Date offerDate;

	private String salesGrp;
	
	private String soNumber;
	
	private String projectName;
	
	private Date createdDate;
	
	private String createdDateStr;

	private Integer createdBy;
	
	private Integer orgTypeId;

	private List<ReturnOfferLetterDetDto> detDtos;
}
