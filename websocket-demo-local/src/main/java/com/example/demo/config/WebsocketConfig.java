package com.example.demo.config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

//@Configuration
//@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(new QuestionHandler(), "/questions").withSockJS();
	}

	
	class QuestionHandler extends TextWebSocketHandler{

		private List<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();
		
		@Override
		public void afterConnectionEstablished(WebSocketSession session) throws Exception {
			webSocketSessions.add(session);
		}

		@Override
		protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
			webSocketSessions.stream().forEach(wsSession -> {
				try {
					wsSession.sendMessage(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		
	}
}
