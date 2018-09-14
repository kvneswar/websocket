'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var username = null;
var ws = null;
var stompClient = null;

var colors = [
	'#2196F3', '#32c787', '#00BCD4', '#ff5652',
	'#ffc107', '#ff85af', '#FF9800', '#39bbb0'
	];

function connect(event) {
	username = document.querySelector('#name').value.trim();
	if(username) {
		usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');
	}
	event.preventDefault();
	connectingElement.classList.add('hidden');
	
	//WebSocket
	//ws = new WebSocket('ws://localhost:8080/questions');
	ws = new SockJS('/questions');
	/*
	 ws.onmessage = function(data){
		console.log(data);
		
		var messageElement = document.createElement('li');
		messageElement.classList.add('chat-message');

	    var avatarElement = document.createElement('i');
	    var avatarText = document.createTextNode(username[0]);
	    avatarElement.appendChild(avatarText);
	    avatarElement.style['background-color'] = getAvatarColor(username);

	    messageElement.appendChild(avatarElement);

	    var usernameElement = document.createElement('span');
	    var usernameText = document.createTextNode(username);
	    usernameElement.appendChild(usernameText);
	    messageElement.appendChild(usernameElement);
	    
	    var textElement = document.createElement('p');
	    var messageText = document.createTextNode(data.data);
	    textElement.appendChild(messageText);

	    messageElement.appendChild(textElement);

	    messageArea.appendChild(messageElement);
	    messageArea.scrollTop = messageArea.scrollHeight;
	};
	*/
	stompClient = new Stomp.over(ws);
	stompClient.connect({}, function(frame){
		stompClient.subscribe("/topic/questions", function(message){
			console.log("Received: "+message);
			updateUi(message.body);
		});
		
		stompClient.subscribe("/user/queue/private", function(message){
			console.log("Received private message: "+message);
			//Materialize.toast(message.body, 6000);
		});
		
	}, function(error){
		console.error('STOMP Protocol Error: '+errror);
	});
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();
	
	//WebSocket
    //ws.send(messageContent);
	
	//Subscribing to the topic directly
	//stompClient.send("/topic/questions", {}, messageContent);
	
	//Send the message to controller, let it get processed
	stompClient.send("/app/questions", {}, messageContent);
	messageInput.value = '';
	event.preventDefault();
}

function updateUi(message){
	var messageElement = document.createElement('li');
	messageElement.classList.add('chat-message');

    var avatarElement = document.createElement('i');
    var avatarText = document.createTextNode(username[0]);
    avatarElement.appendChild(avatarText);
    avatarElement.style['background-color'] = getAvatarColor(username);

    messageElement.appendChild(avatarElement);

    var usernameElement = document.createElement('span');
    var usernameText = document.createTextNode(username);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
    
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);