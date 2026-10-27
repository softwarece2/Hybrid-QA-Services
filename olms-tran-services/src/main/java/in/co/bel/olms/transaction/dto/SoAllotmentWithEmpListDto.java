package in.co.bel.olms.transaction.dto;

import java.util.List;

import in.co.bel.olms.master.dto.EmpRoleDto;
import lombok.Data;

@Data
public class SoAllotmentWithEmpListDto {

	private List<SoAllotmentDto> soAllotmentDtos;
	private List<EmpRoleDto> qcUsers;
	private List<EmpRoleDto> qaUsers;
}
