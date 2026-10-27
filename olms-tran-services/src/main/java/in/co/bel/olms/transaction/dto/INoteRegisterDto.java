package in.co.bel.olms.transaction.dto;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString

public class INoteRegisterDto {
	private Integer totalINotes;
	private Integer noOfItems;
	private Integer totalQty;
	private String totalValue;
	private Map<String, String> iNoteValueMap;
	private List<INoteRegisterItemDto> iNoteRegisterItemDtos;
	
}
