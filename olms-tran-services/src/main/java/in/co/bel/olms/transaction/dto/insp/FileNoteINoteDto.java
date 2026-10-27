package in.co.bel.olms.transaction.dto.insp;

import lombok.Data;

import java.util.List;

/*
* Dto to hold INote no generated based on consignee in OL
* Awadhesh Verma
* 19-11-2024
* */

@Data
public class FileNoteINoteDto {
	
	private Integer fileNoteId;
	private String olIds;
	private Integer consigneeId;
	private String olNumbers;
	private String consigneeName;
	private String iNoteNo;
	private String newINoteNo;
	private List<Integer> offerLetterIds;

}
