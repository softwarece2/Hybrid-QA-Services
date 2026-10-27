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
	
	/* Role constants */
	public static final int CQAE_ADMIN = 2;
	public static final int CQAO = 3;
	public static final int JCQAO = 4;
	public static final int DCQAO = 5;
	public static final int ACQAO = 6;
	public static final int INSPECTOR = 7;
	public static final int SBU_ADMIN = 9;
	
	/**
	 * @author Shubham Anand
	 * Insp phase activities constants
	 */
	public static final int PRE_INSP_PHASE = 1;
	public static final int IN_PROCESS_INSP_PHASE = 2;
	public static final int POST_INSP_PHASE = 3;

	public static final int CONCURRENT_LOGIN = 1;
	
	public static final int INSPECTION_AGENCY = 1;
	public static final int IB = 2;
	public static final int IG = 3;
	public static final String SEPERATOR = "/";
	
	public static final String OL_ITEM_ATTACHMENT_BY_FIRM = "Firm";
	public static final String OL_ITEM_ATTACHMENT_BY_INSPECTOR = "Inspector";
	public static final String ATE_REPORTS = "ate_reports";
	public static final String ALLOWED_CHARS_PATTERN = "^[a-zA-Z0-9_\\.\\s\\-_]+$";
	
}
