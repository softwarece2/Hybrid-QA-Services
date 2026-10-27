package in.co.bel.olms.master.exception;
import java.io.Serializable;

public class DataInUseException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 1L;
    public DataInUseException(String message) {
        super(message);
    }
}

