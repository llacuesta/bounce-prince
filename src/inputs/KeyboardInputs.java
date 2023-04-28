package inputs;

// Imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;
import static utils.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {
	// Instance Attributes
	private GamePanel gamePanel;
	
	// Constructor
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	// Override methods
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		default:
			break;
		}
	}
}
