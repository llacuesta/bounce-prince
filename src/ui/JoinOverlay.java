package ui;

import chat.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinOverlay extends JPanel {

    private JTextField ipTextField;
    private JTextField portTextField;
    private boolean joinVisible = false;
    private Client client;

    public JoinOverlay() {
        // IP Address text field
        JLabel ipLabel = new JLabel("Host IP: ");
        ipTextField = new JTextField(15);
        add(ipLabel);
        add(ipTextField);

        // Port text field
        JLabel portLabel = new JLabel("Host Port: ");
        portTextField = new JTextField(5);
        add(portLabel);
        add(portTextField);

        // Join button
        JButton joinButton = new JButton("Join");
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hostIP = ipTextField.getText();
                String hostPort = portTextField.getText();

                // Perform the join operation with the host IP and port
                client.setClientValues(hostIP, Integer.parseInt(hostPort));
                client.startClient();
            }
        });
        add(joinButton);

        setJoinVisible(false);
    }

    public void setJoinVisible(boolean visible) {
        setVisible(visible);
        joinVisible = true;
    }

    public void setJoinClient(Client client) {
        this.client = client;
    }
}
