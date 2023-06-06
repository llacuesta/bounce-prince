package entities;

// Imports
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.IndicatorConstants.*;
import static utils.Constants.GameConstants.*;
import static utils.Constants.LivesConstants.*;
import static utils.HelpMethods.*;
import java.awt.image.BufferedImage;
import main.Game;
import java.awt.Graphics;

import utils.LoadSave;
import gamestates.Playing;

public class Player extends Entity {
	
	// Animation Attributes
	private transient BufferedImage[][] animations;
	private int animSpeed = 17;
	private int playerAction = IDLE;
	private int flipX = 0;
	private int flipW = 1;
	
	// Movement Attributes
	private boolean left, right, jump;
	private boolean moving = false;
	private float playerSpeed = 0.5f * Game.TILE_SCALE;
	
	// Level Data
	private int[][] levelData;
	
	// Gravity Attributes
	private float airSpeed = 0f;
	private boolean inAir = false;
	
	// Lives Attributes
//	private BufferedImage emptyLivesBar;
//	private BufferedImage fullLivesBar;
	
	private int maxHealth = 3;
	private int currentHealth = maxHealth;
	private int livesBarWidth = 0;
	private boolean alive = true;
	private String timeOfDeath;
	private String timeOfWin;
	
	// Others
	private int playerNum;

	// Constructor
	public Player(float x, float y, int width, int height, int playerNum) {
		super(x, y, width, height);
		this.playerNum = playerNum;
		loadAnimations();
//		loadLives();
		initHitbox(x, y, 13 * Game.PLAYER_SCALE, 30 * Game.PLAYER_SCALE);
	}
	
	// Update Methods
	public void update() {
		updateHealthBar();
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	private void updateHealthBar() {
		livesBarWidth = FULL_LIVES_BAR_WIDTH - (int) ((currentHealth / (float) maxHealth) * FULL_LIVES_BAR_WIDTH);
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

	public void changeHealth(int value) {
		currentHealth += value;

		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
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
		if (!inAir) {
			if ((!left && !right) || (right && left)) {
				return;
			}
		}
			
		float xSpeed = 0;
		
		if (left) {
			xSpeed -= playerSpeed;
			flipX = width;
			flipW = -1;
		} 
		if (right) {
			xSpeed += playerSpeed;
			flipX = 0;
			flipW = 1;
		}
		
		if (!inAir) {
			if (!IsEntityOnFloor(hitbox, levelData)) {
				inAir = true;
			}
		}

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosNextToHorizontalWall(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = FALL_SPEED_AFTER_COLLISION;
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
		airSpeed = JUMP_SPEED;
	}
 	
	// Render Methods
	public void render(Graphics g, int levelOffset) {
		g.drawImage(animations[playerAction][animIndex], (int) (hitbox.x - X_DRAW_OFFSET) + flipX, (int) (hitbox.y - Y_DRAW_OFFSET) - levelOffset, width * flipW, height, null);
//		drawUI(g);
		drawPlayerIndicator(g, levelOffset);
		//drawHitbox(g);
	}

	// Misc Methods
//	private void loadLives() {
//		emptyLivesBar = LoadSave.GetSpriteAtlas(LoadSave.EMPTY_HEALTH_BAR);
//		fullLivesBar = LoadSave.GetSpriteAtlas(LoadSave.FULL_HEALTH_BAR);
//	}

	private void loadAnimations() {
		// Change skin according to playerNum
		BufferedImage img;
		if (playerNum == 1) img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_1_ATLAS);
		else if (playerNum == 2) img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_2_ATLAS);
		else if (playerNum == 3) img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_3_ATLAS);
		else img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_4_ATLAS);

		// Player animations
		animations = new BufferedImage[5][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i*50, j*37, 50, 37);
			}
		}
	}

	private void drawPlayerIndicator(Graphics g, int levelOffset) {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_INDICATORS);
		g.drawImage(img.getSubimage((this.playerNum - 1) * INDICATOR_WIDTH_DEFAULT, 0, INDICATOR_WIDTH_DEFAULT, INDICATOR_HEIGHT_DEFAULT), (int) this.hitbox.x - 20, (int) (this.hitbox.y - levelOffset) - 74, INDICATOR_WIDTH, INDICATOR_HEIGHT, null);
	}

//	private void drawUI(Graphics g) {
//		g.drawImage(emptyLivesBar, EMPTY_LIVES_BAR_X, EMPTY_LIVES_BAR_Y, EMPTY_LIVES_BAR_WIDTH, EMPTY_LIVES_BAR_HEIGHT, null);
//		if (currentHealth != 0) {
//			g.drawImage(fullLivesBar.getSubimage(0, 0, fullLivesBar.getWidth() - (16 * (maxHealth - currentHealth)), fullLivesBar.getHeight()), FULL_LIVES_BAR_X, FULL_LIVES_BAR_Y, FULL_LIVES_BAR_WIDTH - livesBarWidth, FULL_LIVES_BAR_HEIGHT, null);
//		}
//	}
	
	public void loadLevelData(int[][] levelData) {
		this.levelData = levelData;
		if (!IsEntityOnFloor(hitbox, levelData)) {
			inAir = true;
		}
	}

	public int getPlayerHealth() {
		return currentHealth;
	}

	// Setters and Getters
	// Booleans
	public void setLeft(boolean left) {
		this.left = left;
	}
	public void setRight(boolean right) {
		this.right = right;
	}
	public void setJump(boolean jump) {
		this.jump = jump;
	}
	public void setAlive(boolean alive) { this.alive = alive; };
	public void setInAir(boolean inAir) { this.inAir = inAir; };
	public void setMoving(boolean moving) { this.moving = moving; }
	public boolean isJump() {
		return jump;
	}
	public boolean isInAir() {
		return inAir;
	}
	public boolean isLeft() {
		return left;
	}
	public boolean isRight() {
		return right;
	}
	public boolean isMoving() {
		return moving;
	}
	public boolean isAlive() { return alive; }

	// Player Position
	public void setX(float x) {
		this.hitbox.x = x;
	}
	public void setY(float y) {
		this.hitbox.y = y;
	}
	public float getX() {
		return this.hitbox.x;
	}
	public float getY() {
		return this.hitbox.y;
	}

	// Animations
	public int getPlayerAction() {
		return playerAction;
	}
	public int getFlipX() {
		return flipX;
	}
	public int getFlipW() {
		return flipW;
	}
	public void setPlayerAction(int playerAction) {
		this.playerAction = playerAction;
	}
	public void setFlipX(int flipX) {
		this.flipX = flipX;
	}
	public void setFlipW(int flipW) {
		this.flipW = flipW;
	}

	// Others
	public void setNum(int num) {
		this.playerNum = num;
	}
	public void setTimeOfDeath(String timeOfDeath) {
		this.timeOfDeath = timeOfDeath;
	}
	public void setTimeOfWin(String timeOfWin) {
		this.timeOfDeath = timeOfWin;
	}
	public int getPlayerNum() {
		return this.playerNum;
	}
	public String getTimeOfDeath() {
		return timeOfDeath;
	}
	public String getTimeOfWin() {
		return timeOfWin;
	}

	public void resetDirBooleans() {
		this.left = false;
		this.right = false;
		this.jump = false;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		moving = false;
		playerAction = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, levelData))
			inAir = true;
	}
}
