package com.example.websocket_demo.client;

import com.example.websocket_demo.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyStompClient {
    private StompSession session;
    private String username;

    // Initializing STOMP client to communicate with the server using WebSocket / SockJS
    public MyStompClient(String username) throws ExecutionException, InterruptedException {
        this.username = username;

        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));

        SockJsClient socketJsClient = new SockJsClient(transports);

        WebSocketStompClient stompClient = new WebSocketStompClient(socketJsClient);

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSessionHandler stompSessionHandler = new MyStompSessionHandler(username);
        String url = "ws://localhost:8080/websocket";

        session = stompClient.connectAsync(url, stompSessionHandler).get();
    }

    public void sendMessage(Message message) {
        try {
            session.send("/app/messages", message);
            System.out.println("Sent message: " + message.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String username) {
        session.send("/app/disconnect", username);
        System.out.println("Disconnected user: " + username);
    }
}
