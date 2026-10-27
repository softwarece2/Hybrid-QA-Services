package in.co.bel.olms.iol.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FaiProtocolDto {

	
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
   

    private List <String> obsValue= new ArrayList<>();
}
