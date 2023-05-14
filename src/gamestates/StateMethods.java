package gamestates;

// Imports
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface StateMethods {

	// Methods
	public void update();
	public void draw(Graphics g);
	
	// Key Methods
	public void keyPressed(KeyEvent e);
	public void keyReleased(KeyEvent e);

	// Mouse Methods
	public void mouseClicked(MouseEvent e);
	public void mousePressed(MouseEvent e);
	public void mouseReleased(MouseEvent e);
	public void mouseMoved(MouseEvent e);
}
