package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class CoveringLetterDto {
	private Integer id;
	private int fileNoteId;
	private String copyTo;
	private String addressedTo;
	private String title;
	private Date coveringDate;
	private Integer createdBy;
	private Integer signatory;
	private String salesGrpName;
	private String supplierName;
	private String cqaeName;
	private String  address;
	private String fileNo;
	private List<CoveringLetterPointDto> coveringLetterPointDtos;
}
