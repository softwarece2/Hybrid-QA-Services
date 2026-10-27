package in.co.bel.olms.websocket;

/*
 * @author:Awadhesh Verma
 * @date: 27-01-2023
 */


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(final MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic","/user");
		registry.setApplicationDestinationPrefixes("/app");
		registry.setUserDestinationPrefix("/user");
	}

	@Override
	public void registerStompEndpoints(final StompEndpointRegistry registry) {
		registry.addEndpoint("/olms-websocket")
        		.setAllowedOriginPatterns("**")
//                .setHandshakeHandler(new UserHandshakeHandler())
//                .addInterceptors(new HttpHandshakeInterceptor())
				.withSockJS();
				registry.addEndpoint("/freqscan-websocket")
 				.setAllowedOriginPatterns("**")
 				.withSockJS();
	}
}
