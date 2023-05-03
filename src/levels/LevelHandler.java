package levels;

import main.Game;
import utils.LoadSave;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class LevelHandler {

	// Instance Attributes
	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;

	// Constructor
	public LevelHandler(Game game) {
		this.game = game;
		importLevelTiles();
		levelOne = new Level(LoadSave.GetLevelData());

	}

	private void importLevelTiles() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[96];
		for (int j = 0; j < 12; j++) {
			for (int i = 0; i < 8; i++) {
				int index = j * 8 + i;
				levelSprite[index] = img.getSubimage(i*8, j*8, 8, 8);
			}
		}

	}

	public Level getCurrentLevel() {
		return levelOne;
	}

	// Update methods
	public void update() {

	}

	// Render methods
	public void draw(Graphics g) {
		for (int j = 0; j < Game.TILES_IN_HEIGHT; j++) {
			for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
				int index = levelOne.getSpriteIndex(i, j);
				if (index >= 96) {
					g.drawImage(levelSprite[index - 96], i*Game.TILES_SIZE + Game.TILES_SIZE, j*Game.TILES_SIZE, -Game.TILES_SIZE, Game.TILES_SIZE, null);
				} else {
					g.drawImage(levelSprite[index], i*Game.TILES_SIZE, j*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
				}
			}
		}
	}
}
