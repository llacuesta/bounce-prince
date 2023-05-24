package main;

import gamestates.*;

import java.awt.Graphics;

public class Game implements Runnable {
	
	// Instance Attributes
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 120;
	
	public final static int TILES_DEFAULT_SIZE = 8;
	public final static float TILE_SCALE = 4.0f;
	public final static float PLAYER_SCALE = 2.0f;
	public final static int TILES_IN_WIDTH = 30;
	public final static int TILES_IN_HEIGHT = 25;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * TILE_SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
	
	// Gamestates
	private Playing playing;
	private Menu menu;
	private Tutorial tutorial;
	private Create create;
	private Join join;
	
	// Constructor
	public Game() {
		// Initialize Classes and Entities
		initClasses();
				
		// Initialize Game Window and Panel
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();		
		
		// Start Game Loop
		startGameLoop();
	}

	// Initialize method
	private void initClasses() {
		menu = new Menu(this);
		playing = new Playing(this);
		tutorial = new Tutorial(this);
		create = new Create(this);
		join = new Join(this);
	}
	
	// Game Loop
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	@Override
	public void run() {
		long previousTime = System.nanoTime();
		long lastCheck = System.currentTimeMillis();
		
		// Update
		double timePerUpdate = 1000000000.0 / UPS_SET;
		int updates = 0;
		double uDelta = 0;
		
		// Render
		double timePerFrame = 1000000000.0 / FPS_SET;
		int frames = 0;
		double fDelta = 0;
		
		
		while (true) {
			long currentTime = System.nanoTime();
			uDelta += (currentTime - previousTime) / timePerUpdate;
			fDelta += (currentTime - previousTime) / timePerFrame;

			previousTime = currentTime;
			
			if (uDelta >= 1) {
				update();
				updates++;
				uDelta--;
			}
			if (fDelta >= 1) {
				gamePanel.repaint();
				frames++;
				fDelta--;
			}
			
			// Frame counter
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void update() {
		switch(Gamestate.state) {
		case MENU:
			menu.update();
			break;
		case CREATE:
			create.update();
			break;
		case JOIN:
			join.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case TUTORIAL:
			tutorial.update();
			break;
		case CREDITS, QUIT:
		default:
			System.exit(0);
			break;
		}
	}
	
	public void render(Graphics g) {
		switch(Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case CREATE:
			create.draw(g);
			break;
		case JOIN:
			join.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case TUTORIAL:
			tutorial.draw(g);
			break;
		default:
			break;
		}
	}

	public void windowFocusLost() {
		if (Gamestate.state == Gamestate.PLAYING) {
			playing.getPlayer().resetDirBooleans();
		}
	} 
	
	public Menu getMenu() {
		return menu;
	}

	public Create getCreate() { return create; }

	public Join getJoin() { return join; }

	public Playing getPlaying() {
		return playing;
	}

	public Tutorial getTutorial() { return tutorial; }

	public GamePanel getGamePanel() {
		return gamePanel;
	}
}
