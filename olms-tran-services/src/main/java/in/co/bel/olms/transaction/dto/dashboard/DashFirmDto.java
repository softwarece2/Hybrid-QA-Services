package in.co.bel.olms.transaction.dto.dashboard;

import java.math.BigInteger;

import lombok.Data;

@Data
public class DashFirmDto {
	
	private BigInteger countOl;
	private Integer orgId;
	private String orgName;
	private byte[] logo;
	private Integer pending;
	private String projName;
	private Double projectValue;

}
