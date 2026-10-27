package in.co.bel.olms.transaction.dto.check;

import lombok.Data;

@Data
public class ScrutinySoDetDto {

private Integer id;
	private Integer checklistId;
	private String slNo;
	private String reference;
	private String remarks;
	private boolean split;
	private String obsSts;	
	private String obsType;
}
