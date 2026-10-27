package in.co.bel.olms.constants;

/**
 * @author Manasa B
 * @since 27-10-22
 */
public class ApplicationConstants {

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String ALREADYEXISTS = "data already exists";
	
	
	/**
	 * User authentication and authorization constants
	 * @author Manasa B
	 */	
	public static final String RETURN_INVALID_INPUT = "Invalid Input";
	public static final String LOGIN_SECRET_KEY = "my&ecretKey12345";
	
	
	
	/**
	 * @author Manasa B
	 * Constants for files location
	 */
	public static final String LOCATION_SO = "so";
	public static final String LOCATION_OL = "ol";
	public static final String LOCATION_PL = "pl";
	public static final String LOCATION_LI = "li";
	public static final String LOCATION_WT = "WARRANT_ALLOTMENT_TYPE";
	
	/* Duration type constants */
	public static final int DAY = 1;
	public static final int MONTHS = 2;
	public static final int YEARS = 3;
	
	/* Status constants */
	public static final String ACTIVE = "Active";
	public static final String INACTIVE = "Inactive";
	
	public static final String SUPPLY_ORDER = "SupplyOrder";
	public static final String SALE_ORDER = "SaleOrder";
	public static final String OFFER_LETTER = "OfferLetter";
	public static final String SUPPLY_ORDER_APPROVAL = "SupplyOrderApproal";
	public static final String INSP_FILE_NOTE = "InspFileNote";
	public static final String SO_ALLOTMENT = "SoAllotment";
	public static final String SEPERATOR = "/";
	public static final String RECIEVED = "Recieved";
	public static final String NOTIFIED = "Notified";
	/* Role constants */
	public static final int CQAE_ADMIN = 2;
	public static final int CQAO = 3;
	public static final int JCQAO = 4;
	public static final int DCQAO = 5;
	public static final int ACQAO = 6;
	public static final int INSPECTOR = 7;
	public static final int SBU_ADMIN = 9;
	
	public static final int OL_APPROVED = 1; //Inspection File Note Approval
	public static final int OL_REJECTED = 2; //Inspection File Note Rejection
	public static final int OL_CLEARED = 3;// INote generated for all the lineitems in OL
	public static final int OL_PENDING = 4;//Notified to CQAE
	public static final int OL_INPROGRESS = 5;//Allotted to Inspector and inspection started 
	public static final int OL_RETURNED = 6;//OL Returned
	public static final int OL_COMPLETED = 7;//OL Inspection Completed by Inspector
	
	public static final int APPROVED = 1;
	public static final int REJECTED = 2;

	public static final String OL_ITEM_ATTACHMENT_BY_FIRM = "Firm";
	public static final String OL_ITEM_ATTACHMENT_BY_INSPECTOR = "Inspector";

	public static final String JC_ROLE = "JCQAO";
	public static final String CQAO_ROLE = "CQAO";
	
	public static final String TMR_BY_FIRM = "FIRM";
	public static final String TMR_BY_INSPECTOR = "INSPECTOR";
	public static final String TMR_BY_SURPRISE_CHECK = "SURPRISECHECK";


	public static class REGISTRATION_CONSTANTS {
		public static final String ACCEPTED = "accepted";
		public static final String REJECTED = "rejected";
		public static final String RFR = "rfr";
		public static final String PENDING = "pending";
		public static final String INPROCESS = "in-process";
		public static final String PASS = "pass";
		public static final String FAIL = "fail";
		public static final String CREATED = "Created";
	}

	/* Department constants */
	public static class OlStatus {
		public static final int RETURN_QACL = 13;
	}
	
	public static class SERIAL_NO_STATUS_CONSTANTS {
		public static final Integer CREATED = 1;
		public static final Integer MAPPED = 2;
		public static final Integer SUB_SLIP_CREATED = 3;
		public static final Integer LOT_GENERATED = 4;
		public static final Integer RFR = 5;
		public static final Integer ACCEPTED = 6;
		public static final Integer SCRAP = 7;
		public static final Integer I_NOTE_GENERATED = 8;
		public static final Integer UNMAPPED = 9;
		public static final Integer IN_PROGRESS = 10;
	}

	public static class COMMON_STATUS_CONSTANTS {
		public static final Integer APPROVED = 1;
		public static final Integer REJECTED = 2;
		public static final Integer CLEARED = 3;
		public static final Integer PENDING = 4;
		public static final Integer IN_PROGRESS = 5;
		public static final Integer RETURNED = 6;
		public static final Integer COMPLETED = 7;
		public static final Integer RFR = 8;
		public static final Integer I_NOTE_GENERATED = 9;
		public static final Integer RECTIFICATION = 10;
		public static final Integer PASS = 11;
		public static final Integer FAIL = 12;
		public static final Integer RETURN_QACL = 13;
		public static final Integer CREATED = 14;
    public static final Integer QCC_GENERATED = 15;
    public static final Integer DOA_APPROVED = 16;
    public static final Integer DOQC_APPROVED = 17;
    public static final Integer INT_INOTE_GENERATED = 18;
    public static final Integer OL_GENERATED = 19;
	}
	
	public static class SO_STATUS_CONSTANTS {
		public static final Integer APPROVED = 1;
		public static final Integer REJECTED = 2;
		public static final Integer SCRUTINY_APPROVED = 3;
		public static final Integer COMPLETED = 4;
		public static final Integer CREATED = 5;
		public static final Integer IN_PROGRESS = 6;
		public static final Integer NOTIFIED = 7;
		public static final Integer NOTIFIED_TO_HOSA = 8;
		public static final Integer NOTIFIED_TO_GICA = 9;
	}
	
	public static class QCC_STATUS {
		public static final Integer GENERATED = 1;
		public static final Integer DOA_APPROVED = 2;
		public static final Integer DO_QC_APPROVED = 3;
	}

	public static class AMENDMENT_LETTER_TYPE {
		public static final Integer SUPPLY_ORDER = 1;
		public static final Integer SO_LINE_ITEM = 2;
	}
	
	public static class REGISTRATION_NO_TYPE {
		public static final Integer REGISTRATION_NUMBER = 1;
		public static final Integer RUNNING_NUMBER = 2;
		public static final Integer WITHOUT_REGISTRATION_NUMBER = 3;
	}
	
	//MERGE_SUB_SLIP_STATUS
	public static class LOT_STATUS {
		public static final Integer INT_INOTE_GENERATED = 18;
		public static final Integer INOTE_GENERATED = 9;
	}
	
	public static final String INOTE_PREFIX = "CQ/DD/OLF/DD/";
	
	public static final String ALLOWED_CHARS_PATTERN = "^[a-zA-Z0-9_\\.\\s\\-_]+$";
	
	public static final String FILES_PATH_SEGMENT = "files/";

}
