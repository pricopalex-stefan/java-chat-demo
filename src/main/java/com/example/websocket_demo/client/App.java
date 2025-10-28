package com.example.websocket_demo.client;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    String username = JOptionPane.showInputDialog(null,
                            "Please enter your username: (Max: 13 Characters)",
                            "Chat App",
                            JOptionPane.QUESTION_MESSAGE);

                    if (username == null || username.isEmpty() || username.length() > 13) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Username",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                }


                ClientGUI clientGUI = null;
                try {
                    clientGUI = new ClientGUI("Alex");
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                clientGUI.setVisible(true);
            }
        });
    }
}
