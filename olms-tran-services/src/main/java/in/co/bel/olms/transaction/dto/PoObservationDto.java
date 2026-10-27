package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Manasa B
 * @since 09-03-2023
 *
 */

@Data
public class PoObservationDto {

	private Integer id;
	private Integer roleId;
	String soOrderNo;
	private String salesGroupName;
	Integer supplyOrderId;
	private String projectName;
	private String createdDatetime;

	private String submittedDate;
	private String opaSubmittedDate;
	private String obsCreatedDate;
	private String closureDate;
	private Integer closureOfficerId;
	private Integer secOfficerId;
	private String secOfficer;
	private String soDate;

	private int statusId;
	private String status;
	
	private List<PoObservationDetDto> obsList;

}