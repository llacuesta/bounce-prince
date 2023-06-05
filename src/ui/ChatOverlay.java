package ui;

// Imports
import chat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatOverlay extends JPanel {

    public JTextArea chatTextArea;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private boolean chatVisible = false;
    private Client client;

    public ChatOverlay() {
        setLayout(new BorderLayout());

        chatTextArea = new JTextArea(8, 25);
        chatTextArea.setEditable(false);
        chatTextField = new JTextField(20);
        chatSendButton = new JButton("Send");
        chatTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = chatTextField.getText();
                // Process the message in your game logic class
                processChatMessage(message);
                chatTextField.setText("");
            }
        });
        chatTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setChatVisible(false);
                }
            }
        });
        chatSendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = chatTextField.getText();
                processChatMessage(message);
                chatTextField.setText("");
            }
        });
        add(chatTextArea, BorderLayout.NORTH);
        add(chatTextField, BorderLayout.SOUTH);
        add(chatSendButton, BorderLayout.EAST);

        // Initially, hide the chat panel
        setChatVisible(false);
    }

    public void setChatVisible(boolean visible) {
        setVisible(visible);
        chatTextArea.setVisible(visible);
        chatTextField.setVisible(visible);
        chatVisible = visible;
    }

    public void addMessage(String message) {
        chatTextArea.append(message + "\n");
        chatTextArea.setCaretPosition(chatTextArea.getDocument().getLength());
    }

    private void processChatMessage(String message) {
        client.serverHandler(message);
    }

    public boolean isChatVisible() { return chatVisible; }

    public void setChatClient(Client client) {
        this.client = client;
        client.setTextArea(this);
    }
}
