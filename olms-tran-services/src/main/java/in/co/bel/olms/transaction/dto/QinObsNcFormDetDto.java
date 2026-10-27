package in.co.bel.olms.transaction.dto;

import lombok.Data;

/**
 * QinObsNcFormDetDto DTO class
 *
 * @author Awadhesh Verma
 * @version 1.0
 * @since 2023-03-27
 */

@Data
public class QinObsNcFormDetDto {

	private Integer id;
	private String partNo;
	private String description;
	private String observation;
	private String category;
	private String reworkAction;
	private String remarks;
	private int olLineItemId;
	private int qinObsNcFormId;
	private OlLineItemDto olLineItemDto;
	private boolean closureStatus;
	private String subPartNo;

}
