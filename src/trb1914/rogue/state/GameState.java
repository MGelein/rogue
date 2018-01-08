package trb1914.rogue.state;

import java.util.ArrayList;

import processing.core.PGraphics;
import trb1914.rogue.input.MouseDistributor;
import trb1914.rogue.interfaces.IUpdate;
import trb1914.rogue.render.MouseAble;
import trb1914.rogue.render.RenderAble;

/**
 * Every state of the game (Main menu, main game, settings, etc) all inherit from this screen
 * @author trb1914
 */
public class GameState {
	
	/** The GameState that is currently showing, updating, etc*/
	public static GameState current;
	
	/** List of objects we should render*/
	private ArrayList<RenderAble> renderObjects = new ArrayList<RenderAble>();

	/** List maintenance*/
	private ArrayList<RenderAble> renderToAdd = new ArrayList<RenderAble>();
	/** List maintenance*/
	private ArrayList<RenderAble> renderToRem = new ArrayList<RenderAble>();

	/** List of objects we should update*/
	private ArrayList<IUpdate> updateObjects = new ArrayList<IUpdate>();

	/** List maintenance*/
	private ArrayList<IUpdate> updateToAdd = new ArrayList<IUpdate>();
	/** List maintenance*/
	private ArrayList<IUpdate> updateToRem = new ArrayList<IUpdate>();

	/**
	 * Every gamestate has an update function in which we update all subscribed updaters
	 */
	public void update() {
		//Update all objects
		for(IUpdate u : updateObjects) u.update();

		//Check if we need to do list maintenance
		if(updateToAdd.size() > 0 || updateToRem.size() > 0) {
			if(updateToAdd.size() > 0) {
				for(IUpdate u : updateToAdd) updateObjects.add(u);
				updateToAdd.clear();
			}else {
				for(IUpdate u : updateToRem) updateObjects.remove(u);
				updateToRem.clear();
			}
		}
	}

	/**
	 * Adds one or more IUpdate objects to the list of objects to be updated in this state
	 * @param updaters
	 */
	public void addUpdate(IUpdate... updaters) {
		for(IUpdate u : updaters) updateToAdd.add(u);
	}

	/**
	 * Removes one or more IUpdate objects from the list of objects to be updated in this state
	 * @param updaters
	 */
	public void removeUpdate(IUpdate... updaters) {
		for(IUpdate u : updaters) updateToRem.add(u);
	}

	/**
	 * Renders all the renderAble objects in this state using the provided
	 * PGraphics object
	 * @param g
	 */
	public void render(PGraphics g) {
		//First render all objects
		for(RenderAble r : renderObjects) r.render(g);

		//Check if we need to do list maintenance
		if(renderToAdd.size() > 0 || renderToRem.size() > 0) {
			if(renderToAdd.size() > 0) {
				for(RenderAble r : renderToAdd) renderObjects.add(r);
				renderToAdd.clear();
			}else {
				for(RenderAble r : renderToRem) renderObjects.remove(r);
				renderToRem.clear();
			}
		}
	}
	
	/**
	 * Adds one or more RenderAble objects to the list of objects to be rendered in this state
	 * @param renders
	 */
	public void addRender(RenderAble...renders) {
		for(RenderAble r : renders) renderToAdd.add(r);
	}
	
	/**
	 * Removes one or more RenderAble objects from the list of objects to be rendered in this state
	 * @param renders
	 */
	public void removeRender(RenderAble...renders) {
		for(RenderAble r : renders) renderToRem.add(r);
	}
	
	/**
	 * Adds one or more objects to the subscriber list of the mouseDistibutor
	 * @param mousers
	 */
	public void addMouse(MouseAble... mousers) {
		for(MouseAble m : mousers) MouseDistributor.add(m);
	}
	
	/**
	 * Removes one or more objects frm the subscriber list of the mousedistributor
	 * @param mousers
	 */
	public void removeMouse(MouseAble... mousers) {
		for(MouseAble m : mousers) MouseDistributor.remove(m);
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
