package entities;

// Imports
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.image.BufferedImage;

import main.Game;

import java.awt.Graphics;
import utils.LoadSave;

public class Countdown extends Entity {

	// Animation Attributes
	private BufferedImage[] animations;
	private int count = 3;
	private boolean startTimer;

	// Constructor
	public Countdown(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
//		initHitbox(x, y, 13 * Game.COUNT_SCALE, 30 * Game.COUNT_SCALE);
		startTimer = false;
	}

	public int getCount() {
		return count;
	}

	// Render Methods
	public void render(Graphics g, Timer timer) {
		if(count > 0){
			if(x < 230) {
				x += 0.5;
				y += 0.5;
				g.drawImage(animations[count-1],(int) x,(int) y, width--, height--, null);
			} else {
				x = 145;
				y = 215;
				width = (int) (20 * Game.COUNT_SCALE);
				height = (int) (37 * Game.COUNT_SCALE);
				count -= 1;
			}
		} else {
			if(!startTimer)
				timer.startTime();
				startTimer = true;
		}
	}

	// Misc Methods
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.COUNTDOWN);

		animations = new BufferedImage[3];
		animations[2] = img.getSubimage(1268, 900, 120, 200);
		animations[1] = img.getSubimage(1048, 900, 120, 200);
		animations[0] = img.getSubimage(823, 900, 120, 200);
	}
}
