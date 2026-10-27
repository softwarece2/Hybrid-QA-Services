package in.co.bel.olms.transaction.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
@Data
public class DdExtensionDto {
	private Integer id;
	private Date letterDate;
	private Date newDdDate;
	private String remarks;
	private Integer supplyOrderId;
	private Integer soLiId;
	private Integer type;
	private String letterNo;
	private String status;
	List<String> fileDownloadUri;
	private String letterDateStr;
	private String newDdDateStr;
	
}
