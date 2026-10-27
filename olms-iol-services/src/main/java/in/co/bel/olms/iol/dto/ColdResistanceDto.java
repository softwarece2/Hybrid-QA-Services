package in.co.bel.olms.iol.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ColdResistanceDto {

	private Integer id;
	private Double coldCur;
	private Double coldCurPrism;
	private Double coldCurAuto;
	private Integer coldCondensation;
	private Integer coldMovSmooth;
	private Integer coldMovOpen;
	private Integer detoriorationOptPart;
	private String coldCurRem;
	private String coldCurPrismRem;
	private String coldCurAutoRem;
	private String coldCondensationRem;
	private String coldMovRem;
	private String detoriorationOptPartRem;
	private Integer tvneId;
	private String submittedDate;
	private Date createdDatetime;
	private Integer status;
	private String remarks;
	
}
