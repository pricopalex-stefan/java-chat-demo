package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/*
* Manages connected WebSocket users
 */
@Service
public class WebSocketSessionManager {
    private final ArrayList<String> activeUsers = new ArrayList<>();
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketSessionManager(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void addUsername(String username) {
        activeUsers.add(username);
    }

    public void removeUsername(String username) {
        activeUsers.remove(username);
    }

    public void broadcastActiveUsers() {
        messagingTemplate.convertAndSend("/topic/users", activeUsers);
        System.out.println("Broadcasting active users to /topic/users " + activeUsers);
    }
}
