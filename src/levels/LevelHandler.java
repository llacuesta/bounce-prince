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
	private int maxHeight;

	// Constructor
	public LevelHandler(Game game) {
		this.game = game;
		importLevelTiles();
		levelOne = new Level(LoadSave.GetLevelData());
		maxHeight = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ONE_DATA).getHeight();
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
	public void draw(Graphics g, int levelOffset) {
		for (int j = 0; j < maxHeight; j++) {
			for (int i = 0; i < Game.TILES_IN_WIDTH; i++) {
				int index = levelOne.getSpriteIndex(i, j);
				if (index >= 96) {
					g.drawImage(levelSprite[index - 96], i*Game.TILES_SIZE + Game.TILES_SIZE, (j - (maxHeight - Game.TILES_IN_HEIGHT))*Game.TILES_SIZE - levelOffset, -Game.TILES_SIZE, Game.TILES_SIZE, null);
				} else {
					g.drawImage(levelSprite[index], i*Game.TILES_SIZE, (j - (maxHeight - Game.TILES_IN_HEIGHT))*Game.TILES_SIZE - levelOffset, Game.TILES_SIZE, Game.TILES_SIZE, null);
				}
			}
		}	
	}
}
