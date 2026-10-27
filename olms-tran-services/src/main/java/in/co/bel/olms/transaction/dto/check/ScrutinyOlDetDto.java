package in.co.bel.olms.transaction.dto.check;

import lombok.Data;

@Data
public class ScrutinyOlDetDto {

	private Integer id;

	private Integer scrutinyOlId;

	private Integer checklistOlId;

	private String verified;
	
	private String remarks;
	
	private boolean split;
	
	private String obsSts;	
	
	private String obsType;


}
