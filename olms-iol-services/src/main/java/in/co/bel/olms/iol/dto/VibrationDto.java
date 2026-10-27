package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class VibrationDto {

	private Integer id;
	private Integer vibrationCleanOk;
	private Integer vibrationCleanOperate;
	private String vibrationCleanRem;
	private Integer vibrationFlick;
	private Integer vibrationBreakdown;
	private String vibrationOperateRem;
	private Integer tvneId;
	private String submittedDate;
	private Date createdDatetime;
}
