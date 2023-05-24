package inputs;

// Imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import main.GamePanel;

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
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case CREATE:
			gamePanel.getGame().getCreate().keyPressed(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
			break;
		case TUTORIAL:
			gamePanel.getGame().getTutorial().keyPressed(e);
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
		case CREATE:
			gamePanel.getGame().getCreate().keyReleased(e);
			break;
		case JOIN:
			gamePanel.getGame().getJoin().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
			break;
		case TUTORIAL:
			gamePanel.getGame().getTutorial().keyReleased(e);
			break;
		default:
			break;
		}
	}
}
