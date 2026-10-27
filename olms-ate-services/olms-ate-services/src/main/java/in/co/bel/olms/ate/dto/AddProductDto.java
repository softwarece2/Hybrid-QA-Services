package in.co.bel.olms.ate.dto;

import java.util.List;

import lombok.Data;

@Data
public class AddProductDto {

	private Integer refProdId;
	private String prodName;
	private String prodFullName;
	private List<Integer> testIds;
	
}