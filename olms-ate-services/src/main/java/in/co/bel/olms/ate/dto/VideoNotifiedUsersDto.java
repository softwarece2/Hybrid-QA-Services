package in.co.bel.olms.ate.dto;

import lombok.Data;

@Data
public class VideoNotifiedUsersDto {

	private Integer id;
	private Integer videoNotificationId;
	private Integer notifiedUserId;
	private String notifiedUser;
	private Integer statusId;
	private String status;
	
}
