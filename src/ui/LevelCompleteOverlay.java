package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class LevelCompleteOverlay {

    // Instance Attributes
    private Playing playing;
    private BufferedImage winScreen;
    private int winX, winY, winW, winH;

    // Constructor
    public LevelCompleteOverlay(Playing playing) {
        this.playing = playing;
        createImg();
    }

    // Render methods
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 75));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.drawImage(winScreen, winX, winY, winW, winH, null);
    }

    // Input methods
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }

    private void createImg() {
        winScreen = LoadSave.GetSpriteAtlas(LoadSave.WIN_SCREEN);
        winW = (int) (winScreen.getWidth() * 2.5);
        winH = (int) (winScreen.getHeight() * 2.5);
        winX = Game.GAME_WIDTH / 2 - winW / 2;
        winY = Game.GAME_HEIGHT / 2 - winH / 2;
    }
}
