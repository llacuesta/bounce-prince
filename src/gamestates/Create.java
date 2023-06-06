package gamestates;

import chat.Client;
import chat.GamePlayer;
import chat.GameServer;
import chat.Server;
import entities.Crown;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

public class Create extends State implements StateMethods {

    // Instance Entities
    private LevelHandler levelHandler;
    private Crown crown;

    // Level Scrolling Attributes
    private int yLevelOffset = 0;
    private int offsetCopy = 0;
    private int topBorder = (int) (0.4 * Game.GAME_HEIGHT);
    private int minLevelOffsetY;

    // Overlays Attributes
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;
    private TimerOverlay timerOverlay;
    private CountdownOverlay countdownOverlay;
    private boolean timerStart = false;
    private boolean countdownStart = false;
    private boolean gameOver = false;
    private boolean gameWin = false;

    // Server-Client Entities
    private Server server;
    private Client client;
    private LobbyOverlay lobbyOverlay;
    private boolean serverRunning = false;
    private boolean clientConnected = false;
    private ChatOverlay chatInterface;

    // Multiplayer Entities
    private GameServer gameServer;
    private GamePlayer gamePlayer;
    private boolean gameServerRunning = false;
    private boolean gameClientRunning = false;

    // Playing Flags
    private boolean isPlaying = false;
    private boolean isLobby = true;
    private boolean gameDone = false;

    // Constructor
    public Create(Game game) {
        super(game);
        initClasses();
    }

    // Init Method
    private void initClasses() {
        // Initiating Level and Entities
        levelHandler = new LevelHandler(game, 0);
        player = new Player(200, 500, (int) (50 * Game.PLAYER_SCALE), (int) (37 * Game.PLAYER_SCALE), 1);
        player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());

        otherPlayers = new ArrayList<>();
        
        // Initiating Client and Server
        server = new Server();
        client = new Client();
        lobbyOverlay = new LobbyOverlay(server.getIP(), server.getPort());
    }

    // Update Method
    @Override
    public void update() {
        // Getting interfaces
        if (!Objects.isNull(game.getGamePanel())) {
            chatInterface = game.getGamePanel().getChatInterface();
            chatInterface.setChatClient(client);
        }

        // Starting chat server
        if (!serverRunning) {
            server.startServer();
            serverRunning = true;
        }
        // Starting game server
        if (!gameServerRunning) {
            // Initiating Client and Server UDP
            gameServer = new GameServer();

            gameServer.startGameServer();
            gameServerRunning = true;
        }

        // Creating chat client
        if (!clientConnected && server.getStarted()) {
            client.setClientValues(server.getIP(), Integer.parseInt(server.getPort()));
            client.startClient();
            clientConnected = true;
        }
        // Creating gamePlayer client
        if (!gameClientRunning) {
        	try {
	        	gamePlayer = new GamePlayer(player, InetAddress.getByName(server.getIP()), Integer.parseInt(server.getPort()), 1);
	            gamePlayer.setState(this);
	            gamePlayer.startGamePlayer();
	            gameClientRunning = true;
        	} catch (Exception e) {
        		System.out.println("Client creation failed: " + e);
        	}
        }

        // Change updates according to room
        if (isLobby && !isPlaying) {
            // Updating level
            levelHandler.update();

            // Updating Players
            for (Player player : otherPlayers) {
                player.update();
            }
            player.update();

            // Starting up game
            if (!isPlaying && otherPlayers.size() == 3) {
                // Initialize new level, new player position, crown
                levelHandler = new LevelHandler(game, 1);
                player.setX(200);
                player.setY(500);
                player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
                crown = levelHandler.getCurrentLevel().getCrown();
                calculateOffset();

                // Setting up overlays
                gameOverOverlay = new GameOverOverlay(this);
                levelCompleteOverlay = new LevelCompleteOverlay(this);
                timerOverlay = new TimerOverlay();
                countdownOverlay = new CountdownOverlay();

                // Change from lobby to playing
                isLobby = false;
                isPlaying = true;
            }
        } else if (!isLobby && isPlaying) {
            if (!gameDone) {
                levelHandler.update();

                // Updating players
                for (Player player : otherPlayers) {
                    player.update();
                }
                player.update();
                crown.update();
                checkCloseToBorder();
                checkIfWithinVisible();

                // Start Countdown
                if (!countdownStart) {
                    countdownOverlay.update();
                    countdownStart = true;
                }
                if (countdownOverlay.getCount() == 0) {
                    if (!timerStart) {
                        timerOverlay.startTime();
                        timerStart = true;
                    }
                    timerOverlay.update();
                }

                // Win and lose condition
                if (checkCrownTouched() && !gameWin) {
                    setGameWin(true);
                    this.gameDone = true;

                    if (!player.isWin()) {
                        player.setWin(true);
                        player.setTimeOfWin(timerOverlay.getSavedTime());

                        System.out.println("Time of win: " + player.getTimeOfWin());
                    }
                }
                if (checkLastManStanding() && !gameWin) {
                    this.gameDone = true;

                    if (!player.isWin()) {
                        player.setWin(true);
                        player.setTimeOfWin(timerOverlay.getSavedTime());

                        System.out.println("Time of win: " + player.getTimeOfWin());
                    }
                }
                if (checkIfWinnerFound() && !gameDone) {
                    this.gameDone = true;
                }
                if (getPlayer().getPlayerHealth() <= 0 && !gameOver) {
                    setGameOver(true);

                    if (player.isAlive()) {
                        player.setAlive(false);
                        player.setTimeOfDeath(timerOverlay.getSavedTime());

                        System.out.println("Time of death: " + player.getTimeOfDeath());
                    }
                }
            }
        }
    }

    private boolean checkCrownTouched() {
        return player.getHitbox().intersects(crown.getHitbox());
    }

    private boolean checkLastManStanding() {
        for (Player p : otherPlayers) {
            if (p.isAlive()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfWinnerFound() {
        for (Player p : otherPlayers) {
            if (p.isWin()) {
                return true;
            }
        }
        return false;
    }

    private void checkCloseToBorder() {
        int playerY = (int) player.getHitbox().y;
        int diff = playerY - yLevelOffset;

        if (diff < topBorder) {
            yLevelOffset += diff - topBorder;
        }

        if (yLevelOffset > offsetCopy) {
            yLevelOffset = offsetCopy;
        }

        if (yLevelOffset > 0) {
            yLevelOffset = 0;
        } else if (yLevelOffset < minLevelOffsetY) {
            yLevelOffset = minLevelOffsetY;
        }
    }

    private void checkIfWithinVisible() {
        if (player.getHitbox().y + (player.getHeight() / 2) > Game.GAME_HEIGHT + yLevelOffset) {
            player.changeHealth(-3);
        }
    }

    // Draw Method
    @Override
    public void draw(Graphics g) {
        // Change render according to room
        if (isLobby && !isPlaying) {
            levelHandler.draw(g, 0);

            ArrayList<Player> playersCopy = new ArrayList<>(otherPlayers);
            for (Player player : playersCopy) {
                player.render(g, 0);
            }
            player.render(g, 0);

            lobbyOverlay.draw(g);
        } else if (!isLobby && isPlaying) {
            levelHandler.draw(g, yLevelOffset);

            ArrayList<Player> playersCopy = new ArrayList<>(otherPlayers);
            for (Player player : playersCopy) {
                player.render(g, yLevelOffset);
            }
            player.render(g, yLevelOffset);
            crown.render(g, yLevelOffset);
            countdownOverlay.draw(g);

            if (countdownOverlay.getCount() == 0) {
                timerOverlay.draw(g);
            }

            if (!player.isAlive()) {
                gameOverOverlay.draw(g);
            }

            if (gameDone) {
                levelCompleteOverlay.draw(g, gameWin);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (isLobby) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_T:
                    if (!chatInterface.isChatVisible()) {
                        chatInterface.requestFocus();
                        chatInterface.setChatVisible(true);
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (chatInterface.isChatVisible()) {
                        chatInterface.setChatVisible(false);
                    } else {
//                    client.shutdown();
//                    clientConnected = false;
//                    server.shutdownServer();
//                    serverRunning = false;

                        Gamestate.state = Gamestate.MENU;
                    }
                    break;
            }
        } else if (isPlaying) {
            if (gameOver) {
                gameOverOverlay.keyPressed(e);
            } else if (gameWin) {
                levelCompleteOverlay.keyPressed(e);
            } else {
                if (countdownOverlay.getCount() == 0) {
                    switch(e.getKeyCode()) {
                        case KeyEvent.VK_W, KeyEvent.VK_SPACE:
                            player.setJump(true);
                            break;
                        case KeyEvent.VK_A:
                            player.setLeft(true);
                            break;
                        case KeyEvent.VK_D:
                            player.setRight(true);
                            break;
                        case KeyEvent.VK_T:
                            if (!chatInterface.isChatVisible()) {
                                chatInterface.requestFocus();
                                chatInterface.setChatVisible(true);
                            }
                            break;
                        case KeyEvent.VK_ESCAPE:
                            if (chatInterface.isChatVisible()) {
                                chatInterface.setChatVisible(false);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_SPACE:
                player.setJump(false);
                break;
            case KeyEvent.VK_A:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // Misc Methods
    public Player getPlayer() {
        return player;
    }

    public void resetAll() {
        // Resetting timers
        timerStart = false;
        countdownStart = false;
        timerOverlay = new TimerOverlay();
        countdownOverlay = new CountdownOverlay();

        // Resetting win/lose conditions
        gameOver = false;
        gameWin = false;
        player.resetAll();
        yLevelOffset = 0;
    }

    private void calculateOffset() {
        minLevelOffsetY = levelHandler.getCurrentLevel().getMinLevelOffset();
    }

    private void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private void setGameWin(boolean gameWin) {
        this.gameWin = gameWin;
    }

    public int getyLevelOffset() {
        return this.yLevelOffset;
    }

    public void setyLevelOffset(int yLevelOffset) {
        this.offsetCopy = yLevelOffset;
//        System.out.println("Create: " + this.offsetCopy);
    }
}
