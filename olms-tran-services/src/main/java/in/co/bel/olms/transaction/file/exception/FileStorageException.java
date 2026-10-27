package in.co.bel.olms.transaction.file.exception;

/**
 * @author Awadhesh Verma
 * @since Feb 14, 2023 The exception class for handling file upload related exceptions
 * 
 */

public class FileStorageException extends RuntimeException{

	private static final long serialVersionUID = -6166881738711227856L;

	public FileStorageException(String message) {
		super(message);
	}
	
	public FileStorageException(String message,Throwable cause) {
		super(message,cause);
	}
}
