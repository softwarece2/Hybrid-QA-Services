package in.co.bel.olms.iol.ate.dto;

import lombok.Data;

@Data
public class ProductTestMapDto {
	private Integer id;
	private Integer productId;
	private Integer testId;
	private String proudctName ;
	private String testName;
	private Integer roundId;
	private Integer submittedById ;
	private String submittedBy;
	private String  submittedDate;
	private Integer sequence;
	private String isFai;
}
