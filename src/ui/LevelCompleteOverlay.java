package ui;

import entities.Player;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.State;
import main.Game;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LevelCompleteOverlay {

    // Instance Attributes
    private State playing;
    private BufferedImage winScreen;
    private int winX, winY, winW, winH;
    private Font font;

    // Constructor
    public LevelCompleteOverlay(State playing) {
        this.playing = playing;
        createImg();
    }

    // Render methods
    public void draw(Graphics g, ArrayList<Player> otherPlayers, Player player, boolean gameWin) {
        // Import font
        font = LoadSave.ImportFont(LoadSave.FONT).deriveFont(30f);

        g.setColor(new Color(0, 0, 0, 75));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);

        g.setColor(Color.white);
        g.setFont(font);
        
        ArrayList<Player> allPlayers = new ArrayList<>(otherPlayers);
        allPlayers.add(player);
        
        Player winner = null;
        for (Player p : allPlayers) {
        	if (p.isWin()) winner = p;
        }
        
        g.drawString("P" + winner.getPlayerNum() + " WON", 420, (int) (73 * Game.TILE_SCALE));
        
//        if (player.isWin()) {
//        	
//        	g.drawString("BY REACHING", 395, (int) (80 * Game.TILE_SCALE));
//            g.drawString("THE CROWN FIRST", 360, (int) (87 * Game.TILE_SCALE));
//        } else {
//        	g.drawString("P" + winner.getPlayerNum() + " WON", 420, (int) (73 * Game.TILE_SCALE));
//            g.drawString("BY SURVIVING", 375, (int) (80 * Game.TILE_SCALE));
//            g.drawString("THE LONGEST", 385, (int) (87 * Game.TILE_SCALE));
//        }

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
        winY = 75;
    }
}
