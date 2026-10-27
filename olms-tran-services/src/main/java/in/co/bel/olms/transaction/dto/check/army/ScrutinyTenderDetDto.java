package in.co.bel.olms.transaction.dto.check.army;

import lombok.Data;

@Data
public class ScrutinyTenderDetDto {

	private Integer id;
	private Integer checklistScrutinyTenderId;
	private String obsSts;
	private String remarks;
	private String reference;
	private String slNo;
	private boolean split;
	private String obsType;
	private Integer scrutinyTenderId;
	private String actionAndRef;

}
