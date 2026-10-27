package in.co.bel.olms.transaction.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClearanceLetterPointDto {

	private Integer id;
	private int clearanceLetterId;
	private String contentText;
	private List<ClearanceLetterItemDto> clearanceLetterItemDtos;

}
