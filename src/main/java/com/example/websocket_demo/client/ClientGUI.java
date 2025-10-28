package com.example.websocket_demo.client;
import com.example.websocket_demo.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ClientGUI extends JFrame implements MessageListener {
    private JPanel connectedUsersPanel, messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane scrollPane;

    public ClientGUI(String username) throws ExecutionException, InterruptedException {
        super("Chat-App Client");
        this.username = username;
        myStompClient = new MyStompClient(this, username);

        setSize(1280, 860);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        // confirm popup before closing the app
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(ClientGUI.this, "Quit?",
                        "Exit app", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    myStompClient.disconnectUser(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateMessageSize();
            }
        });

        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    private void addGuiComponents() {
        addConnectedUsersComponents();
        addChatComponents();
    }

    private void addConnectedUsersComponents() {
        connectedUsersPanel = new JPanel();
        connectedUsersPanel.setLayout(new BoxLayout(connectedUsersPanel, BoxLayout.Y_AXIS));
        connectedUsersPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUsersPanel.setOpaque(true);
        connectedUsersPanel.setPreferredSize(new Dimension(200, getHeight()));

        EmptyBorder padding = new EmptyBorder(5, 10, 5, 5);
        connectedUsersPanel.setBorder(padding);

        JLabel connectedUsersLabel = new JLabel("Connected Users");
        connectedUsersLabel.setFont(new Font("Inter", Font.BOLD, 18));
        connectedUsersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUsersPanel.add(connectedUsersLabel);

        add(connectedUsersPanel, BorderLayout.WEST);

    }

    private void addChatComponents() {
        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                revalidate();
                repaint();
            }
        });

        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        // field where the user types a message
        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    String input = inputField.getText();

                    // the user cannot send empty messages
                    if (input.isEmpty()) return;

                    inputField.setText("");

                    myStompClient.sendMessage(new Message(username, input));
                }
            }
        });
        inputField.setBackground(Utilities.SECONDARY_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setFont(new Font("Inter", Font.BOLD, 18));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(), 70));
        inputPanel.add(inputField, BorderLayout.CENTER);
        chatPanel.add(inputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);
    }

    private JPanel createChatMessageComponent(Message message) {
        // panel that holds chat messages and input area
        JPanel chatMessage = new JPanel();
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR);
        chatMessage.setLayout(new BorderLayout());
        chatMessage.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel usernameLabel = new JLabel(message.getUser() + ":");
        usernameLabel.setFont(new Font("Inter", Font.BOLD, 18));
        usernameLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(usernameLabel, BorderLayout.NORTH);

        JTextArea messageArea = new JTextArea(message.getMessage());
        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setEditable(false);
        messageArea.setBackground(Utilities.TRANSPARENT_COLOR);
        messageArea.setForeground(Utilities.TEXT_COLOR);
        messageArea.setFont(new Font("Inter", Font.PLAIN, 16));
        messageArea.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chatMessage.add(messageArea, BorderLayout.CENTER);

        return chatMessage;
    }

    @Override
    public void onMessageReceived(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();

        scrollPane.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> users) {
        // remove the current user list panel
        if(connectedUsersPanel.getComponentCount() >= 2) {
            connectedUsersPanel.remove(1);
        }

        JPanel userListPanel = new JPanel();
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);
        userListPanel.setLayout(new BoxLayout(userListPanel, BoxLayout.Y_AXIS));

        for(String user : users) {
            JLabel userLabel = new JLabel();
            userLabel.setText(user);
            userLabel.setForeground(Utilities.TEXT_COLOR);
            userLabel.setFont(new Font("Inter", Font.BOLD, 16));
            userListPanel.add(userLabel);
        }

        connectedUsersPanel.add(userListPanel);
        revalidate();
        repaint();
    }

    private void updateMessageSize() {
        revalidate();
        repaint();
    }
}