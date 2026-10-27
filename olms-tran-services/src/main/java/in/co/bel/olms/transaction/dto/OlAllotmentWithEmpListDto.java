package in.co.bel.olms.transaction.dto;

import java.util.List;

import in.co.bel.olms.master.dto.EmpRoleDto;
import lombok.Data;

@Data
public class OlAllotmentWithEmpListDto {
	private List<OlAllotmentDto> olAllotmentDtos;
	private List<EmpRoleDto> emps;

}
