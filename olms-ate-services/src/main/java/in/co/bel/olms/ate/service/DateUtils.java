package in.co.bel.olms.ate.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.co.bel.olms.ate.constants.StringLiteralConstants;

public class DateUtils {

	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
		return date != null ? sdf.format(date) : null;
	}

	public static Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
		return sdf.parse(dateString);
	}
	
	public static String formatDate(Object[] devDet, int index) {
	    SimpleDateFormat sdf = new SimpleDateFormat(StringLiteralConstants.DATE_FORMAT);
	    if (devDet != null && devDet.length > index && devDet[index] instanceof Date) {
	        return sdf.format((Date) devDet[index]);
	    }
	    return null;
	}
	
	public static Date convertDateFormat(Date inputDate, String outputFormat) throws Exception {
	    SimpleDateFormat newFormat = new SimpleDateFormat(outputFormat);
	    String formattedDateString = newFormat.format(inputDate);
	    return newFormat.parse(formattedDateString); // Return as Date
	}
}
