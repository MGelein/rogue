package trb1914.rogue.ui;

import processing.core.PGraphics;
import trb1914.rogue.grid.Grid;

/**
 * Small button that is only 1 tile high, but can still house text
 * @author trb1914
 */
public class SmallTextButton extends TextButton {
	/**
	 * Creates a new SmallTextButton using the provided parameters
	 * @param x
	 * @param y
	 * @param type
	 * @param text
	 */
	SmallTextButton(int x, int y, ButtonType type, String text){
		super(x, y, type, text);
		small = true;
	}

	/**
	 * The small button needs a slightly different updateDimension method
	 */
	public void updateDim(PGraphics g){
		super.updateDim(g);
		off.y = Grid.SIZE - 3;
	}
}
