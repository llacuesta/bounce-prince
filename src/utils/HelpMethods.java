package utils;

import main.Game;
import java.util.Arrays;
import java.awt.geom.Rectangle2D;

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {
		if (!IsSolid(x, y, levelData))
			if (!IsSolid(x + width, y + height, levelData))
				if (!IsSolid(x + width, y, levelData))
					if (!IsSolid(x, y + height, levelData))
						return true;
		return false;
	}
	
	// TODO: Bug here where the sprite is too tall to not detect collision 
	// if a single tile goes right in between the top and bottom of the sprite
	private static boolean IsSolid(float x, float y, int[][] levelData) {
		int maxHeight = levelData.length * Game.TILES_SIZE;
		
		if (x < 0 || x >= Game.GAME_WIDTH) {
			return true;
		}
		if (y < Game.GAME_HEIGHT - maxHeight || y >= Game.GAME_HEIGHT + (2 * Game.TILES_SIZE)) {
			return true;
		}
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		int yVal = (int) (yIndex + levelData.length - Game.TILES_IN_HEIGHT);
		if (yVal >= levelData.length) {
			yVal = levelData.length - 1;
		}
		int value = levelData[yVal][(int) xIndex];
		if (value >= 96) value -= 96;
		if (Arrays.asList(8, 9, 10, 11, 12, 14, 15, 16, 18, 19, 20, 22, 24, 27, 30, 33, 34, 35, 36, 43, 51, 52, 67, 71, 77, 78, 79).contains(value)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Going right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else {
			// Going left
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static float GetEntityYPosNextToHorizontalWall(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) Math.round(hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Falling, touching floor
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			if (tileYPos % 2 != 0) {
				return tileYPos + yOffset + hitbox.height + 1;
			} else {
				return tileYPos + yOffset + (hitbox.height / 2) + 1;
			}
		} else {
			// Jumping
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData)) {
				return false;
			}
		}
		return true;
	}
}
