package trb1914.rogue.ui;

import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 * A button that instead of text displays an image(8x8)
 * @author trb1914
 */
public class IconButton extends Button{
	/** The image we're drawing to the button surface*/
	protected PImage image;
	/** The offset this image is rendered at*/
	protected PVector off = new PVector(-4, -4);
	
	/**
	 * Creates a new IconButton using the provided Iconimage
	 * @param x
	 * @param y
	 * @param type
	 * @param image
	 */
	public IconButton(int x, int y, ButtonType type, PImage image) {
		super(x, y, type);
		this.image = image;
	}
	
	/**
	 * Also renders the image to the button surface
	 */
	public void render(PGraphics g) {
		super.render(g);
		g.image(image, pos.x + off.x, pos.y + off.y);
	}
}
