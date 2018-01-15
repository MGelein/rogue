package trb1914.rogue.gfx.light;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.io.Registry;

/**
 * This class handles the rednering of the FOW depended on the focus actor 
 * @author trb1914
 */
public class FOW {
	
	/** Amount of opacity taken off in case of discovered cell*/
	public static final float DISCOVERED_OPACITY = 60;
	
	/** The image used as overlay, when the dungeon is fully dark, what does it look like? Imagine if this animated!!!*/
	private PImage darkness;
	/** If we should render FOW, can be controlled in the game.ini*/
	private boolean renderFOW = true;
	
	/**
	 * Creates a new FOW object. Loading the texture from the registry
	 */
	public FOW() {
		//Create a new image 1x1 pixels completely black
		darkness = new PImage(1, 1);
		darkness.loadPixels();
		darkness.pixels[0] = Rogue.app.color(0);
		darkness.updatePixels();
		renderFOW = Registry.getBoolean("game.render_fow");
	}
	
	/**
	 * Renders to the provided graphics stage
	 * @param g
	 */
	public void render(PGraphics g) {
		//Don't render FOW if it was turned off in the options
		if(!renderFOW) return;
		//Now go through every cell and render their FOW
		for(GridCell cell : Grid.current.getCells()) {
			if(!cell.wasVisible()) continue;
			float opacity = 255 - 255 * FOV.getVisibility(cell.pos);
			
			//If this cell is in our FOV (opacity < 255) we need to set it as visited
			if(opacity < 255) cell.discovered = true;
			
			//Check if this cell has been discovered before
			if(cell.discovered) opacity -= DISCOVERED_OPACITY;
			
			//Now apply darkness texture according to all our calculations
			g.tint(255, opacity);
			g.image(darkness, cell.pos.x * Grid.GRID_SIZE, cell.pos.y * Grid.GRID_SIZE, Grid.GRID_SIZE, Grid.GRID_SIZE);
		}
		//reset tint
		g.tint(255);
	}
}
