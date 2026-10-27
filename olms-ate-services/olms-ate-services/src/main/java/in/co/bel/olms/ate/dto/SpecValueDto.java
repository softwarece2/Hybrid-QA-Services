package in.co.bel.olms.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class SpecValueDto {
	private Integer testId;
	private String testName;
	private List<SpecDto> specDtos;

}
