package in.co.bel.olms.ate.dto;

import java.util.List;
import lombok.Data;

@Data
public class VideoNotificationDto {
	
	private Integer id;
	private String requestedBy;
	private Integer requestedById;
	private String statusName;
	private Integer statusId;
	private String dateTime;
	private Integer olId;
	private Integer olLiId;
	private Integer devId;
	private String devName;
	private String approvedBy;
	private Integer approvedById;
	private Integer testId;
	private String testName;
	
	private List<Integer> notifiedUserIds;
	private List<VideoNotifiedUsersDto> notifiedUsers;
	
}
