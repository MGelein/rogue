package trb1914.rogue.grid;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.io.Registry;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.RenderAble;

/**
 * Minimap holds the display overview of the dungeon
 * @author trb1914
 */
public class MiniMap extends RenderAble{
	
	/** Reference to the currently active MiniMap*/
	public static MiniMap current;
	
	/** Color used for undiscovered tiles*/
	public static final int UNDISCOVERED = Rogue.app.color(0);
	/** The collor that represents the player*/
	public static final int PLAYER = Rogue.app.color(255);
	/** Width of the display*/
	public static final int WIDTH = 128;

	/** The image we're rendering the mini map to*/
	private PImage img;
	/** The grid we're rederning, shortcut for Grid.current*/
	private Grid grid;
	/** The position of the minimap*/
	private Int2D pos;
	/** The background of the minimap*/
	private PImage bg;
	/** The amount of padding inside the minimap borders*/
	private int padding = 4;
	/** Flag that if set to true allows the player to see everything on the minimap*/
	private boolean ignoreDiscovered = false;
	
	/**
	 * Creates a new MiniMap of the specified grid
	 */
	public MiniMap(Grid grid) {
		current = this;
		img = new PImage(grid.cols, grid.rows);
		int x = Rogue.stage.width - WIDTH;
		this.grid = grid;
		this.pos = new Int2D(x, 0);
		bg = createBG();
		update();
		ignoreDiscovered = Registry.getBoolean("game.clear_minimap");
	}
	
	/**
	 * Creates the Backgroudn image for this minimap
	 */
	private PImage createBG() {
		PImage image = new PImage(64, 64);
		image.copy(Textures.get("gui.box_black_red_tl"), 0, 0, 16, 16,  0,  0, 16, 16);
		image.copy(Textures.get("gui.box_black_red_tm"), 0, 0, 16, 16, 16,  0, 16, 16);
		image.copy(Textures.get("gui.box_black_red_tm"), 0, 0, 16, 16, 32,  0, 16, 16);
		image.copy(Textures.get("gui.box_black_red_tr"), 0, 0, 16, 16, 48,  0, 16, 16);
		image.copy(Textures.get("gui.box_black_red_ml"), 0, 0, 16, 16,  0, 16, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mm"), 0, 0, 16, 16, 16, 16, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mm"), 0, 0, 16, 16, 32, 16, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mr"), 0, 0, 16, 16, 48, 16, 16, 16);
		image.copy(Textures.get("gui.box_black_red_ml"), 0, 0, 16, 16,  0, 32, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mm"), 0, 0, 16, 16, 16, 32, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mm"), 0, 0, 16, 16, 32, 32, 16, 16);
		image.copy(Textures.get("gui.box_black_red_mr"), 0, 0, 16, 16, 48, 32, 16, 16);
		image.copy(Textures.get("gui.box_black_red_bl"), 0, 0, 16, 16,  0, 48, 16, 16);
		image.copy(Textures.get("gui.box_black_red_bm"), 0, 0, 16, 16, 16, 48, 16, 16);
		image.copy(Textures.get("gui.box_black_red_bm"), 0, 0, 16, 16, 32, 48, 16, 16);
		image.copy(Textures.get("gui.box_black_red_br"), 0, 0, 16, 16, 48, 48, 16, 16);
		return image;		
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
			img.pixels[i] = cells[i].getMiniMapColor(ignoreDiscovered);
		}
		
		//Now update the image
		img.updatePixels();
	}
	
	public void render(PGraphics g) {
		g.image(bg, pos.x, pos.y, WIDTH, WIDTH);
		g.image(img, pos.x + padding, pos.y + padding, WIDTH - padding * 2, WIDTH - padding * 2);
	}
}
