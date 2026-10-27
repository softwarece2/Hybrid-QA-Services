package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class LineItemSerialNoHistoryDto {

	private Integer id;
	private Integer lineItemSerialNoId;
	private List<Integer> lineItemSerialNoIds;
	private Integer statusId;
	private String status;
	private String remark;
	private Integer createdById;
	private String createdBy;
	private String createdAt;
	private Integer fkId;
	private String fkName;
	
}
