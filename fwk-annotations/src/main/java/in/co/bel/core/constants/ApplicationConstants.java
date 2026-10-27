package in.co.bel.core.constants;

public class ApplicationConstants {

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String DATA_NOT_FOUND = "data not found";
	
	public static final String EMPTY = "EMPTY";
	public static final String YES = "Yes";
	public static final String NO = "No";
	
	public static final String ACTIVE ="Active";
	public static final String INACTIVE = "Inactive";
	
	public static class Gender{
		public static final int ID_MALE =1;
		public static final int ID_FEMALE =2;
		public static final int ID_TRANSGENDER =3;
	}
	
	public static final String DATE_FORMAT_INPUT ="yyyy-MM-dd";
	
	public static final String SUPPLY_ORDER = "SupplyOrder";
	public static final String SALE_ORDER = "SaleOrder";
	public static final String OFFER_LETTER = "OfferLetter";
	
	/* Role constants */
	public static class Role {
		public static final int CQAE_ADMIN = 2;
		public static final int CQAO = 3;
		public static final int JCQAO = 4;
		public static final int DCQAO = 5;
		public static final int ACQAO = 6;
		public static final int INSPECTOR = 7;
		public static final int SBU_ADMIN = 9;
		public static final int HOD = 14;
		public static final int GRP_INCHARGE = 15;
		public static final int EXAMINER = 16;
		public static final int PLANNER = 17;
		public static final int DO = 18;

	}
	
	/* Organization Type constants */
	public static class OrgType {
		public static final int ID_CQAE = 1;
		public static final int ID_BEL = 2;		
		public static final int ID_IG = 3;
	}

	/* Department constants */
	public static class Dept {
		public static final int PLANNING = 1;
		public static final int ASSEMBLY = 2;
		public static final int QC = 3;
	}
	
	public static final String ALLOWED_CHARS_PATTERN = "^[a-zA-Z0-9_\\.\\s\\-_]+$"; 
	
}
