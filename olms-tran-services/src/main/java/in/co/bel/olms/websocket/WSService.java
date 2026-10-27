package in.co.bel.olms.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * @author:Awadhesh Verma
 * @date: 27-01-2023
 */
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import in.co.bel.olms.websocket.dto.MessageBean;

@Service
public class WSService {

	private final SimpMessagingTemplate messagingTemplate;
	
	Logger logger = LoggerFactory.getLogger(WSService.class);

	public WSService(SimpMessagingTemplate messagingTemplate) {

		this.messagingTemplate = messagingTemplate;

	}
	
	public boolean notifyCqae(final MessageBean message) {
		
		boolean response = true;
		try {
			messagingTemplate.convertAndSend("/topic/cqae", message);
			logger.info("Message sent to CQAE");
		} catch (Exception e) {
			response = false;
		}
		return response;
	}

	public void notifyBel(final String id, final MessageBean message) {

		messagingTemplate.convertAndSendToUser(id, "/topic/bel", message);

	}
	public void sendPrivateMessage(final MessageBean message) {
 		for (int i=0 ; i<message.getReceivers().size();i++){
 			messagingTemplate.convertAndSendToUser(message.getReceivers().get(i).toString(), "/private", message);
 		}
 	}

}
