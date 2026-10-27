package in.co.bel.olms.transaction.service.insp;
import java.util.Comparator;

import in.co.bel.olms.transaction.dto.insp.InspectionFileNoteDto;

public class FileNoteStatusComparator implements Comparator<InspectionFileNoteDto> {
	 @Override
	    public int compare(InspectionFileNoteDto d1, InspectionFileNoteDto d2) {
		 //if(d1.getFileNoteStatus()!=null && d2.getFileNoteStatus()!=null) {
	        if (d1.getFileNoteStatus()!=null &&d1.getFileNoteStatus().contains("Held") &&d2.getFileNoteStatus()!=null &&d2.getFileNoteStatus().contains("Approved")) {
	            return -1; // d1 should come before d2
	        } else if (d1.getFileNoteStatus().contains("Approved") && d2.getFileNoteStatus().contains("Held")) {
	            return 1; // d1 should come after d2
	        } else {
	            return d1.getFileNoteStatus().compareTo(d2.getFileNoteStatus()); // default comparison
	        }
	  //  }
	 }
}



