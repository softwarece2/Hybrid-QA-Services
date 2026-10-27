package in.co.bel.olms.transaction.dto.check;

import java.util.List;

import lombok.Data;


/**
 * 
 * @author Manasa B
 *
 */

@Data
public class ScrutinySoDto {

	private Integer id;

	private String createdDatetime;
	
	private String submittedDatetime;
	
	private Integer supplyOrderId;

	private String supplyOrderNo;
	
	private Integer sectionOfficerId;	

	private List<ScrutinySoDetDto> scrutinySoDetDtos;

	
}
