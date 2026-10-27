package in.co.bel.olms.iol.ate.dto;

import lombok.Data;

@Data
public class TestValDto {
	private Integer testId;
//	private Integer id;
	private String testName;
	private Integer	 specId;		 
    private String specVal;
//    private BigDecimal  min;
//	private BigDecimal  max;
    private String  min;
	private String  max;
		
	private String status ;	
	private String paramCode;
	private Integer paramTypeId;
	private String paramType;
	private String tyClauseNo;
//	private Integer  unitId;
	private String unit;
//	private String obsType;
	private Integer obsTypeId;
	private String obsValue;
	private String observation;
	private String submittedBy ; 
	
}
