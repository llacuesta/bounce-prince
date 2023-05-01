package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import entities.Player;
import levels.LevelHandler;
import main.Game;

public class Playing extends State implements StateMethods {

	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;
	
	// Level Scrolling Attributes
	private int yLevelOffset;

	// Constructor
	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	// Init Method
	private void initClasses() {
		levelHandler = new LevelHandler(game);
		player = new Player(200, 100, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE));
		player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
	}
	
	// Misc Methods
	public Player getPlayer() {
		return player;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	@Override
	public void update() {
		levelHandler.update();
		player.update();
	}

	@Override
	public void draw(Graphics g) {
		levelHandler.draw(g);
		player.render(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setJump(true);
			break;
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
			break;
		case KeyEvent.VK_ESCAPE:
			Gamestate.state = Gamestate.MENU;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W:
			player.setJump(false);
			break;
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
			break;
		}
	} 
}
