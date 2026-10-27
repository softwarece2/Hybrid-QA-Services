package in.co.bel.olms.validator;

public class InputNameValidation {
	public static boolean checkString(String s) {
		return s.matches("^[a-zA-Z0-9(),&\\s./_-]+$");
		// return s.matches("^[a-zA-Z0-9(),&. /-]*$") ;
	}

	public static boolean checkRemarks(String s) {
		return s.matches("^[a-zA-Z0-9(),&.\"/%@_+=*↑-]*$");
	}
}
