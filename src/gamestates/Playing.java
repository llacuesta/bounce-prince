package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import entities.Player;
import levels.LevelHandler;
import main.Game;
import utils.LoadSave;

public class Playing extends State implements StateMethods {

	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;
	
	// Level Scrolling Attributes
	private int yLevelOffset;
	private int topBorder = (int) (0.4 * Game.GAME_HEIGHT);
	private int levelTilesTall = LoadSave.GetLevelData().length;
	private int minTilesOffset = Game.TILES_IN_HEIGHT - levelTilesTall;
	private int minLevelOffsetY = minTilesOffset * Game.TILES_SIZE;
	
	// Constructor
	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	// Init Method
	private void initClasses() {
		levelHandler = new LevelHandler(game);
		player = new Player(200, 500, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE));
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
		checkCloseToBorder();
	}

	private void checkCloseToBorder() {
		int playerY = (int) player.getHitbox().y;
		int diff = playerY - yLevelOffset;
		
		if (diff < topBorder) {
			yLevelOffset += diff - topBorder;
		}
		
		if (yLevelOffset > 0) {
			yLevelOffset = 0;
		} else if (yLevelOffset < minLevelOffsetY) {
			yLevelOffset = minLevelOffsetY;
		}
	}

	@Override
	public void draw(Graphics g) {
		levelHandler.draw(g, yLevelOffset);
		player.render(g, yLevelOffset);
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
