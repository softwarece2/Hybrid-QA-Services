package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OperationDto {

	private Integer id;
	private String opNo;
	private String desc;
	private Boolean isFinal;
	private Integer seq;
	private Integer deptTypeId;
	private String deptType;
	private Integer warrantId;
	private String warrant;
	private Date createdDate;
	
}
