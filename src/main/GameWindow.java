package main;

// Imports
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

// Imports
import javax.swing.JFrame;

public class GameWindow extends JFrame {
	
	// Constructor
	public GameWindow(GamePanel gamePanel) {
		// Window Setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(gamePanel);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
