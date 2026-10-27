package in.co.bel.olms.transaction.service;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import in.co.bel.olms.transaction.dto.OfferLetterDto;
public class OfferLetterStatusComparator implements Comparator<OfferLetterDto> {
	private static final Map<String, Integer> statusOrder = new HashMap<>();

    static {
        statusOrder.put("Pending", 1);
        statusOrder.put("In Progress", 2);
        statusOrder.put("Approved", 3);
        statusOrder.put("Completed", 4);
        statusOrder.put("Cleared", 5);
        statusOrder.put("Returned", 6);
        statusOrder.put("Rejected", 7);
    }
    @Override
    public int compare(OfferLetterDto o1, OfferLetterDto o2) {
    Integer p1 = statusOrder.getOrDefault(o1.getFileNoteStatus(), Integer.MAX_VALUE);
    Integer p2 = statusOrder.getOrDefault(o2.getFileNoteStatus(), Integer.MAX_VALUE);
    return p1.compareTo(p2);
    } 
}




