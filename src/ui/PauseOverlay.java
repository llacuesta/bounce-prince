package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import main.Game;
import utils.LoadSave;
import static utils.Constants.UI.URMButtons.*;

public class PauseOverlay {

    private BufferedImage background;
    private int bgX, bgY, bgW, bgH;
    private UrmButton menuB, replayB, unpauseB;
    private Playing playing;
    
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = 145;
        int replayX = 215;
        int unpauseX = 285;
        int bY = 345;

        menuB = new UrmButton(menuX, bY, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 0);
    }

    private void loadBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = background.getWidth();
        bgH = background.getHeight();
        bgX = (Game.GAME_WIDTH / 2) - (bgW / 2);
        bgY = 300;
    }

    public void update() {

        //Urm Buttons
        menuB.update();
        replayB.update();
        unpauseB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgW, bgH, null);

        //Urm Buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if(isIn(e, menuB))
            menuB.setMouseOver(true);
        else if(isIn(e, replayB))
            replayB.setMouseOver(true);
        else if(isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, menuB))
            menuB.setMousePressed(true);
        else if(isIn(e, replayB))
            replayB.setMousePressed(true);
        else if(isIn(e, unpauseB))
            replayB.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menuB)) {
            if(menuB.isMousePressed())
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
        } else if(isIn(e, replayB)) {
            if(replayB.isMousePressed())
                System.out.println("Replay level");
        } else if(isIn(e, unpauseB)) {
            if(unpauseB.isMousePressed())
                playing.unpauseGame();
        }
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
