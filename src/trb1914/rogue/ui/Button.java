package trb1914.rogue.ui;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.input.MouseHandler;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.MouseAble;

/**
 * Simple Button class. Might be a bit hackish and they seem to be not too good for 
 * performance, maybe because of composite nature
 * @author trb1914
 */
public class Button extends MouseAble{
	/** Normal tiles, used when not highlughted*/
	private PImage[] normal = new PImage[10];
	/** hlightS tiles, used when moused over*/
	private PImage[] hlight = new PImage[10];

	/** Used as a flag to indicate small buttons*/
	protected boolean small = false;

	/** The position of the button*/
	protected Int2D pos;
	/** The tile dimensions of the button*/
	protected Int2D dim = new Int2D(1, 1);

	/**
	 * Creates a new button using the provided parameters
	 * @param x
	 * @param y
	 * @param type
	 */
	public Button(int x, int y, ButtonType type) {
		//Set the position
		pos = new Int2D(x, y);

		//Depending on type, load the right tiles
		if(type == ButtonType.BLUE) loadTiles("black_blue", "blue_orange");
		else if(type == ButtonType.GREEN) loadTiles("black_green", "green_red");
		else if(type == ButtonType.YELLOW) loadTiles("black_yellow", "yellow_black");
		else loadTiles("black_red", "red_green");

		//Add the hlightS listener
		addMouseHandler(new MouseHandler() {
			/** See if this location is on top of the button*/
			public void mouseMove(int x, int y) {
				highlighted = x > pos.x && x < pos.x + getWidth() && y > pos.y && y < pos.y + getHeight();
			}
		});
	}

	/**
	 * The pixel width of this button
	 * @return
	 */
	private float getWidth() {
		return dim.x * Grid.SIZE;
	}

	/**
	 * The pixel height of this button
	 * @return
	 */
	private float getHeight() {
		return dim.y * Grid.SIZE;
	}

	/**
	 * Loads the provided normal and hlightS tiles into their respective arrays
	 * @param normal
	 * @param hlight
	 */
	private void loadTiles(String normalS, String hlightS) {
		normal[0] = Textures.get("gui.box_" + normalS + "_tl");
		normal[1] = Textures.get("gui.box_" + normalS + "_tm");
		normal[2] = Textures.get("gui.box_" + normalS + "_tr");
		normal[3] = Textures.get("gui.box_" + normalS + "_ml");
		normal[4] = Textures.get("gui.box_" + normalS + "_mm");
		normal[5] = Textures.get("gui.box_" + normalS + "_mr");
		normal[6] = Textures.get("gui.box_" + normalS + "_bl");
		normal[7] = Textures.get("gui.box_" + normalS + "_bm");
		normal[8] = Textures.get("gui.box_" + normalS + "_br");
		normal[9] = Textures.get("gui.box_" + normalS);

		hlight[0] = Textures.get("gui.box_" + hlightS + "_tl");
		hlight[1] = Textures.get("gui.box_" + hlightS + "_tm");
		hlight[2] = Textures.get("gui.box_" + hlightS + "_tr");
		hlight[3] = Textures.get("gui.box_" + hlightS + "_ml");
		hlight[4] = Textures.get("gui.box_" + hlightS + "_mm");
		hlight[5] = Textures.get("gui.box_" + hlightS + "_mr");
		hlight[6] = Textures.get("gui.box_" + hlightS + "_bl");
		hlight[7] = Textures.get("gui.box_" + hlightS + "_bm");
		hlight[8] = Textures.get("gui.box_" + hlightS + "_br");
		hlight[9] = Textures.get("gui.box_" + hlightS);
	}

	/**
	 * Centers this button on the x-axis
	 */
	public void centerX() {
		pos.x = Rogue.floor((Rogue.stage.width - getWidth()) * 0.5f);
	}

	/**
	 * Renders this button
	 */
	public void render(PGraphics g){
		//Special case for 1x1 buttons
		if(dim.x == 1 && dim.y == 1){
			renderTile(g, 9, 0, 0);
			return;
		}

		//In all other cases, check for each location how to build the button
		for(int x = 0; x < dim.x; x++){
			for(int y = 0; y < dim.y; y++){
				if(x == 0 && y == 0) renderTile(g, 0, x, y);                    //TL
				else if(x == 0 && y == dim.y - 1) renderTile(g, 6, x, y);       //BL
				else if(x == dim.x - 1 && y == 0) renderTile(g, 2, x, y);       //TR
				else if(x == dim.x - 1 && y == dim.y - 1) renderTile(g, 8, x, y);//BR
				else if(x == 0) renderTile(g, 3, x, y);                         //ML
				else if(x == dim.x - 1) renderTile(g, 5, x, y);                 //MR
				else if(y == 0) renderTile(g, 1, x, y);                         //TM
				else if(y == dim.y - 1) renderTile(g, 7, x, y);                 //BM
				else renderTile(g, 4, x, y);                                    //MM
			}
		}
	}

	/**
	 * Renders one tile of the button, special case in place for small tiles
	 * @param g
	 * @param tileIndex
	 * @param x
	 * @param y
	 */
	private void renderTile(PGraphics g, int tileIndex, int x, int y){
		if(!small){
			g.image(
					(highlighted ? hlight[tileIndex] : normal[tileIndex]),
					pos.x + (x * Grid.SIZE),
					pos.y + (y * Grid.SIZE)
					);
		}else{
			g.image(
					(highlighted ? hlight[tileIndex] : normal[tileIndex]).get(0, (y == 0 ? 0 : 8), 16, 8),
					pos.x + (x * Grid.SIZE),
					pos.y + (y * Grid.SIZE * 0.5f)
					);
		}
	}
}
