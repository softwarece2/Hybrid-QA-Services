package in.co.bel.olms.upload.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author Manasa B
 * @since 27-10-22
 */
public class UploadUtil {

	public static String getFilePrefx()
	{
		 Date date = Calendar.getInstance().getTime();  
         DateFormat dateFormat = new SimpleDateFormat("ddmmyyyyhhmmss");  
         String strDate = dateFormat.format(date);  
        // System.out.println("Converted String: " + strDate);  
         return strDate;
	}	
}
