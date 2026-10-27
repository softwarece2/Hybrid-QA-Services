package in.co.bel.olms.transaction.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LineItemObservationFileDto {
	private Integer id;
	private Integer createdBy;
	private String createdByName;
	private String remarks;
	private Date createdDate;
	private List<LineItemObservationAttachmentDto> lineItemObservationAttachmentDtos;
	private Integer lineItemObsId;
	private List<String> uploadedFiles = new ArrayList<>();
	private Integer sendTo;
	private String sendToName;
}
