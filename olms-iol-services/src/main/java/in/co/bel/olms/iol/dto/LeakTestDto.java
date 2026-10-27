package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class LeakTestDto {

	private Integer id;
	private Double leakTest;
	private String leakTestRem;
	private Integer tvneId;
	private String submittedDate;
	private Date createdDatetime;
	private Integer status;
	private String remarks;
}
