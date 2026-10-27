package in.co.bel.olms.ate.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SpecDto {

	private Integer id;
	private Integer prodId;
	private String productName;
	private Integer testId;
	private String testName ;
	private String specVal;
	private BigDecimal  min;
	private BigDecimal  max;
	private Integer logInId;
	private String paramCode;
	private Integer paramTypeId;
	private String paramType;
	private String tyClauseNo;
	private Integer  unitId;
	private String unit;
	private Integer  obsTypeId;
	private String obsType;
	private String obsValue;
	private Boolean status;
	private Integer slNo;
	private Integer mainTypeId;
	private String mainType;
	private String testType;
	private Boolean active;
	private List <TestByMstDto> testByMstDtos;
	
	public SpecDto(Integer id, Integer prodId, String specVal, BigDecimal  min, BigDecimal  max, Integer testId , String testName ) {
		this.id = id;
		this.prodId = prodId;
		this.specVal = specVal;
		this.min = min;
		this.max = max;
		this.testId = testId;
		this.testName = testName;
	}


	public SpecDto() {
	}
	
}
