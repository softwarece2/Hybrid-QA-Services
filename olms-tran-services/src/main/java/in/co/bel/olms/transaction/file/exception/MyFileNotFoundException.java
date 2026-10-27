package in.co.bel.olms.transaction.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Awadhesh Verma
 * @since Feb 14, 2023 The exception class for handling file not found exceptions
 * 
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -317259993446167278L;

	public MyFileNotFoundException(String message) {
		super(message);
	}
	
	public MyFileNotFoundException(String message,Throwable cause) {
		super(message,cause);
	}
}
