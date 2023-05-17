package levels;

import entities.Crown;
import main.Game;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Level {

	// Instance Attributes
	private BufferedImage levelImg;
	private int[][] levelData;

	// Level Scrolling Attributes
	private int levelTilesTall;
	private int minTilesOffset;
	private int minLevelOffsetY;

	// Entities
	private Crown crown;

	// Constructor
	public Level(BufferedImage levelImage) {
		this.levelImg = levelImage;
		this.levelData = new int[this.levelImg.getHeight()][this.levelImg.getWidth()];
		loadLevel();
		calculateOffset();
	}

	// Misc Methods
	private void loadLevel() {
		for (int y = 0; y < levelImg.getHeight(); y++) {
			for (int x = 0; x < levelImg.getWidth(); x++) {
				Color c = new Color(levelImg.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();

				loadLevelData(red, x, y);
				loadCrown(green, x, y);
			}
		}
	}

	private void loadLevelData(int red, int x, int y) {
		levelData[y][x] = red;
	}

	private void loadCrown(int green, int x, int y) {
		if (green == 1) {
			crown = new Crown(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
		}
	}

	private void calculateOffset() {
		levelTilesTall = levelImg.getHeight();
		minTilesOffset = Game.TILES_IN_HEIGHT - levelTilesTall;
		minLevelOffsetY = minTilesOffset * Game.TILES_SIZE;
	}

	// Getter Methods
	public int getSpriteIndex(int x, int y) {
		return levelData[y][x];
	}
	
	public int[][] getLevelData() {
		return levelData;
	}

	public BufferedImage getLevelImg() {
		return levelImg;
	}

	public int getMinLevelOffset() {
		return minLevelOffsetY;
	}

	public Crown getCrown() {
		return crown;
	}
}
