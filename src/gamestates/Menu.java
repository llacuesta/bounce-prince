package gamestates;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;

import main.Game;

public class Menu extends State implements StateMethods {

	// Constructor
	public Menu(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.drawString("MENU", Game.GAME_WIDTH/2, 200);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_ENTER:
			Gamestate.state = Gamestate.PLAYING;
			break;
		case KeyEvent.VK_I:
			Gamestate.state = Gamestate.TUTORIAL;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
