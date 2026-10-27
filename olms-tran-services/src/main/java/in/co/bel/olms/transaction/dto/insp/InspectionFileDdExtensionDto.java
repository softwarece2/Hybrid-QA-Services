package in.co.bel.olms.transaction.dto.insp;

import java.util.Date;

import lombok.Data;

@Data
public class InspectionFileDdExtensionDto {
	private int inspectionFileNoteId;
	private Date extendedDd;
	private Boolean provDd;
	private Boolean formalDd;
}
