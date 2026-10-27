package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class PoObservationDetDto {

	private Integer id;

	private Integer poObservationId;

	private String observation;

	private String remedialAction;

	private String remarks;

	private Boolean isClosed;
	
	private String obsCategory;
	
	private Integer soScrutinyDetId;
	
	private Integer scrutinyTenderDetId ;
	
	private String actionReference;
	
}
