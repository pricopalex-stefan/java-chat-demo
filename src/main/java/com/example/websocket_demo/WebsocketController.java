package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate, WebSocketSessionManager sessionManager) {
        this.messagingTemplate = messagingTemplate;
        this.sessionManager = sessionManager;
    }

    // handles incoming chat messages from clients sent to "/app/messages"
    @MessageMapping("/messages")
    public void handleMessage(Message message) {
        System.out.println("Received message from user: " + message.getUser() + ": " + message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Sent message to /topic/messages: " + message.getUser() + ": " + message.getMessage());
    }

    // called when a users connect -> adds username to active users list and notifies all connected clients
    @MessageMapping("/connect")
    public void connectUser(String username){
        sessionManager.addUsername(username);
        sessionManager.broadcastActiveUsers();
        System.out.println(username + " connected");
    }

    // called when a users disconnect -> removes username from active users list and notifies all connected clients
    @MessageMapping("/disconnect")
    public void disconnectUser(String username){
        sessionManager.removeUsername(username);
        sessionManager.broadcastActiveUsers();
        System.out.println(username + " disconnected");
    }

    /*
    * called after a new client subscribes to the users topic
    * so it ensures the new client receives the list of active users
    */
    @MessageMapping("/request-users")
    public void requestUsers(){
        sessionManager.broadcastActiveUsers();
    }
}
