package trb1914.rogue.input;

import java.util.ArrayList;

import trb1914.rogue.Rogue;
import trb1914.rogue.interfaces.IUpdate;
import trb1914.rogue.render.*;
/**
 * Mouse input handling and redistribution. This class should not be initialized.
 * It is used as a Singleton of sorts to manage mouse events
 * @author trb1914
 */
abstract public class MouseDistributor implements IUpdate{
	/** All mouse objects that have subscribed to the mouse events*/
	private final static ArrayList<MouseAble> subscribers = new ArrayList<MouseAble>();
	/** All new mouseAble objects to add next update*/
	private final static ArrayList<MouseAble> toAdd = new ArrayList<MouseAble>();
	/** All mouseAble objects to remove next update*/
	private final static ArrayList<MouseAble> toRemove = new ArrayList<MouseAble>();
	
	/** Mouse x-coord last time it moved*/
	private static float oX = 0;
	/** Mouse y-coord last time it moved*/
	private static float oY = 0;
	
	/** Flag to indicate we need to do a reset of subscribers*/
	private static boolean reset = false;
	
	/**
	 * Updates the mouse coords if they have changed, if so
	 * notify the subscribers
	 * @param mX	the x-coord of the mouse
	 * @param mY	the y-coord of the mouse
	 */
	public static void update(float mX, float mY) {
		//See if the mouse has changed, if so, notify subscribers
		if(mX != oX || mY != oY) {
			for(MouseAble m : subscribers) {
				m.mouseMove(scl(mX), scl(mY));
			}
			//Update last known position
			oX = mX;
			oY = mY;
		}
		
		//If we need to reset, do it after updating everyone
		if(reset) subscribers.clear();
		
		//Check if the lists need maintenance
		if(toAdd.size() > 0 || toRemove.size() > 0) {
			//Add any that want added
			if(toAdd.size() > 0) {
				for(MouseAble m : toAdd) subscribers.add(m);
				toAdd.clear();
			//Remove any that want from the list
			}else {
				for(MouseAble m : toRemove) subscribers.remove(m);
				toRemove.clear();
			}
		}
	}
	
	/**
	 * Distributes and forwards this mouse click event to all subscribers
	 * @param mX the x-coord of the press
	 * @param mY the y-coord of the press
	 */
	public static void mouseDown(float mX, float mY) {
		for(MouseAble m : subscribers) {
			m.mouseDown(scl(mX), scl(mY));
		}
	}
	
	/**
	 * Distributes and forwards this mouse release event to all subscribers
	 * @param mX the x-coord of the release
	 * @param mY the y-coord of the release
	 */
	public static void mouseUp(float mX, float mY) {
		for(MouseAble m : subscribers) {
			m.mouseUp(scl(mX), scl(mY));
		}
	}
	
	/**
	 * Divides the provided value by Rogue.SCL and floors it
	 * @param val the value to calculate with
	 * @return
	 */
	private static int scl(float val) {
		return Rogue.floor(val / Rogue.SCL);
	}
	
	/**
	 * Call this function to remove all subscribers, for example on a
	 * game state change.
	 */
	public static void reset() {
		reset = true;
	}
	
	/**
	 * Adds the provided MouseAble object to the list of objects
	 * to be added on the next update
	 * @param m
	 */
	public static void add(MouseAble m) {
		toAdd.add(m);
	}
	
	/**
	 * Adds the provided MouseAble object to the list of objects to
	 * be removed on the next update
	 * @param m
	 */
	public static void remove(MouseAble m) {
		toRemove.add(m);
	}
	
}
