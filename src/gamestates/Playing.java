package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import entities.Countdown;
import entities.Player;
import entities.Timer;
import levels.LevelHandler;
import main.Game;

public class Playing extends State implements StateMethods {

	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;
	private Countdown countdown;
	private Timer timer;
	private boolean exitted;

	// Constructor
	public Playing(Game game) {
		super(game);
		initClasses();
	}

	// Init Method
	private void initClasses() {
		levelHandler = new LevelHandler(game);
		player = new Player(200, 200, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE));
		player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
		countdown = new Countdown(145, 215, (int) (20 * Game.COUNT_SCALE), (int) (37 * Game.COUNT_SCALE));
		timer = new Timer(20,50,0,0);
		exitted = false;
	}

	// Misc Methods
	public Player getPlayer() {
		return player;
	}

	public Timer getTimer() {
		return timer;
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
		if(exitted) {
			timer.startTime();
			exitted = false;
		}

		levelHandler.draw(g);
		player.render(g);
		countdown.render(g, timer);

		if(countdown.getCount() <= 0)
			timer.draw(g);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(countdown.getCount() <= 0) {
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
				timer.saveTime();
				exitted = true;
				break;
			default:
				break;
			}
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
