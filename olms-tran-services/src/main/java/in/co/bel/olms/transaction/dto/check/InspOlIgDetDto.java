package in.co.bel.olms.transaction.dto.check;

import lombok.Data;

@Data
public class InspOlIgDetDto {
	private Integer id;
	private Integer checklistId;
	private String slNo;
	private String reference;
	private boolean split;
   private String remarks;
   private String obsSts;	
	private String obsType;
}
