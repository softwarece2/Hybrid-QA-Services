package in.co.bel.olms.websocket;

import java.util.Arrays;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import in.co.bel.olms.websocket.dto.MessageBean;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/ws")
@RequiredArgsConstructor
public class WSController {

	private final WSService service;

	@PostMapping("/sendMessage")
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public MessageBean greeting(@RequestBody Greeting greeting) throws Exception {
		Thread.sleep(1000); // simulated delay
		String finalMessage = "Hello, " + HtmlUtils.htmlEscape(greeting.getName()) + "!";
		return new MessageBean(finalMessage);
	}

	@PostMapping("/send-private-message/{id}")
	public void sendPrivateMessage(@PathVariable final String id, @RequestBody final MessageBean message) {
		service.notifyBel(id, message);
	}

	@GetMapping("/send")
	public void send() {
		MessageBean message = new MessageBean();
		message.setSender(2);
		message.setMessageType("OL");
		message.setItemNo("ABC");
		message.setMessage("New Supply Order created");
		message.setReceivers(Arrays.asList(1, 2));
		service.notifyCqae(message);
	}

	@MessageMapping("/private-message")
 	public MessageBean recMessage(@Payload MessageBean message){
 		service.sendPrivateMessage(message);
 		return message;
 	}
}

@Data
class Greeting {

	private String name;

}
