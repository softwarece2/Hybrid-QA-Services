package in.co.bel.olms.master.dto;

import java.util.List;

import lombok.Data;

@Data
public class IsAteDto {

	private List<TestDto> testDtos;
	private Integer oLLineItemId;
	private String lineItemNo;
	
}
