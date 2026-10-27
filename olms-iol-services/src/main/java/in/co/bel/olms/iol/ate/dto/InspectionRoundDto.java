package in.co.bel.olms.iol.ate.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class InspectionRoundDto {
	private Integer id;
	private Integer devId;
	private Date inspDate;
	private Integer submittedById;
	private Integer approvedById;
	private Integer olLineItemId;
	private Integer subSlipLineItemId;
	private Integer subSlipSerialNoId;
	private Integer productId;
	private Date createdDatetime;
	private Date submittedDate;
	private String status;
	private Integer roundId;
	private String submittedBy;
	private Boolean isQa;
	private List<ProdAteDto> prodAteDtos;
	private Integer olId;
	private Boolean activeStatus;

}
