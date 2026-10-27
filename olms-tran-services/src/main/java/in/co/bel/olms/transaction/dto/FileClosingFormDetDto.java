package in.co.bel.olms.transaction.dto;


import lombok.Data;

@Data
public class FileClosingFormDetDto {
	private Integer id;
	private Integer checklistId;
	private Integer slNo;
	private String reference;
	private String remarks;
	private boolean split;
	private String obsSts;	
	private String obsType;

}
