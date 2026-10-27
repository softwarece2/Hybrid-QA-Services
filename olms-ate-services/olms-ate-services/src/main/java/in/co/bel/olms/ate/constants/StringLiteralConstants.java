package in.co.bel.olms.ate.constants;

public final class StringLiteralConstants {
	
	
	private StringLiteralConstants() {
        // Throw an error to prevent instantiation via Reflection
        throw new UnsupportedOperationException("This is a utility class for constants and cannot be instantiated");
    }
	
	public static final String DATE_FORMAT = "dd-MM-yyyy";
	public static final String IST_DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss a";
	public static final String IST_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss a";

	public static final String OL_LINEITEM_ID_ERROR_STRING = "Error: olLineItemId ";
	public static final String OL_LINEITEM_ID_SERIAL_NO_ERROR_STRING = "Error: olLineItemSlNoId ";
	public static final String CREATED_BY_ID_ERROR_STRING = "Error: createdById ";
	public static final String DEV_ID_ERROR_STRING = "Error: devId ";
	public static final String APPLICABLE_TEST_ID_ERROR_STRING = "Error: applicableTestId ";
	public static final String OL_LINEITEM_ID_NOT_PRESENT_STRING = " not present in OL_LINE_ITEM table in database.";
	public static final String OL_LINEITEM_ID_SERIAL_NO_NOT_PRESENT_STRING = " not present in OL_LINE_ITEM_SERIAL_NO table in database.";
	public static final String EMPLOYEE_NOT_PRESENT_STRING = " not present in EMPLOYEE table in database.";
	public static final String APPLICABLE_TEST_NOT_PRESENT_STRING = " not present in applicable test table in database.";
	public static final String DEV_ID_ALREADY_EXIST_STRING = " already exists in the database table, cannot save records for duplicate dev names.";
}
