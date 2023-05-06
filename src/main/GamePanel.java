package main;

// Imports
import inputs.KeyboardInputs;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel {	
	private Game game;
	
	// Constructor
	public GamePanel(Game game) {
		this.game = game;
		
		setPanelSize();
		
		// Input Listeners
		addKeyListener(new KeyboardInputs(this));
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
