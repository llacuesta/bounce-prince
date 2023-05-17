package utils;

import main.Game;

public class Constants {

	// Game Constants
	public static class GameConstants {
		public static final float GRAVITY = 0.04f * Game.TILE_SCALE;
		public static final float JUMP_SPEED = -1.65f * Game.TILE_SCALE;
		public static final float FALL_SPEED_AFTER_COLLISION = 0.5f * Game.PLAYER_SCALE;
	}

	// UI Constants
	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
		}
	}

	// Player Constants
	public static class PlayerConstants {
		// HitBox Constants
		public static final float X_DRAW_OFFSET = 19 * Game.PLAYER_SCALE;
		public static final float Y_DRAW_OFFSET = 6 * Game.PLAYER_SCALE;

		public static final int IDLE = 0;
		public static final int CROUCH = 1;
		public static final int RUN = 2;
		public static final int JUMP = 3;
		public static final int FALL = 4;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
				case 0, 1: return 4;
				case 2: return 6;
				case 3: return 8;
				case 4: return 2;
				default: return 1;
			}
		}
	}

	// Crown Constants
	public static class CrownConstants {
		public static final int IDLE_AMOUNT = 8;

		public static final int CROWN_WIDTH_DEFAULT = 24;
		public static final int CROWN_HEIGHT_DEFAULT = 16;
		public static final int CROWN_WIDTH = CROWN_WIDTH_DEFAULT * 3;
		public static final int CROWN_HEIGHT = CROWN_HEIGHT_DEFAULT * 3;
	}

	// Countdown Constants
	public static class CountdownConstants {
		public static final int COUNTDOWN_WIDTH_DEFAULT = 32;
		public static final int COUNTDOWN_HEIGHT_DEFAULT = 32;
		public static final int COUNTDOWN_WIDTH = (int) (COUNTDOWN_WIDTH_DEFAULT * 1.5);
		public static final int COUNTDOWN_HEIGHT = (int) (COUNTDOWN_HEIGHT_DEFAULT * 1.5);
	}

	// Indicator Constants
	public static class IndicatorConstants {
		public static final int INDICATOR_WIDTH_DEFAULT = 32;
		public static final int INDICATOR_HEIGHT_DEFAULT = 32;
		public static final int INDICATOR_WIDTH = (int) (INDICATOR_WIDTH_DEFAULT * Game.PLAYER_SCALE);
		public static final int INDICATOR_HEIGHT = (int) (INDICATOR_HEIGHT_DEFAULT * Game.PLAYER_SCALE);
	}

	// Lives Constants
	public static class LivesConstants {
		public static final int EMPTY_LIVES_BAR_WIDTH = (int) (24 * Game.TILE_SCALE);
		public static final int EMPTY_LIVES_BAR_HEIGHT = (int) (8 * Game.TILE_SCALE);
		public static final int EMPTY_LIVES_BAR_X = (int) (Game.GAME_WIDTH - (EMPTY_LIVES_BAR_WIDTH + (10 * Game.TILE_SCALE)));
		public static final int EMPTY_LIVES_BAR_Y = (int) (10 * Game.TILE_SCALE);

		public static final int FULL_LIVES_BAR_WIDTH = (int) (24 * Game.TILE_SCALE);
		public static final int FULL_LIVES_BAR_HEIGHT = (int) (8 * Game.TILE_SCALE);
		public static final int FULL_LIVES_BAR_X = (int) (Game.GAME_WIDTH - (FULL_LIVES_BAR_WIDTH + (10 * Game.TILE_SCALE)));
		public static final int FULL_LIVES_BAR_Y = (int) (10 * Game.TILE_SCALE);
	}
}
