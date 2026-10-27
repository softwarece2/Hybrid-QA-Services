package in.co.bel.olms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Chirag Swamy
 * @since 29-09-26
 * 
 * Custom exception for cases where a requested resource 
 * does not exist in the database.
 */
@ResponseStatus(HttpStatus.NOT_FOUND) 
public class ResourceNotFoundException extends RuntimeException {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8704135420354869184L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
