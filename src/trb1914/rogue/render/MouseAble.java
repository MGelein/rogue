package trb1914.rogue.render;

import java.util.ArrayList;

import trb1914.rogue.input.MouseHandler;

/**
 * All objects that support mouse interaction have to inherit from
 * this class. In this class basic Mousehandling is described. 
 * @author trb1914
 */
public class MouseAble extends RenderAble{
	/** List of all the mouseHandlers this mouseAble object has*/
	protected ArrayList<MouseHandler> mouseHandlers = new ArrayList<MouseHandler>();

	/**
	 * Called by the MouseDistributor whenever the mouse moves
	 * @param x	the new x position of the mouse
	 * @param y the new y position of the mouse
	 */
	public void mouseMove(int x, int y) {
		for(int i = mouseHandlers.size() - 1; i >= 0; i--) {
			mouseHandlers.get(i).mouseMove(x, y);
		}
	};
	
	/**
	 * Called by the MouseDistributor whenever the mouse is pressed down
	 * @param x	the x-coord of the press
	 * @param y the y-coord of the press
	 */
	public void mouseDown(int x, int y) {
		for(int i = mouseHandlers.size() - 1; i >= 0; i--) {
			mouseHandlers.get(i).mouseDown(x, y);
		}
	};
	
	/**
	 * Called by the MouseDistributor whenever the mouse is released
	 * @param x	the x-coord of the release
	 * @param y the y-coord of the release
	 */
	public void mouseUp(int x, int y) {
		for(int i = mouseHandlers.size() - 1; i >= 0; i--) {
			mouseHandlers.get(i).mouseUp(x, y);
		}
	};
	
	/**
	 * Adds the provided mouseHandler to the list of handlers for this object
	 * @param handler
	 */
	public void addMouseHandler(MouseHandler handler) {
		handler.setTarget(this);
		mouseHandlers.add(handler);
	}
	
	/**
	 * Removes the specified handler from the list of handlers for this object
	 * @param handler
	 */
	public void removeMouseHandler(MouseHandler handler) {
		mouseHandlers.remove(handler);
	}
}
