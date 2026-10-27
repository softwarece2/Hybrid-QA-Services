package in.co.bel.olms.transaction.dto.check.army;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ScrutinyQaclSparesDto {
	private Integer id;

	private Integer offerLetterId;

	private Integer createdBy;

	private Date createdDatetime;

	private Integer modifiedBy;

	private Date modifiedDatetime;

	private Integer signQaStaffId;

	private Date signQaStaffDate;

	private String remarksQaTeam;

	private String remarksQaTeamSign;

	private Date remarksQaTeamDate;

	private String remarksQao;

	private String remarksQaoSign;

	private Date remarksQaoDate;

	private String submittedDate;

	private List<ScrutinyQaclSparesDetDto> qaclSparesDetDtos;

	private Integer soStatusId;

	private String soStatus;
	private List<String> qalFileUploads;
	private String orderNo;

	private Date orderDate;

	private String description;

	private String olNumber;

	private Date offerDate;
	private Date qaoSubmittedDate;
}
