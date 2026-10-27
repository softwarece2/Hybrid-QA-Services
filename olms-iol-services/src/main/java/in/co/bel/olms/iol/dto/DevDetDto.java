package in.co.bel.olms.iol.dto;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class DevDetDto {

	private BigInteger acceptedCount;
	private BigInteger rejectedCount;
	private Double successRate;
	private List<IolDataDto> ioldata;
}
