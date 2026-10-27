package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ClearanceLetterDto {

	private Integer id;
	private int offerLetterId;
	private String copyTo;
	private String addressedTo;
	private String title;
	private Date letterDate;
	private Integer createdBy;
	private Integer signatory;
	private String salesGrpName;
	private String supplierName;
	private String cqaeName;
	private String  address;
	private String fileNo;
	private List<ClearanceLetterPointDto> clearanceLetterPointDtos;
	

}
