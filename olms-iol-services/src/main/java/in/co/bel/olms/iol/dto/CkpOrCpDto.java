package in.co.bel.olms.iol.dto;

import java.util.List;

import lombok.Data;

@Data
public class CkpOrCpDto {
	private double ckp;
	private double cp;
	private double mean;
	private double stdDev;
	private double median;
	private List<VoltDto> voltDtos;
}
