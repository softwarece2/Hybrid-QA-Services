package in.co.bel.olms.exception;


/**
 * @author Manasa B
 * @since 29-10-22
 * Exception for the user 
 */
public class AppSecurityException extends Exception{


	/**
	 * 
	 */
	private static final long serialVersionUID = -850796594769457291L;

	//Constructor
	public AppSecurityException(String str)  
    {  
        super(str);  
    }  
}
