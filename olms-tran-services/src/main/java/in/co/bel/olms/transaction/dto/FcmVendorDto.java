package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class FcmVendorDto {

	private Integer id;
	private Integer fcmId;
	private String vendor;	
	private String itemDesc;
	private String agency;	
}
