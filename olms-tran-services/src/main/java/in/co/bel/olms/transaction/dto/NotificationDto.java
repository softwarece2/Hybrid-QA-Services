package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class NotificationDto {

	private Integer id;
	private String notificationName;
	private String createdDateTime;
	private boolean readStatus;
	private Date readTime;
	private Integer categoryId;
	private Integer readBy;
	private String readStatusString;
	private String notificationCategory;
	private Integer senderId;
	private String senderName;
	private Integer receiverId;
	private String receiverName;
	private String soNumber;
	private String sbuName;
	private String Epcode;
	private Boolean isLatest;
	private List<String> categories;
	private String inspAgency;
    private String materialDescription;
    private Integer mergeSubSlipId;
}
