package trb1914.rogue.render;

import processing.core.PGraphics;

/**
 * All renderable objects inherit from this object. Also contains a boolean to
 * track if this object is highlighted (moused over). 
 * @author trb1914
 */
public class RenderAble {
	/** Can be used by mouseable objects to register if we're highlighted*/
	protected boolean highlighted = false;
	
	/**
	 * Render this object using the provided PGraphics interface.
	 * @param g	the provided PGraphics interface
	 */
	public void render(PGraphics g) {};
}
