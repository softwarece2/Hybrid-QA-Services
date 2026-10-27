
package in.co.bel.olms.transaction.dto;

import lombok.Data;

@Data
public class MergeOlMappingDto {
	
	private Integer id;
	private String offerLetter ;
	private Integer mergeOlNoId;
	private String status;
	private Boolean isMerged;
	
}
