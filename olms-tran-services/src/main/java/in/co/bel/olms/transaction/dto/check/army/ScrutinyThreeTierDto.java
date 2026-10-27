package in.co.bel.olms.transaction.dto.check.army;

import java.util.List;

import lombok.Data;

@Data
public class ScrutinyThreeTierDto {

	private Integer id;

	private String createdDatetime;

	private Integer offerLetterId;

	private Integer sectionOfficerId;

	private String submittedDate;

	private String modifiedDatetime;

	private Integer lastUpdatedBy;

	private List<ScrutinyThreeTierDetDto> scrutinyThreeTierDetDtos;
}
