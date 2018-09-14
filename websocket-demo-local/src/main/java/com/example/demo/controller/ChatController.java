package com.example.demo.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

	@MessageMapping("/questions")
	public String processQuestion(String question, Principal principal) {
		return question.toUpperCase() +" - "+ principal.getName();
	}
	
}
