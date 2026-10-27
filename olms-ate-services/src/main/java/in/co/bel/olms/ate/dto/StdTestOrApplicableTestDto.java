package in.co.bel.olms.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class StdTestOrApplicableTestDto {
	private List<ApplicableTestDto> applicableTestDtos;
	private List<ProductMappingDto> stdProdctTest;
	private List<SpecDto> specDtos;
}
