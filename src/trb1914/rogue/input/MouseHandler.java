package trb1914.rogue.input;

import trb1914.rogue.render.MouseAble;

/**
 * Use this class to make an anonymous object of it for mouseEvent
 * handling
 * @author trb1914
 */
public abstract class MouseHandler {
	/** The MouseObject that called this handler*/
	private MouseAble target;
	
	/**
	 * Sets the target of this mouseHandler
	 * @param t	the target of this mouseHandler
	 */
	public void setTarget(MouseAble t) {
		target = t;
	}
	
	/**
	 * Called by the MouseDistributor whenever the mouse moves
	 * @param x	the new x position of the mouse
	 * @param y the new y position of the mouse
	 */
	public void mouseMove(int x, int y) {};
	
	/**
	 * Called by the MouseDistributor whenever the mouse is pressed down
	 * @param x	the x-coord of the press
	 * @param y the y-coord of the press
	 */
	public void mouseDown(int x, int y) {};
	
	/**
	 * Called by the MouseDistributor whenever the mouse is released
	 * @param x	the x-coord of the release
	 * @param y the y-coord of the release
	 */
	public void mouseUp(int x, int y) {};
}
