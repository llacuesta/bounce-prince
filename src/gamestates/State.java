package gamestates;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import entities.Player;
import main.Game;
import ui.MenuButton;

public class State {

	// Instance Attributes
	protected Game game;
    protected ArrayList<Player> otherPlayers;
	
	// Constructor
	public State(Game game) {
		this.game = game;
	}
	
	// Getters
	public Game getGame() {
		return this.game;
	}

	// Methods
	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
    public void setOtherPlayers(ArrayList<Player> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }
}
