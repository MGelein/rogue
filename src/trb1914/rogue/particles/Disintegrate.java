package trb1914.rogue.particles;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;
/**
 * Simple particle effect that has a disintegrate look.
 * Explodes a texture into pieces
 * @author trb1914
 */
public class Disintegrate extends GridObject{
	
	/** The list of particles this effect consits of*/
	private ArrayList<PParticle> particles = new ArrayList<PParticle>();
	/** Removes the specified particles from the list once its save*/
	private ArrayList<PParticle> toRem = new ArrayList<PParticle>();
	
	/**
	 * Creates a new Disintegrate at the provided position
	 * @param parent
	 * @param img
	 */
	public Disintegrate(GridCell parent, PImage img) {
		super(parent.pos, parent);
		walkable = true;
		grind(img);
	}
	
	/**
	 * Updates all particles
	 */
	public void update() {
		super.update();
		for(PParticle p : particles) {
			if(!p.update()) {//return false if outside of boundaries
				toRem.add(p);
			}
		}
		
		//List maintenance
		if(toRem.size() > 0) {
			for(PParticle p : toRem) particles.remove(p);
			
			//If no more particles are left, remove myself
			if(particles.size() == 0) {
				parent.remove(this);
			}
		}
	}
	
	/**
	 * Renders the particles
	 */
	public void render(PGraphics g) {
		g.pushMatrix();
		g.translate(pos.x * Grid.GRID_SIZE, pos.y * Grid.GRID_SIZE);
		for(PParticle p : particles) {
			p.render(g);
		}
		g.popMatrix();
	}
	
	/**
	 * Grinds the provided image into an array of particles
	 * @param img
	 */
	private void grind(PImage img) {
		//Load your pixels for inspection
		int w = img.width;
		int h = img.height;
		for(int x = 0; x < w; x ++) {
			for(int y = 0; y < h; y++) {
				particles.add(new PParticle(x, y, img.get(x, y, 1, 1)));
			}
		}
		
		//Now null the reference
		img = null;
	}
}
