package com.example.demo.config;

import java.security.Principal;
import java.util.Map;
import java.util.Random;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/questions").setHandshakeHandler(new RandomHandShakeHandler()).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app")
			.enableSimpleBroker("/topic", "/queue");
		//.enableStompBrokerRelay("/topic", "/queue")
		;
	}
	
	private class RandomHandShakeHandler extends DefaultHandshakeHandler{

		private String[] ADJECTIVES = {"aggressive", "annoyed", "black", "bootiful", "crazy", "elegant", "little", "old-fashioned", 
				"secret", "sleepy", "toxic"};
		private String[] NOUNS = {"agent", "american", "anaconda", "caiman", "crab", "flamingo", "gorilla", "king", "kitten", 
				"penguin", "runner", "warrior"};
		
		@Override
		protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
				Map<String, Object> attributes) {
			
			String username = ADJECTIVES[new Random().nextInt(ADJECTIVES.length)]
					+"-"+NOUNS[new Random().nextInt(NOUNS.length)]+"-"+new Random().nextInt(ADJECTIVES.length);
			return new UsernamePasswordAuthenticationToken(username, null);
		}
		
		
		
	}

}
