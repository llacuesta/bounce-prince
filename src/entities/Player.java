package entities;

// Imports
import static utils.Constants.PlayerConstants.GetSpriteAmount;
import static utils.Constants.PlayerConstants.*;
import static utils.HelpMethods.*;
import java.awt.image.BufferedImage;
import main.Game;
import java.awt.Graphics;
import utils.LoadSave;
import gamestates.Playing;

public class Player extends Entity {
	
	// Animation Attributes
	private BufferedImage[][] animations;
	private int animTick, animIndex, animSpeed = 17;
	private int playerAction = IDLE;
	private int flipX = 0;
	private int flipW = 1;
	
	// Movement Attributes
	private boolean left, up, right, down, jump;
	private boolean moving = false;
	private float playerSpeed = 0.5f * Game.TILE_SCALE;
	
	// Level Data
	private int[][] levelData;
	
	// Hitbox Attributes
	private float xDrawOffset = 19 * Game.PLAYER_SCALE;
	private float yDrawOffset = 6 * Game.PLAYER_SCALE;
	
	// Gravity Attributes
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.TILE_SCALE;
	private float jumpSpeed = -1.65f * Game.TILE_SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.PLAYER_SCALE;
	private boolean inAir = false;
	
	// Lives Attributes
	private BufferedImage emptyLivesBar;
	private BufferedImage fullLivesBar;

	private int emptyLivesBarWidth = (int) (24 * Game.TILE_SCALE);
	private int emptyLivesHeight = (int) (8 * Game.TILE_SCALE);
	private int emptyLivesBarX = (int) (Game.GAME_WIDTH - (emptyLivesBarWidth + (10 * Game.TILE_SCALE)));
	private int emptyLivesBarY = (int) (10 * Game.TILE_SCALE);

	private int fullLivesBarWidth = (int) (24 * Game.TILE_SCALE);
	private int fullLivesBarHeight = (int) (8 * Game.TILE_SCALE);
	private int fullLivesBarX = (int) (Game.GAME_WIDTH - (fullLivesBarWidth + (10 * Game.TILE_SCALE)));
	private int fullLivesBarY = (int) (10 * Game.TILE_SCALE);
	
	private int maxHealth = 3;
	private int currentHealth = maxHealth;
	private int livesBarWidth = 0;
	
	// Others
	private Playing playing;

	// Constructor
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		loadLives();
		// TODO: Change width and height to int
		initHitbox(x, y, 13 * Game.PLAYER_SCALE, 30 * Game.PLAYER_SCALE);
	}
	
	// Update Methods
	public void update() {
		if (playing.checkCrownTouched()) {
			playing.setGameWin(true);
			return;
		}
		updateHealthBar();
		if (currentHealth <= 0) {
			playing.setGameOver(true);
			return;
		}
		updatePos();
		updateAnimationTick();
		setAnimation();
	}
	
	private void updateHealthBar() {
		playing.checkIfWithinVisible();
		livesBarWidth = fullLivesBarWidth - (int) ((currentHealth / (float) maxHealth) * fullLivesBarWidth);
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
	public void render(Graphics g, int levelOffset) {
		g.drawImage(animations[playerAction][animIndex], (int) (hitbox.x - xDrawOffset) + flipX, (int) (hitbox.y - yDrawOffset) - levelOffset, width * flipW, height, null); 
		drawUI(g);
		// drawHitbox(g);
	}

	// Misc Methods
	private void loadLives() {
		emptyLivesBar = LoadSave.GetSpriteAtlas(LoadSave.EMPTY_HEALTH_BAR);
		fullLivesBar = LoadSave.GetSpriteAtlas(LoadSave.FULL_HEALTH_BAR);
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		// Player animations
		animations = new BufferedImage[5][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i*50, j*37, 50, 37);
			}
		}
	}
	
	private void drawUI(Graphics g) {
		g.drawImage(emptyLivesBar, emptyLivesBarX, emptyLivesBarY, emptyLivesBarWidth, emptyLivesHeight, null);
		if (currentHealth != 0) {
			g.drawImage(fullLivesBar.getSubimage(0, 0, fullLivesBar.getWidth() - (16 * (maxHealth - currentHealth)), fullLivesBar.getHeight()), fullLivesBarX, fullLivesBarY, fullLivesBarWidth - livesBarWidth, fullLivesBarHeight, null);
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
