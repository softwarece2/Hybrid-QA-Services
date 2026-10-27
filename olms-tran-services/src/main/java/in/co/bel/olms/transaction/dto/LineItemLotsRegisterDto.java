package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class LineItemLotsRegisterDto {
	private Integer id;
	private List<String> registerNoList;
	private String status;
	private Integer lineItemLotsId;
	private Integer olLineItemId;
	private Boolean isRunning;
	private Boolean isSample;
	private Integer qaStatusId;
	private String qaStatus;
		
    // for deletion and updates
	private String registerNo;
    private List<Integer> ids;
    private Integer sampleQty;
    private Integer soLineItemId;
    private Integer empId;
}