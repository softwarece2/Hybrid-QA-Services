package in.co.bel.olms.transaction.dto.check.army;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScrutinyPreInspectionDto {

private Integer id;
	
	private String createdDatetime;
	
	private Integer offerLetterId;
	
	private Integer sectionOfficerId;
	
	private String submittedDate;
	
	private String modifiedDatetime;
	
	private Integer lastUpdatedBy;
	
	private List<ScrutinyPreInspectionDetDto> scrutinyPreInspectionDetDtos;
	
	private String qaoRemarks;

	private Integer soStatusId;
	
	private String soStatus;
	
	private Date qaoSubmittedDate;
	
	private List<String> qalFileUploads;
	
}
