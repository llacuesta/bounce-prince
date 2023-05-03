package gamestates;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import entities.Countdown;
import entities.Player;
import entities.Timer;
import levels.LevelHandler;
import main.Game;

public class Tutorial extends State implements StateMethods {
	// Instance Entities
	private Player player;
	private LevelHandler levelHandler;

	// Constructor
	public Tutorial(Game game) {
		super(game);
		initClasses();
	}

	@Override
	public void draw(Graphics g) {
		int y = 100;
		int x =50;

		levelHandler.draw(g);
		player.render(g);

		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.BOLD, 20));
		g.drawString("HOW TO PLAY", x, y);
		g.drawString("Press 'A' to MOVE LEFT.", x, y += 40);
		g.drawString("Press 'D' to MOVE RIGHT.", x, y += 30);
		g.drawString("Press 'W' or SpaceBar to JUMP.", x, y += 30);
		g.drawString("Press Esc to RETURN to MENU.", x, y += 30);

		g.drawString("GOAL", x, y += 80);
		g.drawString("BE THE LAST PLAYER LEFT or", x, y += 30);
		g.drawString("BE THE FIRST TO REACH THE TOP.", x, y += 30);

		g.drawString("\"MAY THE ODDS BE EVER IN YOUR FAVOR\"", 30, 750);
	}

	// Init Method
	private void initClasses() {
		levelHandler = new LevelHandler(game);
		player = new Player(50, 530, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE));
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
			default:
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
		default:
			break;
		}
	}
}
