package in.co.bel.olms.transaction.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class ProjectDataDto {

	private String projectName;
	private String shipNo;
	private String categoryName;
	private String saleOrderNo;
	private Integer createdBy;
	private Integer modifiedBy;
	private String status;
	private int saleOrderId;
	private int projectId;
	private boolean active;
	private Date wtIssueDate;
	private Date estDate;
	private Integer revNo;
	private String opNo;
	private Integer deptTypeId;
	private BigDecimal qty;
	private Integer id;
	private Integer statusId;

}
