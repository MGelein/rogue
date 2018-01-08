package trb1914.rogue.ui;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.grid.Grid;

/**
 * This class can amazingly display both text and icon :O
 * @author trb1914
 */
public class FancyButton extends TextButton {
	
	/** The image we're drawing to the button*/
	protected PImage image;
	
	/**
	 * Creates a new FancyButton using the provided parameters
	 * @param x
	 * @param y
	 * @param type
	 * @param text
	 * @param icon
	 */
	public FancyButton(int x, int y, ButtonType type, String text, PImage icon) {
		super(x, y, type, text);
		image = icon;
	}
	
	/**
	 * Override of updateDim to provide better updating
	 * with the graphic
	 */
	protected void updateDim(PGraphics g) {
		super.updateDim(g);
		off.x += Grid.SIZE;
		dim.x += 1;
	}
	
	/**
	 * Renders the textbutton, and then renders the icon to the left of it
	 */
	public void render(PGraphics g) {
		super.render(g);
		g.image(image, pos.x + Grid.SIZE * 0.5f, pos.y + Grid.SIZE * 0.5f);
	}
}
