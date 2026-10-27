package in.co.bel.olms.transaction.dto.check.army;

import java.util.List;

import lombok.Data;

@Data
public class ScrutinyGiFormDto {

	private Integer id;
	
	private String createdDatetime;
	
	private Integer supplyOrderId;
	
	private Integer sectionOfficerId;
	
	private String submittedDate;
	
	private String modifiedDatetime;
	
	private Integer lastUpdatedBy;
	
	private List<ScrutinyGiFormDetDto> scrutinyGiFormDetDtos;
}
