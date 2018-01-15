package trb1914.rogue.grid;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.RenderAble;

/**
 * Minimap holds the display overview of the dungeon
 * @author trb1914
 */
public class MiniMap extends RenderAble{
	
	/** Color used for undiscovered tiles*/
	public static final int UNDISCOVERED = Rogue.app.color(0);
	/** The collor that represents the player*/
	public static final int PLAYER = Rogue.app.color(255);

	/** The image we're rendering the mini map to*/
	private PImage img;
	/** The grid we're rederning, shortcut for Grid.current*/
	private Grid grid;
	/** The position of the minimap*/
	private Int2D pos;
	
	/**
	 * Creates a new MiniMap of the specified grid
	 */
	public MiniMap(Grid grid) {
		img = new PImage(grid.cols, grid.rows);
		int x = Rogue.stage.width - img.width * 2;
		this.grid = grid;
		this.pos = new Int2D(x, 0);
		update();
	}
	
	/**
	 * Updates the minimap with the currently visible state of the game
	 */
	public void update() {
		//Start updting the image
		img.loadPixels();
		
		//for every cell apply a color
		GridCell[] cells = grid.getCells();
		for(int i = 0 ; i < cells.length; i++) {
			img.pixels[i] = cells[i].getMiniMapColor();
		}
		
		//Now update the image
		img.updatePixels();
	}
	
	public void render(PGraphics g) {
		g.stroke(255);
		g.fill(0);
		g.rect(pos.x, pos.y, img.width * 2, img.height * 2);
		g.image(img, pos.x, pos.y, img.width * 2, img.height * 2);
	}
}
