package utils;

import entities.Crown;
import groovyjarjarantlr4.v4.misc.Graph;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {
	
	public static final String PLAYER_ATLAS = "adventurer_sprites.png";
	public static final String LEVEL_ATLAS = "level_tiles.png";
	public static final String LEVEL_ONE_DATA = "level_1_data.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String EMPTY_HEALTH_BAR = "hearts_empty.png";
	public static final String FULL_HEALTH_BAR = "hearts_full.png";
	public static final String DEATH_SCREEN = "dead_screen.png";
	public static final String CROWN_ATLAS = "crown_sprites.png";
	public static final String WIN_SCREEN = "win_screen.png";
	public static final String COUNTDOWN_BG = "countdown_background.png";
	public static final String COUNTDOWN = "countdown.png";
	public static final String FONT = "bombardier_font.ttf";

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

	public static Font ImportFont(String fileName) {
		Font importFont = null;
		InputStream is = LoadSave.class.getResourceAsStream("/assets/" + fileName);

		try {
			importFont = Font.createFont(Font.TRUETYPE_FONT, is);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(importFont);
		} catch (IOException|FontFormatException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return importFont;
	}

	public static Crown GenerateCrown() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		Crown crown = new Crown(0, 0);;

		for (int j = 0; j < img.getHeight(); j++) {
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 1) {
					crown = new Crown(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
				}
			}
		}
		return crown;
	}
	
	public static int[][] GetLevelData() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] levelData = new int[img.getHeight()][img.getWidth()];

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
