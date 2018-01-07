package trb1914.rogue.gfx;

import java.util.ArrayList;

import processing.core.PImage;
import trb1914.rogue.math.Int2D;

/**
 * Collection of SpriteSheets that can be played one after the other
 * to get the illusion of animation
 * @author trb1914
 */
public class AnimatedSheet{
	
	/** The list of spritesheets that are part of this animated sheet*/
	private ArrayList<SpriteSheet> sheets = new ArrayList<SpriteSheet>();
	
	/**
	 * Creates a new animated tileSheet from the provided size and list of file urls
	 * @param tileSize
	 * @param strings
	 */
	public AnimatedSheet(int tileSize, String... sheetUrls) {
		for(String url : sheetUrls) {
			sheets.add(new SpriteSheet(tileSize, "Dawnlike/" +  url));
		}
	}
	
	/**
	 * Request the sprite at the specified position of the specified frame
	 * @param pos
	 * @param frame
	 * @return
	 */
	public PImage get(Int2D pos, int frame) {
		return get(pos.x, pos.y, frame);
	}
	
	/**
	 * Request the sprite at the specified position of the specified frame
	 * @param x
	 * @param y
	 * @param frame
	 * @return
	 */
	public PImage get(int x, int y, int frame) {
		return sheets.get(frame % sheets.size()).get(x, y);
	}
	
	/**
	 * Request the sprite at the specified position of the specified frame
	 * @param index
	 * @param frame
	 * @return
	 */
	public PImage get(int index, int frame) {
		return sheets.get(frame % sheets.size()).get(index);
	}
}
