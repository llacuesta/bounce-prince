package ui;

// Imports
import gamestates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import main.Game;
import utils.LoadSave;

import javax.swing.Timer;

public class TimerOverlay {

    // Instance Attributes
    private long previousTime, currentTime;
    private long savedTime;
    private int min, sec;
    private Font font;

    // Constructor
    public TimerOverlay(Playing playing) {
        previousTime = 0;
        currentTime = 0;
        savedTime = 0;
    }

    // Update Method
    public void update() {
        long elapsedTimeInSecond = this.getTime();
        min = (int) (elapsedTimeInSecond /60);
        sec = (int) (elapsedTimeInSecond - (min * 60));
    }

    // Draw Method
    public void draw(Graphics g) {
        // Import Font
        font = LoadSave.ImportFont(LoadSave.FONT).deriveFont(40f);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("TIME", (int) (10 * Game.TILE_SCALE), (int) (18 * Game.TILE_SCALE));

        int padding = 25;
        if (sec < 10)
            g.drawString(min + ":0" + sec, (int) (10 * Game.TILE_SCALE), (int) (21 * Game.TILE_SCALE + padding));
        else
            g.drawString(min + ":" + sec, (int) (10 * Game.TILE_SCALE), (int) (21 * Game.TILE_SCALE + padding));
    }

    // Misc Methods
    public void startTime() {
        previousTime = System.currentTimeMillis();
    }

    public long getTime() {
        currentTime = System.currentTimeMillis();
        return ((currentTime - previousTime)/1000) + savedTime;
    }

    public void saveTime() {
        savedTime = getTime();
    }
}
