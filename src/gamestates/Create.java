package gamestates;

import chat.Server;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.ChatOverlay;
import ui.LobbyOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Create extends State implements StateMethods {

    // Instance Entities
    private Player player;
    private LevelHandler levelHandler;

    // Server-Client Entities
    private Server server;
    private LobbyOverlay lobbyOverlay;
    private boolean serverRunning = false;
    private ChatOverlay chatInterface;

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

        server = new Server();
        lobbyOverlay = new LobbyOverlay(server.getIP(), server.getPort());
    }

    // Update Method
    @Override
    public void update() {
        // Getting Chat interface
        if (!Objects.isNull(game.getGamePanel())) {
            chatInterface = game.getGamePanel().getChatInterface();
        }

        // Running server
        if (!serverRunning) {
            serverRunning = true;
            server.startServer();
        }

        levelHandler.update();
        player.update();
    }

    // Draw Method
    @Override
    public void draw(Graphics g) {
        levelHandler.draw(g, 0);
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
                    Gamestate.state = Gamestate.MENU;
                    server.shutdownServer();
                    serverRunning = false;
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
