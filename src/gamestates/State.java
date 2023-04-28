package gamestates;

import main.Game;

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
