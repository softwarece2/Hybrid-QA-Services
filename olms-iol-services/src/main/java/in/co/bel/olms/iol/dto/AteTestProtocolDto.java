package in.co.bel.olms.iol.dto;

import lombok.Data;

@Data
public class AteTestProtocolDto {
	private Integer id;
    private Integer slNo;
    private Integer paramCode;
    private String mainType;
    private String paramType;
    private String paramDesc;
    private String tyClauseNo;
    private Double min;
    private Double max;
    private String unit;
    private String testBy;
    private Integer duration;
    private Integer ateTestId;
    private Integer testId;

}
