package com.example.websocket_demo.client;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClientGUI clientGUI = new ClientGUI("Alex");
                clientGUI.setVisible(true);
            }
        });
    }
}
