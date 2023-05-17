package main;

// Imports
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import chat.Client;
import chat.Server;

import javax.swing.*;

//import gamestates.JButton;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;

	// Constructor
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;

		setPanelSize();

		// Input Listeners
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);

	    Client client  = new Client(this);
	    Thread t = new Thread(client);
		t.start();

		client.serverHandler(this);
	}

	public void serverHandler() {
		JTextField tf = new JTextField(20);
		JButton b1 = new JButton("Send");
		add(tf);
		add(b1);

		b1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
		           System.out.println(tf.getText());
			}
		});
	}

	// Initialize Panel
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	// Update Methods
	public void updateGame() {
	}

	// Render Methods
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public Game getGame() {
		return game;
	}
}
