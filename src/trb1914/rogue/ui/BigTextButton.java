package trb1914.rogue.ui;

import processing.core.PGraphics;
import trb1914.rogue.grid.Grid;

/**
 * An even bigger text button. Useful for the mainMenu for example
 * @author trb1914
 */
public class BigTextButton extends TextButton {
	/**
	 * Creates a new BigTextButton with the provided parameters
	 * @param x
	 * @param y
	 * @param type
	 * @param text
	 */
	public BigTextButton(int x, int y, ButtonType type, String text){
		super(x, y, type, text);
		dim.y = 3;
	}

	/**
	 * Called when we need to update the dimensions of the BigTextbutton
	 */
	public void updateDim(PGraphics g){
		g.textSize(16);
		super.updateDim(g);
		off.y += Grid.SIZE * 0.66f;
		g.textSize(8);
	}

	public void render(PGraphics g){
		g.textSize(16);
		super.render(g);
		g.textSize(8);
	}
}
