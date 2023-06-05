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

    public JTextArea chatTextArea, fillerArea;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private boolean chatVisible = false;
    private Client client;

    public ChatOverlay() {
        setBackground(new Color(0,0,0,0));
        setLayout(new BorderLayout());

        JPanel filler = new JPanel();
        filler.setBackground(new Color(0,0,0,0));
        fillerArea = new JTextArea(25, 30);     //adjust rows to move the chat box to the bottom
        fillerArea.setEditable(false);
        fillerArea.setBackground(new Color(0,0,0,0));
        filler.add(fillerArea);

        chatTextArea = new JTextArea(8, 25);
        chatTextArea.setEditable(false);
        chatTextArea.setBackground(new Color(0,0,0,30));
        chatTextArea.setForeground(Color.WHITE);

        chatTextField = new JTextField(20);
        chatTextField.setBackground(new Color(0,0,0,30));
        chatTextField.setForeground(Color.WHITE);

        chatSendButton = new JButton("Send");
        chatSendButton.setBackground(new Color(0,0,0,30));
        chatSendButton.setForeground(Color.WHITE);

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

        add(filler, BorderLayout.NORTH);
        add(chatTextArea, BorderLayout.CENTER);
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
        // Your game logic code to handle the chat message
        // For now, we'll simply display the received message
        addMessage("Player: " + message);
        client.serverHandler(message);
    }

    public boolean isChatVisible() { return chatVisible; }

    public void setChatClient(Client client) {
        this.client = client;
        client.setTextArea(this);
    }
}
