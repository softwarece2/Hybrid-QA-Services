package in.co.bel.core.constants;

public class MasterConstants {

	public static class EMP_ROLE {
		public static final int ID_SUPER_ADMIN = 1;
		public static final int ID_CQAE_ADMIN = 2;
		public static final int ID_CQAO = 3;
		public static final int ID_JCQAO = 4;
		public static final int ID_DCQAO = 5;
		public static final int ID_ACQAO = 6;
		public static final int ID_INSPECTOR = 7;
		public static final int ID_COMP_ADMIN = 8;
		public static final int ID_SBU_ADMIN = 9;
		public static final int ID_QA = 10;
		public static final int ID_SALES_ADMIN = 11;
		public static final int ID_TESTING = 12;
		public static final int ID_DDE = 13;
		public static final int ID_MIS_ADMIN = 14;
		public static final int ID_HOSQC = 14;
		public static final int ID_EXAMINER = 16;
		public static final int ID_DO = 18;
		
	}

	public static class PoObsStatus {

		private PoObsStatus() {
			throw new IllegalStateException("PoObsStatus Utility class");
		}

		public static final int ID_CREATED = 1;
		public static final int ID_OPA_SUBMITTED = 2;
		public static final int ID_CLOSED = 3;
	}
	
	/**
	 * @author Shubham Anand
	 * Insp phase activities constants
	 */
	public static final int PRE_INSP_PHASE = 1;
	public static final int IN_PROCESS_INSP_PHASE = 2;
	public static final int POST_INSP_PHASE = 3;
}
