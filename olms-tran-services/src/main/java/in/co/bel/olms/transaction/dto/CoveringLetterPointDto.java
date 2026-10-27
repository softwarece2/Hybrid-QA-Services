package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class CoveringLetterPointDto {
	private Integer id;
	private int coveringLetterId;
	private String contentText;
}
