package trb1914.rogue.gfx;

import processing.core.PImage;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * Encapsulates all kinds of texture data, such as frame,
 * sheet and animation data
 * @author trb1914
 */
public class Tex {
	
	/** The image we're actually drawing*/
	public PImage img;
	/** The definition name we're using to load*/
	public String name;
	/** The theme that was active to load this texture*/
	public Int2D theme;
	/** The name of the sheet this was loaded from*/
	public String sheet;
	/** The coordinates on the sheet this was loaded from*/
	public Int2D coord;
	/** If this texture is animated, can be overriden*/
	public boolean animated;
	/** The animation frame for this texture*/
	public int frame = 0;
	/** The width and height this texture is rendered at*/
	public int size = Grid.GRID_SIZE;

	/**
	 * Creates a new texture from the provided string definition
	 * @param s
	 */
	public Tex(String s) {
		parse(s);
	}
	
	/**
	 * Creates a new texture and overrides the automatic animation detection
	 * @param s
	 * @param animated
	 */
	public Tex(String s, boolean animated) {
		parse(s, animated);
	}
	
	/**
	 * Loads the provided String definition and auto animates
	 * in case the texture is an animated one
	 * @param s
	 */
	public void parse(String s) {
		parse(s, Textures.isAnimated(s));
	}
	
	/**
	 * Loads the provided String definition and overrides the automatic 
	 * animation detection
	 * @param s
	 */
	public void parse(String s, boolean animated) {
		this.animated = animated;
		name = s;
		sheet = Textures.getSheetName(s);
		coord = Textures.getTexCoord(s);
		img = Textures.get(s);
		theme = Textures.theme;
		frame = 0;
	}
	
	/**
	 * Reloads the texture by requerying the Textures DB
	 */
	public void reload() {
		parse(name, animated);
	}
	
	/**
	 * Forwards the animation by one frame and updates the texture accordingly
	 */
	public void animate() {
		frame++;
		img = Textures.getCached(sheet, coord, frame);
	}
}
