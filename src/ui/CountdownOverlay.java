package ui;

// Imports
import java.awt.image.BufferedImage;
import gamestates.Playing;
import main.Game;
import java.awt.Graphics;
import utils.LoadSave;
import static utils.Constants.CountdownConstants.*;
import javax.swing.Timer;

public class CountdownOverlay {

    // Animation Attributes
    private BufferedImage[] animation;
    private BufferedImage countdown;
    private int count = 3;
    private int cdX, cdY, cdW, cdH;

    // Background Attributes
    private BufferedImage background;
    private int bgX, bgY, bgW, bgH;

    // Countdown
    private Timer timer;
    private long countdownTime = -1;
    private final long duration = 3000;

    // Constructor
    public CountdownOverlay() {
        loadAnimations();
        createBackground();
    }

    // Update Method
    public void update() {
        // Start countdown
        timer = new Timer(0, e -> {
            if (countdownTime < 0) {
                countdownTime = System.currentTimeMillis();
            }

            long now = System.currentTimeMillis();
            long clockTime = now - countdownTime;
            if (clockTime >= duration) {
                clockTime = duration;
                count = 0;
                timer.stop();
            }

            if (count != (int) ((duration - clockTime) / 1000)) {
                count = (int) ((duration - clockTime) / 1000) + 1;
            }
        });

        timer.start();
    }

    // Render Method
    public void draw(Graphics g) {
        // Draw countdown
        if (count > 0) {
            // Draw background
            g.drawImage(background, bgX, bgY, bgW, bgH, null);
            g.drawImage(animation[count - 1], cdX, cdY, cdW, cdH, null);
        }
    }

    // Misc Methods
    private void createBackground() {
        background = LoadSave.GetSpriteAtlas(LoadSave.COUNTDOWN_BG);
        bgW = (int) (background.getWidth() * 2.5);
        bgH = (int) (background.getHeight() * 2.5);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = Game.GAME_HEIGHT / 2 - (int) (Game.GAME_HEIGHT / 1.7 - bgH / 2);
    }

    private void loadAnimations() {
        countdown = LoadSave.GetSpriteAtlas(LoadSave.COUNTDOWN);
        cdW = (int) (COUNTDOWN_WIDTH * 2.5);
        cdH = (int) (COUNTDOWN_HEIGHT * 2.5);
        cdX = Game.GAME_WIDTH / 2 - cdW / 2;
        cdY = Game.GAME_HEIGHT / 2 - (int) (Game.GAME_HEIGHT / 2.125 - cdH / 2);

        animation = new BufferedImage[3];
        animation[2] = countdown.getSubimage(0, 0, 32, 32);
        animation[1] = countdown.getSubimage(32, 0, 32, 32);
        animation[0] = countdown.getSubimage(64, 0, 32, 32);
    }

    public int getCount() {
        return count;
    }
}