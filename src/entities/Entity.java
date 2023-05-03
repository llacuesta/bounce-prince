package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

	// Instance Attributes
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;

	// Constructor
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void initHitbox(float x, float y, float width, float height) {
		this.hitbox = new Rectangle2D.Float(x, y, width, height);
	}
	
	protected void drawHitbox(Graphics g) {
		// For debugging purposes only
		g.setColor(Color.PINK);
		g.setColor(Color.PINK);
		g.drawRect((int) this.hitbox.x, (int) this.hitbox.y, (int) this.hitbox.width, (int) this.hitbox.height);
	}
	
//	protected void updateHitbox() {
//		this.hitbox.x = (int) x;
//		this.hitbox.y = (int) y;
//	}
	
	public Rectangle2D.Float getHitbox() {
		return this.hitbox;
	}
}
