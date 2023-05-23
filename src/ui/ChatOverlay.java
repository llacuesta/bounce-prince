package ui;

// Imports
import com.intellij.ui.components.JBScrollPane;
import main.Game;
import main.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatOverlay extends JPanel {

    private JTextArea chatTextArea;
    private JTextField chatTextField;
    private JButton chatSendButton;
    private boolean chatVisible = false;

    public ChatOverlay() {
        setLayout(new BorderLayout());

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);

        chatTextField = new JTextField(20);
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
        add(chatTextField, BorderLayout.SOUTH);

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
    }

    public boolean isChatVisible() { return chatVisible; }
}
