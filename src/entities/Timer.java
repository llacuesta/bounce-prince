package entities;

// Imports
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.image.BufferedImage;

import main.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import utils.LoadSave;
import java.awt.Color;

public class Timer extends Entity {

	// Animation Attributes
	private long previousTime, currentTime;
	private int minLength;
	private long savedTime;

	// Constructor
	public Timer(float x, float y, int width, int height) {
		super(x, y, width, height);
		previousTime = 0;
		currentTime = 0;
		minLength = 10;
		savedTime = 0;
	}

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

	public void draw(Graphics g) {
		long elapsedTimeInSecond = this.getTime();
		int min = (int) (elapsedTimeInSecond /60);
		int sec = (int) (elapsedTimeInSecond - (min * 60));

		if(min > minLength) {
			x += 10;
			minLength = minLength * 10;
		}

		g.setColor(Color.white);
		g.setFont(new Font("Monospaced", Font.BOLD, 30));
		g.drawString("TIME", (int) x, (int) y);

		int padding = 25;
		if (sec < 10)
			g.drawString(min + ":0" + sec, (int) x, (int) y+padding);
		else
			g.drawString(min + ":" + sec, (int) x, (int) y+padding);
	}
}
