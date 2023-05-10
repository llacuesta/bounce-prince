package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import entities.Crown;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.CountdownOverlay;
import ui.GameOverOverlay;
import ui.LevelCompleteOverlay;
import ui.TimerOverlay;
import utils.LoadSave;

public class Playing extends State implements StateMethods {

	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;
	private Crown crown;

	// Level Scrolling Attributes
	private int yLevelOffset = 0;
	private int topBorder = (int) (0.4 * Game.GAME_HEIGHT);
	private int levelTilesTall = LoadSave.GetLevelData().length;
	private int minTilesOffset = Game.TILES_IN_HEIGHT - levelTilesTall;
	private int minLevelOffsetY = minTilesOffset * Game.TILES_SIZE;

	// Overlays Attributes
	private GameOverOverlay gameOverOverlay;
	private LevelCompleteOverlay levelCompleteOverlay;
	private TimerOverlay timerOverlay;
	private CountdownOverlay countdownOverlay;
	private boolean timerStart = false;
	private boolean countdownStart = false;
	private boolean gameOver = false;
	private boolean gameWin = false;

	// Constructor
	public Playing(Game game) {
		super(game);
		initClasses();
	}
	
	// Init Method
	private void initClasses() {
		// Initiating Level and Entities
		levelHandler = new LevelHandler(game);
		player = new Player(200, 500, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), this);
		player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
		crown = LoadSave.GenerateCrown();

		// Initiating Overlays
		gameOverOverlay = new GameOverOverlay(this);
		levelCompleteOverlay = new LevelCompleteOverlay(this);
		timerOverlay = new TimerOverlay(this);
		countdownOverlay = new CountdownOverlay();
	}

	// Update Methods
	@Override
	public void update() {
		if (!gameOver && !gameWin) {
			levelHandler.update();
			player.update();
			crown.update();
			checkCloseToBorder();

			if (!countdownStart) {
				countdownOverlay.update();
				countdownStart = true;
			}

			if (countdownOverlay.getCount() == 0) {
				if (!timerStart) {
					timerOverlay.startTime();
					timerStart = true;
				}
				timerOverlay.update();
			}
		}
	}

	public boolean checkCrownTouched() {
		return player.getHitbox().intersects(crown.getHitbox());
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

	public void checkIfWithinVisible() {
		if (player.getHitbox().y + player.getHeight() / 2 > Game.GAME_HEIGHT + yLevelOffset) {
			player.changeHealth(-3);
		}
	}

	// Render Methods
	@Override
	public void draw(Graphics g) {
		levelHandler.draw(g, yLevelOffset);
		player.render(g, yLevelOffset);
		crown.render(g, yLevelOffset);
		countdownOverlay.draw(g);
		if (countdownOverlay.getCount() == 0) {
			timerOverlay.draw(g);
		}

		if (gameOver) {
			gameOverOverlay.draw(g);
		} else if (gameWin) {
			levelCompleteOverlay.draw(g);
		}
	}
	
	// Misc Methods
	public Player getPlayer() {
		return player;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public void resetAll() {
		// Resetting timers
		timerStart = false;
		countdownStart = false;
		timerOverlay = new TimerOverlay(this);
		countdownOverlay = new CountdownOverlay();

		// Resetting win/lose conditions
		gameOver = false;
		gameWin = false;
		player.resetAll();
		yLevelOffset = 0;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void setGameWin(boolean gameWin) {
		this.gameWin = gameWin;
	}

	// Input methods
	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameOverOverlay.keyPressed(e);
		} else if (gameWin) {
			levelCompleteOverlay.keyPressed(e);
		} else {
			if (countdownOverlay.getCount() == 0) {
				switch(e.getKeyCode()) {
					case KeyEvent.VK_W, KeyEvent.VK_SPACE:
						player.setJump(true);
						break;
					case KeyEvent.VK_A:
						player.setLeft(true);
						break;
					case KeyEvent.VK_D:
						player.setRight(true);
						break;
					case KeyEvent.VK_ESCAPE:
						Gamestate.state = Gamestate.MENU;
						break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameWin) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W, KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
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
