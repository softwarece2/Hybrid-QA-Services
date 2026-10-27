package in.co.bel.olms.master.exception;


/**
 * @author Manasa B
 */
public class UserException extends Exception{

	private int messageId;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8304347299453402339L;

	/**
	 * 
	 */

	public UserException(String str) {
		super(str);
	}

	public UserException(String str, int messageId) {
		super(str);
		this.messageId = messageId;
	}

	public int getMessageId() {
		return messageId;
	}
}
