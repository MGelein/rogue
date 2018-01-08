package trb1914.rogue.particles;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import trb1914.rogue.grid.Grid;

/**
 * A pixel particle
 * @author trb1914
 */
public class PParticle {
	
	/** Gravity for every particle*/
	private final static PVector grav = new PVector(0, 0.03f);
	/** The friction on the particles*/
	private final static float friction = 0.97f;
	/** The force of the explosion*/
	private final static float force = 0.3f;
	
	/**The one pixel of this image*/
	private PImage pixel;
	/** The position of this particle*/
	private PVector pos = new PVector();
	/** Size to shrink over time*/
	private float size = Grid.SCL;
	/** A random 2D direction on spawning*/
	private PVector vel = PVector.random2D().mult(force);
	
	/**
	 * Creates a new PixelParticle at the provided location
	 * @param x
	 * @param y
	 * @param c
	 */
	protected PParticle(int x, int y, PImage img) {
		pixel = img;
		pos.x = x;
		pos.y = y;
	}

	/**
	 * Updates the particle
	 */
	protected boolean update() {
		pos.add(vel);
		vel.add(grav);
		vel.mult(friction);
		
		if(vel.mag() < 0.1) return false;
		
		if(pos.y > Grid.SIZE) vel.y *= -.6f;
		return true;
	}
	
	/**
	 * Renders this one pixelParticle
	 * @param g
	 */
	protected void render(PGraphics g) {
		g.image(pixel, pos.x * Grid.SCL, pos.y * Grid.SCL, size, size);
	}
}
