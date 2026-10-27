package in.co.bel.olms.transaction.dto;
import lombok.Data;

@Data
public class OLDto {
	private int id;
	private String soNumber;
	private String name;
	private String salesGroupName;
	private String projectName;
	private String soDate;
	private String olDate;
	private Float totalValue;
	private Integer totalItem;
	private String fileNoteStatus;
	private Integer iNoteTypeId;
}
