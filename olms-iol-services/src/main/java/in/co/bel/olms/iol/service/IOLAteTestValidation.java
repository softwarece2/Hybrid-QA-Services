package in.co.bel.olms.iol.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IOLAteTestValidation {

	private static final Logger logger = LoggerFactory.getLogger(IOLAteTestValidation.class);

	public Double extractNumericValue(String input) {
		Pattern pattern = Pattern.compile("-?\\d*\\.?\\d+");
		Matcher matcher = pattern.matcher(input);
		StringBuilder extractedNumbers = new StringBuilder();
		while (matcher.find()) {
			if (!extractedNumbers.isEmpty()) {
				extractedNumbers.append(" "); // Add space between numbers
			}
			extractedNumbers.append(matcher.group()); // Append the matched number
		}
		String result = extractedNumbers.toString().trim();
		if (!result.isEmpty()) {
			try {
				return Double.parseDouble(result);
			} catch (NumberFormatException e) {
				 logger.error("Failed to parse extracted numeric string: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}
	
	public Integer extractNumericValues(String input) {
	    // Pattern to match all numeric values in the string
	    Pattern pattern = Pattern.compile("\\d+");
	    Matcher matcher = pattern.matcher(input);
	    
	    Integer lastNumber = null;

	    // Find all matches and keep the last one
	    while (matcher.find()) {
	        lastNumber = Integer.parseInt(matcher.group());
	    }

	    return lastNumber;
	}
	
	public String extractHyphens(String input) {
		// Regular expression to match hyphens not surrounded by alphabetic characters
		Pattern pattern = Pattern.compile("(?:^|[^a-zA-Z])(-+)(?:[^a-zA-Z]|$)");
		Matcher matcher = pattern.matcher(input);
		StringBuilder extractedHyphens = new StringBuilder();

		while (matcher.find()) {
			// Extract the matched hyphens group
			extractedHyphens.append(matcher.group(1));
		}

		return extractedHyphens.toString();
	}

	public Integer extractNumericValueDs(String input) {
		String lastMatchedNumber = null;
	    Pattern pattern = Pattern.compile(".*-\\s*(\\d+\\.?\\d*)\\s*M?$");
	    Matcher matcher = pattern.matcher(input);

	    while (matcher.find()) {
	        lastMatchedNumber = matcher.group(1);
	    }

	    if (lastMatchedNumber != null) {
	        try {
	            Double doubleValue = Double.parseDouble(lastMatchedNumber);
	            return doubleValue.intValue();
	        } catch (NumberFormatException e) {
	        	logger.error("Failed to parse extracted value: {}", lastMatchedNumber, e);
	            return null;
	        }
	    }
	    return null;
	}


	public Double extractNumericValuePhCur(String input) {
	    Pattern pattern = Pattern.compile("-?\\s*(\\d*\\.?\\d+)\\s*(A)?\\s*$");
	    Matcher matcher = pattern.matcher(input.trim());
	    String lastMatchedNumber = null;
	    while (matcher.find()) {
	        if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
	            lastMatchedNumber = matcher.group(1);
	        }
	    }
	    if (lastMatchedNumber != null && !lastMatchedNumber.isEmpty()) {
	        try {
	            return Double.parseDouble(lastMatchedNumber);
	        } catch (NumberFormatException e) {
	        	logger.error("Failed to parse extracted value: {}", e.getMessage());
	            return null;
	        }
	    }
	    return null;
	}

	public Double extractMechSpPhCurrLastNumericValue(String input) {
	    Pattern pattern = Pattern.compile("-?\\s*(\\d+(\\.\\d*)?)\\s*(A)?\\s*$");
	    Matcher matcher = pattern.matcher(input);
	    String lastMatchedNumber = null;

	    while (matcher.find()) {
	        if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
	            lastMatchedNumber = matcher.group(1);
	        }
	    }
	    if (lastMatchedNumber != null && !lastMatchedNumber.isEmpty()) {
	        try {
	            return Double.parseDouble(lastMatchedNumber);
	        } catch (NumberFormatException e) {
	        	logger.error("Failed to parse extracted value: {}", e.getMessage());
	            return null;
	        }
	    }
	    return null;
	}

	
	public Double extractDouble(String input) {
		// Regular expression to match numeric values
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(input);
		String lastMatchedNumber = null;

		while (matcher.find()) {
			lastMatchedNumber = matcher.group();
		}

		if (lastMatchedNumber != null) {
			try {
				return Double.parseDouble(lastMatchedNumber);
			} catch (NumberFormatException e) {
				logger.error("Failed to parse extracted value: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}

	public Double extractLastDouble(String input) {
		// Regular expression to match decimal numbers
		Pattern pattern = Pattern.compile("\\d+\\.\\d+");
		Matcher matcher = pattern.matcher(input);
		String lastMatchedNumber = null;

		while (matcher.find()) {
			lastMatchedNumber = matcher.group();
		}

		if (lastMatchedNumber != null) {
			try {
				return Double.parseDouble(lastMatchedNumber);
			} catch (NumberFormatException e) {
				logger.error("Failed to parse extracted value: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}

	public String extractAlphabeticCharacters(String input) {
		// Regular expression to match sequences of alphabetic characters surrounded by
		// non-alphabetic characters or spaces
		Pattern pattern = Pattern.compile("(?<=\\W|^)\\p{Alpha}+(?=\\W|$)");
		Matcher matcher = pattern.matcher(input);
		StringBuilder extractedAlphabets = new StringBuilder();

		while (matcher.find()) {
			extractedAlphabets.append(matcher.group());
		}

		return extractedAlphabets.toString();
	}

	public Double extractNumericValueFromTime(String input) {
		String cleaned = input.replaceAll("[a-zA-Z\\-\\s]", "");
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

		Matcher matcher = pattern.matcher(cleaned);
		StringBuilder extractedNumbers = new StringBuilder();
		while (matcher.find()) {
			if (!extractedNumbers.isEmpty()) {
				extractedNumbers.append(" "); // Add space between numbers
			}
			extractedNumbers.append(matcher.group()); // Append the matched number
		}
		String result = extractedNumbers.toString().trim();
		if (!result.isEmpty()) {
			try {
				return Double.parseDouble(result);
			} catch (NumberFormatException e) {
				logger.error("Failed to parse extracted value: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}

	public String extractDevNameInfo(String input) {
		Pattern pattern = Pattern.compile("\\bDevice No\\.\\:\\s*(\\w+\\s*\\d*)?");
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}

	public String extractDate(String input) {
		Pattern pattern = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}

	public Double extractNumericValueFromTimeDs(String input) {
		Pattern pattern = Pattern.compile("\\b810\\b");
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			try {
				return Double.parseDouble(matcher.group());
			} catch (NumberFormatException e) {
				logger.error("Failed to parse extracted value: {}", e.getMessage());
				return null;
			}
		}
		return null;
	}

	public String extractBid(String input) {
		Pattern pattern = Pattern.compile("\\s----\\s");
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group().trim();
		}
		return null;
	}

	public String extractNumFromCurrent(String input) {
		String cleaned = input.replaceAll("[a-zA-Z<=>\\s]", "");
		Pattern pattern = Pattern.compile("\\(.*\\)\\s*-\\s*\\d+(\\.\\d+)?");
		Matcher matcher = pattern.matcher(cleaned);
		if (matcher.find()) {
			return matcher.group(); // Return the matched substring
		} else {
			return null; // Return null if no match found
		}
	}

	public Double extractNumFromVolt(String input) {
		Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)|([+\\-*/])");
		Matcher matcher = pattern.matcher(input);

		Double firstNumber = null;
		Double secondNumber = null;
		String operator = null;

		while (matcher.find()) {
			String match = matcher.group();
			if (match.matches("\\d+(\\.\\d+)?")) {
				if (firstNumber == null) {
					firstNumber = Double.parseDouble(match);
				} else {
					secondNumber = Double.parseDouble(match);
				}
			} else if (match.matches("[+\\-*/]")) {
				operator = match;
			}
		}

		if (firstNumber != null && secondNumber != null && operator != null) {
			return performOperation(firstNumber, secondNumber, operator);
		} else if (firstNumber != null && operator == null) {
			return firstNumber;
		} else {
			return null;
		}
	}

	public Double performOperation(Double num1, Double num2, String operator) {
		switch (operator) {
		case "+":
			return num1 + num2;
		case "-":
			return num1 - num2;
		case "*":
			return num1 * num2;
		case "/":
			if (num2 != 0) {
				return num1 / num2;
			} else {
				logger.error("Division by zero is not allowed.");
				return null;
			}
		default:
			return null;
		}
	}
}
