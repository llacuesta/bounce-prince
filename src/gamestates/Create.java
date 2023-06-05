package gamestates;

import chat.Client;
import chat.GamePlayer;
import chat.GameServer;
import chat.Server;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.ChatOverlay;
import ui.LobbyOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Objects;

public class Create extends State implements StateMethods {

    // Instance Entities
    private Player player;
    private LevelHandler levelHandler;

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
        levelHandler.update();

        for (Player player : otherPlayers) {
        	player.update();
        }
        player.update();
    	
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
    }

    // Draw Method
    @Override
    public void draw(Graphics g) {
        levelHandler.draw(g, 0);
        ArrayList<Player> playersCopy = new ArrayList<>(otherPlayers);
        for (Player player : playersCopy) {
            player.render(g, 0);
        }
        player.render(g, 0);
        lobbyOverlay.draw(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
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
}
