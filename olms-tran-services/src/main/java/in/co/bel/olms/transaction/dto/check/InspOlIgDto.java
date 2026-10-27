package in.co.bel.olms.transaction.dto.check;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Manasa B
 *
 */

@Data
public class InspOlIgDto {
	
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

	private List<InspOlIgDetDto> inspOlIgDetDtos;

}
