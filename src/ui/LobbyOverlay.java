package ui;

// Imports
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import main.Game;
import utils.LoadSave;

public class LobbyOverlay {

    // Instance Attributes
    private String ip;
    private String port;
    private Font font;

    // Constructor
    public LobbyOverlay(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    // Update Method
    public void update() {
        // TODO: Check if the player count is 4/4 then allow start game to be clickable
    }

    // Render Method
    public void draw(Graphics g) {
        // Import Font
        font = LoadSave.ImportFont(LoadSave.FONT).deriveFont(40f);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString("Host IP: " + ip, (int) (10 * Game.TILE_SCALE), (int) (18 * Game.TILE_SCALE));
        g.drawString("Host Port: " + port, (int) (10 * Game.TILE_SCALE), (int) (28 * Game.TILE_SCALE));
    }
}
