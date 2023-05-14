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
	
	// TODO: Change width and height to int
	protected void initHitbox(float x, float y, float width, float height) {
		this.hitbox = new Rectangle2D.Float(x, y, width, height);
	}

	protected  void setHitbox(float x, float y) {
		this.hitbox.x = x;
		this.hitbox.y = y;
	}
	
	protected void drawHitbox(Graphics g) {
		// For debugging purposes only
		g.setColor(Color.PINK);
		g.drawRect((int) this.hitbox.x, (int) this.hitbox.y, (int) this.hitbox.width, (int) this.hitbox.height);
	}
	
	public Rectangle2D.Float getHitbox() {
		return this.hitbox;
	}
	
	public int getHeight() {
		return this.height;
	}
}
