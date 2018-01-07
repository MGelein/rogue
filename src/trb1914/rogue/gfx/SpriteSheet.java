package trb1914.rogue.gfx;

import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * Loads an image file and parses it as a spritesheet. Now you can retrieve
 * sprites from it based on index or coordinates
 * @author trb1914
 */
public class SpriteSheet {

	/** The loaded image*/
	private PImage sheet;
	/** The chopped up spritesheet*/
	private PImage[] sprites;
	/** The size (w = h) of one sprite*/
	private int size;
	/** The amount of columns this sheet has*/
	private int cols;
	/** The amount fo rows this sheet has*/
	private int rows;
	
	/**
	 * Creates a new SpriteSheet instance from the provided tileSize parameter
	 * and the url that specifies where the spritesheet is stored
	 * @param tileSize the size of the tiles
	 * @param url	the location of the image file
	 */
	public SpriteSheet(int tileSize, String url) {
		load(tileSize, url);
	}
	
	/**
	 * Loads an image and stores it in this instance, also chops it up in separate sprites
	 * @param tileSize
	 * @param url
	 */
	public void load(int tileSize, String url) {
		//Set size and sheet reference
		size = tileSize;
		sheet = Rogue.app.loadImage("./data/" + url);
		
		//Calculate the amt of cols and rows
		cols = Rogue.floor(sheet.width / size);
		rows = Rogue.floor(sheet.height / size);
		
		//Now populate the sprites array
		sprites = new PImage[cols * rows];
		for(int x = 0; x < cols; x++) {
			for(int y = 0; y< rows; y++) {
				sprites[x + y * cols] = sheet.get(x * size, y * size, size, size);
			}
		}
	}
	
	/**
	 * Returns the PImage that is in the spriteSheet at that index
	 * @param index
	 * @return
	 */
	public PImage get(int index) {
		return sprites[index % sprites.length];
	}
	
	/**
	 * Returns the PImage that is in the spritesheet at the specified coords
	 * @param x
	 * @param y
	 * @return
	 */
	public PImage get(int x, int y) {
		//Check if we are within the bounds of the sheet
		if(x < 0 || x >= cols || y < 0 || y >= rows) System.err.println("Invalid texture coord: " + x + " ,"  + y);
		return get(x + y * cols);
	}
	
	/**
	 * Returns the PImage that is in the spritesheet at the specified coords
	 * @param coords
	 * @return
	 */
	public PImage get(Int2D coords) {
		return get(coords.x, coords.y);
	}
	
}
