package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class FileClosingFormDto {
	
	private Integer id;
	private int supplyOrderId;
	private Date createdDatetime;
	private Integer modifiedBy;
	private Date modifiedDatetime;
	private Integer createdBy;
	
	
	private List<FileClosingFormDetDto> detDtos ;
}