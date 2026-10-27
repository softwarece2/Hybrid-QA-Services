package in.co.bel.olms.transaction.dto.check;

import lombok.Data;

/**
 * 
 * @author Manasa B
 *
 */

@Data
public class InspOlIbDetDto {
	private Integer id;
	private Integer checklistId;
	private String slNo;
	private String reference;
	private boolean split;
	private String obsSts;	
	private String obsType;
	private String remarks;
}
