package gamestates;

import chat.Client;
import entities.Player;
import levels.LevelHandler;
import main.Game;
import ui.ChatOverlay;
import ui.JoinOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

public class Join extends State implements StateMethods {

    // Instance Entities
    private Player player;
    private LevelHandler levelHandler;
    private ArrayList<Player> otherPlayers;

    // Client Entities
    private Client client;
    private ChatOverlay chatInterface;
    private JoinOverlay joinInterface;
    private boolean interfacesInitialized = false;

    // Constructor
    public Join(Game game) {
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

        // Initiating Client
        client = new Client();
    }

    @Override
    public void update() {
        // Updating level
        levelHandler.update();

        // Updating players
        for (Player player : otherPlayers) {
            player.update();
        }
        player.update();

        // Getting interfaces
        if (!Objects.isNull(game.getGamePanel())) {
            chatInterface = game.getGamePanel().getChatInterface();
            joinInterface = game.getGamePanel().getJoinInterface();

            // Setting up chat
            chatInterface.setChatClient(client);

            // Setting up join overlay
            joinInterface.setJoinClient(client);
            joinInterface.setPlayer(player);
            joinInterface.setGamestate(this);
            interfacesInitialized = true;
        }
    }

    @Override
    public void draw(Graphics g) {
        // Getting Chat interface
        if (!Objects.isNull(game.getGamePanel()) && interfacesInitialized) {
            if (!client.isClientStarted()) {
                joinInterface.setJoinVisible(true);

                g.setColor(new Color(0, 0, 0));
                g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            }

            if (client.isClientStarted()) {
                levelHandler.draw(g, 0);

                // Drawing players
                for (Player player : otherPlayers) {
                    player.render(g, 0);
                }
                player.render(g, 0);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (client.isClientStarted()) {
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
                    }
                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (client.isClientStarted()) {
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

    public void setOtherPlayers(ArrayList<Player> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }
}
