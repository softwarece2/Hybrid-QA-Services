package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProjectExcelDto {

	private Integer empId;
	private Integer supplyOrderId;
	private Integer soLiId;
	private List<ProjectDataDto> dataDtos;

}
