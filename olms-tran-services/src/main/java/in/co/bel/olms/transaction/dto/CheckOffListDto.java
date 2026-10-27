package in.co.bel.olms.transaction.dto;

import java.util.List;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CheckOffListDto {
	private int id;
	private String docName;
    private List<String> attachmentURLs;
    private List<Integer> attachmentIds;

}
