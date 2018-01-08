package trb1914.rogue.ui;

import processing.core.PImage;

/**
 * A tile button makes any tile from the game fit on that button
 * @author trb1914
 */
public class TileButton extends IconButton{
	
	/**
	 * Creates a new TileButton that will resize the provided image to fit on a button
	 * @param x
	 * @param y
	 * @param type
	 * @param image
	 */
	public TileButton(int x, int y, ButtonType type, PImage image) {
		super(x, y, type, image);
		image.resize(8, 8);
		off.x = off.y = 4;
	}
}
