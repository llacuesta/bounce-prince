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

public class Playing extends State implements StateMethods {

	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;
	private Crown crown;

	// Level Scrolling Attributes
	private int yLevelOffset = 0;
	private int topBorder = (int) (0.4 * Game.GAME_HEIGHT);
	private int minLevelOffsetY;

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
		calculateOffset();
	}

	// Init Method
	private void initClasses() {
		// Initiating Level and Entities
		levelHandler = new LevelHandler(game, 1);
		player = new Player(200, 500, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), 4);
		player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
		crown = levelHandler.getCurrentLevel().getCrown();

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
			checkIfWithinVisible();

			// Start Countdown
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

			// Win and lose condition
			if (checkCrownTouched()) {
				setGameWin(true);
			}
			if (getPlayer().getPlayerHealth() <= 0) {
				setGameOver(true);
			}
		}
	}

	private boolean checkCrownTouched() {
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

	private void checkIfWithinVisible() {
		if (player.getHitbox().y + (player.getHeight() / 2) > Game.GAME_HEIGHT + yLevelOffset) {
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

	private void calculateOffset() {
		minLevelOffsetY = levelHandler.getCurrentLevel().getMinLevelOffset();
	}

	private void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	private void setGameWin(boolean gameWin) {
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
					case KeyEvent.VK_W:
					case KeyEvent.VK_SPACE:
						player.setJump(true);
						break;
					case KeyEvent.VK_A:
						player.setLeft(true);
						break;
					case KeyEvent.VK_D:
						player.setRight(true);
						break;
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameWin) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_W:
			case KeyEvent.VK_SPACE:
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
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
