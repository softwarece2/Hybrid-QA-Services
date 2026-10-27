package in.co.bel.olms.iol.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProdDto {
	private Integer inspId ;
	private Integer	roundId ;
	private Integer prodId;
	private List<TestValDto> testValDto;
}
