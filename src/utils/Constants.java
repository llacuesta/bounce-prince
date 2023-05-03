package utils;

public class Constants {

	// Player Constants
	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int CROUCH = 1;
		public static final int RUN = 2;
		public static final int JUMP = 3;
		public static final int FALL = 4;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
				case 0: return 4;
				case 1: return 4;
				case 2: return 6;
				case 3: return 8;
				case 4: return 2;
				default: return 1;
			}
		}
	}
	
	// Directions
	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
}
