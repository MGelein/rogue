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
	
	/** The image used as overlay, when the dungeon is fully dark, what does it look like? Imagine if this animated!!!*/
	private PImage darkness;
	/** If we should render FOW, can be controlled in the game.ini*/
	private boolean renderFOW = true;
	
	/**
	 * Creates a new FOW object. Loading the texture from the registry
	 */
	public FOW() {
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
		if(renderFOW) return;
		//Now go through every cell and render their FOW
		for(GridCell cell : Grid.current.getCells()) {
			g.tint(255, 255 - (cell.lightness * 255));
			g.image(darkness, cell.pos.x * Grid.GRID_SIZE, cell.pos.y * Grid.GRID_SIZE, Grid.GRID_SIZE, Grid.GRID_SIZE);
		}
		//reset tint
		g.tint(255);
	}
}
