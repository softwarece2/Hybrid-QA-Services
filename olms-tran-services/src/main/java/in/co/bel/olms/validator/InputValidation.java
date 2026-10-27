package in.co.bel.olms.validator;

public class InputValidation {

	public static boolean checkRemark(String s)
	{
		return s.matches("^[a-zA-Z0-9+%\\-\\&@\\(\\)\\{\\}\\[\\]?*`\"',./\\s\\n\\)\\(]*$");


                   // return s.matches("^[a-zA-Z0-9(),&. /-]*$") ;
	}
	
}
