package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.GameOverOverlay;
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

	// Game Over Attributes
	private GameOverOverlay gameOverOverlay;
	private boolean gameOver = false;

	// Constructor
	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	// Init Method
	private void initClasses() {
		levelHandler = new LevelHandler(game);
		player = new Player(200, 500, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), this);
		player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
		gameOverOverlay = new GameOverOverlay(this);
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
		if (!gameOver) {
			levelHandler.update();
			player.update();
			checkCloseToBorder();
		}
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

		if (gameOver) {
			gameOverOverlay.draw(g);
		}
	}

	public void resetAll() {
		gameOver = false;
		player.resetAll();
		yLevelOffset = 0;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkIfWithinVisible() {
		if (player.getHitbox().y + player.getHeight() / 2 > Game.GAME_HEIGHT + yLevelOffset) {
			player.changeHealth(-3);
		}
	}

	// Input methods
	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
		} else {
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
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
