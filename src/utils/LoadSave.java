package utils;

import entities.Crown;
import main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {
	
	public static final String PLAYER_1_ATLAS = "player_1_sprites.png";
	public static final String PLAYER_2_ATLAS = "player_2_sprites.png";
	public static final String PLAYER_3_ATLAS = "player_3_sprites.png";
	public static final String PLAYER_4_ATLAS = "player_4_sprites.png";
	public static final String LEVEL_ATLAS = "level_tiles.png";
	public static final String LEVEL_ONE_DATA = "level_1_data2.png";
	public static final String LEVEL_ZERO_DATA = "tutorial_data.png";
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
	public static final String PLAYER_INDICATORS = "player_indicators.png";
	public static final String TITLE = "title_logo.png";

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
}
