package main;

// Imports
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import ui.ChatOverlay;
import ui.JoinOverlay;

import javax.swing.*;
import java.awt.*;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {	

	private MouseInputs mouseInputs;
	private Game game;
	private ChatOverlay chatInterface;
	private JoinOverlay joinInterface;
	
	// Constructor
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		setPanelSize();
		
		// Input Listeners
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);

		// Chat Interface
		chatInterface = new ChatOverlay();
		add(chatInterface, BorderLayout.EAST);
		// Join Interface
		joinInterface = new JoinOverlay();
		add(joinInterface, BorderLayout.SOUTH);
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

	public ChatOverlay getChatInterface() {
		return chatInterface;
	}

	public JoinOverlay getJoinInterface() {
		return joinInterface;
	}
}
