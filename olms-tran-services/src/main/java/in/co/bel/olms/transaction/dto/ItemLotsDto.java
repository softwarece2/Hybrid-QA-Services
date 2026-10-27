package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;


import lombok.Data;

@Data
public class ItemLotsDto {
	private Integer id;
	private Date ilLetterDate;
	private String ilLetterNo;
	private String lotNo;
	private String status;
	private Integer supplyOrderId;
	private Integer soLiId;
	private Integer type;
	List<String> fileDownloadUri;
}
