package in.co.bel.olms.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import in.co.bel.olms.master.exception.DataNotValidException;

public class DateValidator {
	private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    
    

    static {
        sdf.setLenient(false); // Ensures strict date validation
    }
    
    public static void validateDate(String dateString) throws DataNotValidException {
        if (dateString == null || dateString.isEmpty()) {
            return; 
        }
        try {
            sdf.parse(dateString); 
        } catch (ParseException e) {
            throw new DataNotValidException("Invalid date format");
        }
    }

}
