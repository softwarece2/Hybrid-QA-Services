package in.co.bel.olms.util;

import java.time.LocalDate;
import java.time.Month;

public class TdmsUtil {

	
	 public static String getFinancialYearStr() {
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		Month month = currentDate.getMonth();

		// If the current month is from January to March, the financial year belongs to
		// the previous calendar year.
		if (month.getValue() >= 1 && month.getValue() <= 3) {
			year = year - 1;
		}

		// The financial year is represented by the last two digits of the starting
		// year.
		return String.valueOf(year % 100);
	}
	 
	 static String getFinancialYearStrForInote() {
		LocalDate currentDate = LocalDate.now();
		int year = currentDate.getYear();
		Month month = currentDate.getMonth();

		// If the current month is from January to March, the financial year belongs to
		// the previous calendar year.
		if (month.getValue() >= 1 && month.getValue() <= 3) {
			year = year - 1;
		}

		// The financial year is represented by the last two digits of the starting
		// year.
		 return String.valueOf(year%100);
	}
	
}
