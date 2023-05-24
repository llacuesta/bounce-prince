package gamestates;

public enum Gamestate {

	PLAYING, MENU, CREATE, JOIN, TUTORIAL, CREDITS, QUIT;
	
	public static Gamestate state = MENU;
}
