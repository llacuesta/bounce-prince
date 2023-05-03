package utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;
import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {

	public static final String PLAYER_ATLAS = "adventurer_sprites.png";
	public static final String LEVEL_ATLAS = "level_tiles.png";
	public static final String LEVEL_ONE_DATA = "level_1_data.png";
	public static final String COUNTDOWN = "blocktext.png";

	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/assets/" + fileName);

		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return img;
	}

	public static int[][] GetLevelData() {
		int[][] levelData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);

		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				levelData[j][i] = value;
			}
		}
		return levelData;
	}
}
