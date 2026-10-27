package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;


@Data
public class AmendmentsLetterDto {
	private Integer id;
	private String letterNo;
	private Date letterDate;
	private String amendments;
	private String amendmentsOf;
	private String remarks;
	private Integer supplyOrderId;
	private Integer soLiId;
	private Integer type;
	private String subject;
	private List<String> fileDownloadUri;
}
