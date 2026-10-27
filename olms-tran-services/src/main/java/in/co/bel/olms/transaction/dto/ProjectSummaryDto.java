package in.co.bel.olms.transaction.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class ProjectSummaryDto {

	private String opa;
	private Double prVal;
	private Integer valCLot;
	private Integer valClQty;
	private Boolean alert;
	private Integer testThrInd;
	private Integer valTtlLItems;
	private BigInteger valTtlTe;
	private String qaclNo;
	private BigInteger noRfr;
	
}
