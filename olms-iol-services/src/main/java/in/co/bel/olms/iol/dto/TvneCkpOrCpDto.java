package in.co.bel.olms.iol.dto;

import java.util.List;

import lombok.Data;

@Data
public class TvneCkpOrCpDto {

	private double ckp;
	private double cp;
	private List<TvneFieldsDto> dtoVal;
}
