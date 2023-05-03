package gamestates;

// Imports
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public interface StateMethods {

	// Methods
	public void update();
	public void draw(Graphics g);
	
	// Key Methods
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);
}
