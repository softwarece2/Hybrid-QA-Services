package in.co.bel.olms.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class FaiProductDto {
	private Integer faiId;
	private Integer subQty;
	private Integer actQty;
	private Integer rejQty;
	private String remarks;
	private List<ObservationDto> dtoList;
}
