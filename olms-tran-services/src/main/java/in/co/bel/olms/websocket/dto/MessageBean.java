package in.co.bel.olms.websocket.dto;

import java.util.List;

import lombok.Data;

@Data
public class MessageBean {

	private String messageType;
	private String message;
	private List<Integer> receivers;
	private int sender;
	private String itemNo;
	private String sbuName;
	private String meetingId;
	private int olId;
	private int testId;
	private String olNumber;
	private String lineItemNo;
	
	
	
	public MessageBean(String message) {
		super();
		this.message = message;
	}



	public MessageBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
