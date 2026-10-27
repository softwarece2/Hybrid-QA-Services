package in.co.bel.olms.transaction.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TmrResultDto {
	private Integer id;
	private String parameter;
	private Double expectedMin;
	private Double expectedMax;
	private String result;
	private String remarks;
	private String inspAgencyRemarks;
	private Integer olLineItemId;
	private Integer appTestId;
	private String createdBy;
	private Date selectedDatetime;
	private String unit;
}
