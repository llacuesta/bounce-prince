package ui;

// Imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.State;
import main.Game;
import utils.LoadSave;
import java.awt.image.BufferedImage;

public class GameOverOverlay {

	// Instance attributes
	private State playing;
	private BufferedImage deathScreen;
	private int deathX, deathY, deathW, deathH;
	
	// Constructor
	public GameOverOverlay(State playing) {
		this.playing = playing;
		createImg();
	}
	
	// Render methods
	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 75));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

		g.drawImage(deathScreen, deathX, deathY, deathW, deathH, null);
	}
	
	// Input methods
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
			Gamestate.state = Gamestate.MENU;
		}
	}

	// Misc methods
	private void createImg() {
		deathScreen = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		deathW = (int) (deathScreen.getWidth() * 2.5);
		deathH = (int) (deathScreen.getHeight() * 2.5);
		deathX = Game.GAME_WIDTH / 2 - deathW / 2;
		deathY = Game.GAME_HEIGHT - (int) (Game.GAME_HEIGHT / 2.5 - deathH / 2);
	}
}
