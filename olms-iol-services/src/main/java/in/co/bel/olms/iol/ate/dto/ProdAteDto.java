package in.co.bel.olms.iol.ate.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProdAteDto {
	private Integer prodId;
	private String productName;
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
	private String obsType;
	private String obsValue;

	private Integer id;
	private Integer specId;
	private Integer inspRoundId;
	private Integer obsTypeId;
	private String remarks;
	
	private String status;
	private Integer testId;
	private String obsValTx;

	private String testType;
	private String mainType;
	
	private Boolean activeStatus;

	private List<FileDetailDto> fileDetails = new ArrayList<>();
	
//	private Integer modifiedBy;
//	private Date modifiedDate;

}
