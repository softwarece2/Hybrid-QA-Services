package in.co.bel.olms.transaction.dto;

import java.util.List;

import in.co.bel.olms.master.dto.EmpRoleDto;
import lombok.Data;

@Data

public class LotAllotmentEmpDto {
private List<LotAllotmentDto> lotAllotmentDtos;
private List<EmpRoleDto> empDto;
}
