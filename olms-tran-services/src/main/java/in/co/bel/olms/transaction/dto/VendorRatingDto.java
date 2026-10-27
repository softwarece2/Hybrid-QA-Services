package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class VendorRatingDto {
	
	private Integer id;

	private Integer vendorId;

	private Double qr;

	private Double dr;

	private Double er;

	private Double kd;

	private Double kr;

	private Double na;

	private Double ni;

	private Double q1;

	private Double drQ;

	private Double t;

	private Double t1;

	private Double p;

	private Double q;
	
	private Integer promptness;

	private Integer attitude;

	private Integer preparedBy;

	private Integer vettedBy;

}
