package gamestates;

import java.awt.event.MouseEvent;

import main.Game;
import ui.UrmButton;

public class State {

	// Instance Attributes
	protected Game game;
	
	// Constructor
	public State(Game game) {
		this.game = game;
	}
	
	// Getters
	public Game getGame() {
		return this.game;
	}
}
