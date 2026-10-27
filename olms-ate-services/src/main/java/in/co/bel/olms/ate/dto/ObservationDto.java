package in.co.bel.olms.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class ObservationDto {
	private Integer id;
	private Integer prodId;
	private Integer olLineItemId;
	private Integer applicableTestId;
	private String createdDatetime;
	private String submittedDate;
	private Integer devId;
	private String  devName;
	private Integer pageNo;
	private Integer faiId;
	private Integer maxPageNo;
	private Integer createdById;
	private String  createdBy;
	private List<ObsRemDto> obsRemDtos;
	
}
