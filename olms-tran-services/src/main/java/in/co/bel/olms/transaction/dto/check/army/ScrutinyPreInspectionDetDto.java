package in.co.bel.olms.transaction.dto.check.army;

import lombok.Data;

@Data
public class ScrutinyPreInspectionDetDto {

	private Integer id;
	private String slNo;
	private String reference;
	private Integer scrutinyPreInspectionId;
	private Integer checklistScrutinyPreInspectionId;
	private String remarks;
	private String obsSts;
	private String modifiedDatetime;
	private String createdDatetime;
	private Integer lastUpdatedBy;
	private boolean split;
	private String obsType;
	
}
