package trb1914.rogue.ui;

import processing.core.PGraphics;
import processing.core.PVector;
import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;

/**
 * A basic button with some text on it
 * @author trb1914
 */
public class TextButton extends Button{

	/** The string on the button*/
	private String text;
	/** If we need to recalculate the dimensions*/
	private boolean recalc = false;
	/** Offset of text to button top left*/
	protected PVector off = new PVector();

	/**
	 * Creates a new Textbutton of the speicfied type at the provided coords
	 * with the provided text
	 * @param x
	 * @param y
	 * @param type
	 * @param text
	 */
	public TextButton(int x, int y, ButtonType type, String text) {
		super(x, y, type);
		setText(text);
		//text buttons are nomrally 2 high
		dim.y = 2;
	}

	/***
	 * Sets the button text to the provided string
	 * @param t
	 */
	public void setText(String t) {
		text = t;
		recalc = true;
	}

	/**
	 * Updates its dimensions to fit the provided text
	 * @param g
	 */
	protected void updateDim(PGraphics g) {
		recalc = false;
		dim.x = Rogue.floor((g.textWidth(text) / Grid.SIZE) + 2);
		off.x = ((dim.x * Grid.SIZE) - g.textWidth(text)) * 0.5f;
		off.y = (dim.y * 0.66f * Grid.SIZE);
	}

	/**
	 * Called to render the button
	 */
	public void render(PGraphics g){
		super.render(g);
		//Check if we need to recalc width
		if(recalc) updateDim(g);

		if(highlighted){ //If highlighted, also draw a text shadow
			g.fill(0);
			g.text(text, pos.x + off.x + 1, pos.y + off.y + 1);
			g.fill(255);
		}
		g.text(text, pos.x + off.x, pos.y + off.y);
	}

}
