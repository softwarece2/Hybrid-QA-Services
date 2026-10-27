package in.co.bel.olms.transaction.dto.check.army;

import java.util.List;

import lombok.Data;

@Data
public class ScrutinyTenderDto {

	private Integer id;
	private String submittedDatetime;
	private Integer supplyOrderId;
	private Integer sectionOfficerId;
	private List<ScrutinyTenderDetDto> scrutinyTenderDetDtos;

}
