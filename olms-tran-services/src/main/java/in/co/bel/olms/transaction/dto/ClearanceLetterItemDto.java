package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class ClearanceLetterItemDto {

	private Integer id;
	private String unitSlNo;
	private String hologramNo;
	private Integer olLineItemId;
	private Integer clearanceLetterPointId;
    private String  description;
	private String partNo;
	
}
