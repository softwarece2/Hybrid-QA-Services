package in.co.bel.olms.validator;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class InputNameValidation {
	private static final String[] ALLOWED_TAGS = { "div", "b", "br" };

	public static boolean checkString(String s) {
		return s.matches("^[a-zA-Z0-9()\\s./-]+$");
		// return s.matches("^[a-zA-Z0-9(),&. /-]*$") ;
	}

	public static boolean checkRemarks(String s) {
		return s.matches("^[-a-zA-Z0-9(),&.\"/%@_+=*↑\n: ]+$");
	}

	public static boolean checkNumbers(String s) {
		return s.matches("^[0-9]+$");
	}

	public static boolean isHtmlSafe(String input) {
		// Regular expression to match HTML tags
		String tagRegex = "<(/?)(\\w+)([^>]*?)>";

		Pattern pattern = Pattern.compile(tagRegex);
		Matcher matcher = pattern.matcher(input);

		while (matcher.find()) {
			String tagName = matcher.group(2).toLowerCase();

			boolean isAllowed = false;
			for (String allowedTag : ALLOWED_TAGS) {
				if (allowedTag.equals(tagName)) {
					isAllowed = true;
					break;
				}
			}
			if (!isAllowed) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean checkVigilanceRemarks(String s) {
		return s.matches("^[a-zA-Z0-9(),&.\\\\\"/%@_+=*↑:?\\-\\n ]*$");
		 
		 }
}
