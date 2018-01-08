package trb1914.rogue.ui;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.grid.Grid;

/**
 * One tile high button that displays both text and graphic
 * @author trb1914
 */
public class SmallFancyButton extends SmallTextButton{

	/** The image we're drawing on the button surface*/
	protected PImage image;

	/**
	 * Creates a new SmallFancyButton using the providedd parameters
	 * @param x
	 * @param y
	 * @param type
	 * @param text
	 * @param icon
	 */
	public SmallFancyButton(int x, int y, ButtonType type, String text, PImage icon) {
		super(x, y, type, text);
		image = icon;
		image.resize(8, 8);
	}

	/**
	 * Overridden update dimension code to allow more room for the icon
	 */
	public void updateDim(PGraphics g){
		super.updateDim(g);
		off.x += Grid.SIZE * 0.75;
		dim.x += 1;
	}
	
	/**
	 * Overriddeen render code to allow to render the icon last
	 */
	public void render(PGraphics g){
		super.render(g);
		g.image(image, pos.x + Grid.SIZE * 0.5f, pos.y + Grid.SIZE * 0.5f - 3);
	}
}
