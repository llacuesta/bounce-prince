package entities;

// Imports
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.image.BufferedImage;

import main.Game;

import java.awt.Graphics;
import utils.LoadSave;

public class Player extends Entity {
	
	// Animation Attributes
	private BufferedImage[][] animations;
	private int animTick, animIndex, animSpeed = 20;
	private int playerAction = IDLE;
	
	// Movement Attributes
	private boolean left, up, right, down, jump;
	private boolean moving = false;
	private float playerSpeed = 2.0f;
	
	// Level Data
	private int[][] levelData;
	
	// Hitbox Attributes
	private float xDrawOffset = 19 * Game.PLAYER_SCALE;
	private float yDrawOffset = 6 * Game.PLAYER_SCALE;
	
	// Gravity Attributes
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.TILE_SCALE;
	private float jumpSpeed = -1.25f * Game.TILE_SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.PLAYER_SCALE;
	private boolean inAir = false;

	// Constructor
	public Player(float x, float y, int width, int height) {
		super(x, y, width, height);
		loadAnimations();
		initHitbox(x, y, 13 * Game.PLAYER_SCALE, 30 * Game.PLAYER_SCALE);
	}
	
	// Update Methods
	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	private void updateAnimationTick() {
		animTick++;
		if (animTick >= animSpeed) {
			animTick = 0;
			animIndex++;
			if (animIndex >= GetSpriteAmount(playerAction)) {
				animIndex = 0;
			}
		}
	}
	
	private void setAnimation() {
		int startAnim = playerAction;
		
		if (moving) {
			playerAction = RUN;
		} else {
			playerAction = IDLE;
		}
		
		if (inAir) {
			if (airSpeed < 0) {
				playerAction = JUMP;
			} else {
				playerAction = FALL;
			}
		}
		
		if (startAnim != playerAction) {
			resetAnimTick();
		}
	}
	
	private void resetAnimTick() {
		animTick = 0;
		animIndex = 0;
	}
	
	private void updatePos() {
		moving = false;
		if (jump) {
			jump();
		}
		if (!left && !right && !inAir) {
			return;
		}
		float xSpeed = 0;
		
		if (left) {
			xSpeed -= playerSpeed;
		} 
		if (right) {
			xSpeed += playerSpeed;
		}
		
		if (!inAir) {
			if (!IsEntityOnFloor(hitbox, levelData)) {
				inAir = true;
			}
		}

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosNextToHorizontalWall(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}
	
	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		} 
	}
	
	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}
	
	private void jump() {
		if (inAir) {
			return;
		}
		
		inAir = true;
		airSpeed = jumpSpeed;
	}
 	
	// Render Methods
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][animIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null); 
		drawHitbox(g);
	}

	// Misc Methods
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
			
		animations = new BufferedImage[5][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i*50, j*37, 50, 37);
			}
		}
	}
	
	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if (!IsEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	// Setters and Getters
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetDirBooleans() {
		this.left = false;
		this.up = false;
		this.right = false;
		this.down = false;
	}
}
