package trb1914.rogue.state;

import java.util.ArrayList;

import processing.core.PGraphics;
import trb1914.rogue.input.MouseDistributor;
import trb1914.rogue.interfaces.IUpdate;
import trb1914.rogue.render.MouseAble;
import trb1914.rogue.render.RenderAble;
import trb1914.rogue.util.ManagedList;

/**
 * Every state of the game (Main menu, main game, settings, etc) all inherit from this screen
 * @author trb1914
 */
public class GameState {
	
	/** The GameState that is currently showing, updating, etc*/
	public static GameState current;
	
	/** The new managed list type of renderAble ites*/
	private ManagedList<RenderAble> renders = new ManagedList<RenderAble>();

	/** List of objects we should update*/
	private ManagedList<IUpdate> updaters = new ManagedList<IUpdate>();

	/**
	 * Every gamestate has an update function in which we update all subscribed updaters
	 */
	public void update() {
		//Update all objects
		for(IUpdate u : updaters) u.update();

		//Do list maintenance
		updaters.doMaintenance();
	}

	/**
	 * Adds one or more IUpdate objects to the list of objects to be updated in this state
	 * @param updaters
	 */
	public void addUpdate(IUpdate... updateObjs) {
		for(IUpdate u : updateObjs) updaters.add(u);
	}

	/**
	 * Removes one or more IUpdate objects from the list of objects to be updated in this state
	 * @param updaters
	 */
	public void removeUpdate(IUpdate... updateObjs) {
		for(IUpdate u : updateObjs) updaters.add(u);
	}

	/**
	 * Renders all the renderAble objects in this state using the provided
	 * PGraphics object
	 * @param g
	 */
	public void render(PGraphics g) {
		//First render all objects
		for(RenderAble r : renders) r.render(g);

		//Now do list maintenance
		renders.doMaintenance();
	}
	
	/**
	 * Adds one or more RenderAble objects to the list of objects to be rendered in this state
	 * @param renders
	 */
	public void addRender(RenderAble...renderObjs) {
		for(RenderAble r : renderObjs) renders.add(r);
	}
	
	/**
	 * Removes one or more RenderAble objects from the list of objects to be rendered in this state
	 * @param renders
	 */
	public void removeRender(RenderAble...renderObjs) {
		for(RenderAble r : renderObjs) renders.remove(r);
	}
	
	/**
	 * Adds one or more objects to the subscriber list of the mouseDistibutor
	 * @param mousers
	 */
	public void addMouse(MouseAble... mouseObjs) {
		for(MouseAble m : mouseObjs) MouseDistributor.add(m);
	}
	
	/**
	 * Removes one or more objects frm the subscriber list of the mousedistributor
	 * @param mousers
	 */
	public void removeMouse(MouseAble... mouseObjs) {
		for(MouseAble m : mouseObjs) MouseDistributor.remove(m);
	}
	
	/**
	 * Unloads the current GameState and starts the new one
	 * @param gs
	 */
	public void change(GameState gs) {
		unload();
		GameState.current = gs;
	}
	
	/**
	 * Called when the GameState is changed, this currently only resets
	 * the MouseDistributor, but a Gamestate may give its own extra functionality
	 */
	private void unload() {
		MouseDistributor.reset();
	}
}
