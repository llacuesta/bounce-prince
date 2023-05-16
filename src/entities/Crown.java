package entities;

// Imports
import static utils.Constants.CrownConstants.*;
import main.Game;
import utils.LoadSave;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Crown extends Entity {

    // Animation Attributes
    private BufferedImage[] animation;
    private int animSpeed = 20;

    // Others
    private int maxHeight = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ONE_DATA).getHeight();

    // Constructor
    public Crown(float x, float y) {
        super(x, y, CROWN_WIDTH, CROWN_HEIGHT);
        loadAnimation();
        initHitbox(x, y - (maxHeight - Game.TILES_IN_HEIGHT) * Game.TILES_SIZE, CROWN_WIDTH, CROWN_HEIGHT);
    }

    // Update Methods
    public void update() {
        updateAnimationTick();
    }

    private void updateAnimationTick() {
        animTick++;
        if (animTick >= animSpeed) {
            animTick = 0;
            animIndex++;
            if (animIndex >= IDLE_AMOUNT) {
                animIndex = 0;
            }
        }
    }

    // Render Methods
    public void render(Graphics g, int offset) {
        g.drawImage(animation[animIndex], (int) (hitbox.x - 10), (int) (hitbox.y - offset), CROWN_WIDTH, CROWN_HEIGHT, null);
        drawHitbox(g);
    }

    // Misc Methods
    private void loadAnimation() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.CROWN_ATLAS);
        animation = new BufferedImage[8];

        for (int i = 0; i < animation.length; i++) {
            animation[i] = img.getSubimage(i * CROWN_WIDTH_DEFAULT, 0, CROWN_WIDTH_DEFAULT, CROWN_HEIGHT_DEFAULT);
        }
    }
}
