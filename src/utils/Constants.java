package utils;

import main.Game;

public class Constants {

	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
		}
	}

	// Player Constants
	public static class PlayerConstants {
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
		public static final int CROWN_WIDTH = (int) (CROWN_WIDTH_DEFAULT * 3);
		public static final int CROWN_HEIGHT = (int) (CROWN_HEIGHT_DEFAULT * 3);
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
}
