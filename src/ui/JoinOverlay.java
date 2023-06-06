package ui;

import chat.Client;
import chat.GamePlayer;
import entities.Player;
import gamestates.Gamestate;
import gamestates.State;

import javax.swing.*;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

public class JoinOverlay extends JPanel {

    private JTextArea fillerArea;
    private JTextField ipTextField;
    private JTextField portTextField;
    private boolean joinVisible = false;
    private Client client;
    private State state;

    private Player player;
    private GamePlayer gamePlayer;

    public JoinOverlay() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        setBackground(new Color(0,0,0,0));
        // filler
        JPanel filler = new JPanel();
        filler.setBackground(new Color(0,0,0,0));
        fillerArea = new JTextArea(20, 30);     //adjust rows to move the chat box to the bottom
        fillerArea.setEditable(false);
        fillerArea.setBackground(new Color(0,0,0,0));
        filler.add(fillerArea);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(filler, gbc);
        
        // IP Address text field
        JLabel ipLabel = new JLabel("Host IP: ");
        ipLabel.setForeground(Color.WHITE);
        ipTextField = new JTextField(15);
        ipTextField.setBackground(new Color(0,0,0,0));
        ipTextField.setForeground(Color.WHITE);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(ipLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(ipTextField, gbc);

        // Port text field
        JLabel portLabel = new JLabel("Host Port: ");
        portLabel.setForeground(Color.WHITE);
        portTextField = new JTextField(5);
        portTextField.setBackground(new Color(0,0,0,0));
        portTextField.setForeground(Color.WHITE);
        add(portLabel);
        add(portTextField);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(portLabel, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(portTextField, gbc);

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
                setJoinVisible(false);

                // Creating GamePlayer instance
                try {
                    gamePlayer = new GamePlayer(player, InetAddress.getByName(hostIP), Integer.parseInt(hostPort));
                    gamePlayer.setState(state);
                    gamePlayer.startGamePlayer();
                } catch (Exception ex) {
                    System.out.println("something went wrong :((( " + ex);
                }
            }
        });

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(joinButton, gbc);

        setJoinVisible(false);
    }

    public void setJoinVisible(boolean visible) {
        setVisible(visible);
        joinVisible = true;
    }

    public void setJoinClient(Client client) {
        this.client = client;
    }

    public void setGamestate(State state) {
        this.state = state;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GamePlayer getGamePlayer() {
        return this.gamePlayer;
    }

    public boolean isJoinVisible() {
        return joinVisible;
    }
}
