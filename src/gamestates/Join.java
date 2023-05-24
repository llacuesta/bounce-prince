package gamestates;

import chat.Client;
import main.Game;
import ui.ChatOverlay;
import ui.JoinOverlay;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Join extends State implements StateMethods {

    // Instance Entities
    private Client client;
    private boolean clientRunning;
    private ChatOverlay chatInterface;
    private JoinOverlay joinInterface;

    // Constructor
    public Join(Game game) {
        super(game);
        initClasses();
    }

    // Init Method
    private void initClasses() {
        // Initiating Classes
        client = new Client();
    }

    @Override
    public void update() {
        // Getting Chat interface
        if (!Objects.isNull(game.getGamePanel())) {
            chatInterface = game.getGamePanel().getChatInterface();
            joinInterface = game.getGamePanel().getJoinInterface();
            chatInterface.setChatClient(client);
            joinInterface.setJoinClient(client);

        }
    }

    @Override
    public void draw(Graphics g) {
        // Getting Chat interface
        if (!Objects.isNull(game.getGamePanel())) {
            joinInterface.setJoinVisible(true);
            chatInterface.setChatVisible(true);
        }


        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
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
