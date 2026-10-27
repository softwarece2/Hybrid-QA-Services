package in.co.bel.olms.transaction.dto.check.army;

import lombok.Data;

@Data
public class ScrutinyQaclDetDto {
	
	private Integer id;
	private Integer scrutinyQaclId;
	private Integer checklistScrutinyQaclId;
	private String slNo;
	private String reference;
	private boolean split;
	private String obsSts;	
	private String obsType;
	private String remarks;
	private Integer part; 

}
