package trb1914.rogue.grid;

import java.util.ArrayList;

import processing.core.PGraphics;
import trb1914.debug.Debug;
import trb1914.rogue.Rogue;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.RenderAble;

/**
 * This class is what the grid is made up of. Handles lighting, pathfinding status
 * and renders all objects for this cell
 * @author trb1914
 */
public class GridCell extends RenderAble{
	
	/** The position of this GridCell*/
	public Int2D pos;
	/** Reference to the grid that is holding it*/
	public Grid grid;
	/** The color this cell uses for lighting*/
	public int lighting;
	
	/** List of all objects in this cell*/
	private ArrayList<GridObject> objects = new ArrayList<GridObject>();
	/** List of all objects to be added next update*/
	private ArrayList<GridObject> toAdd = new ArrayList<GridObject>();
	/** List of all objects to be removed next update*/
	private ArrayList<GridObject> toRemove = new ArrayList<GridObject>();
	
	/**
	 * Creates a new GridCell at the specified position with the specified grid
	 * as a parent
	 * @param x
	 * @param y
	 * @param g
	 */
	public GridCell(int x, int y, Grid g) {
		pos = new Int2D(x, y);
		grid = g;
	}
	
	/**
	 * Returns whether this cell is walkable
	 * @return
	 */
	public boolean isWalkable() {
		//See if any object is not walkable, if so return it
		for(GridObject o : objects) if(!o.walkable) return false;
		return true;
	}
	
	/**
	 * Returns if this cell lets no light through
	 * @return
	 */
	public boolean isOpaque() {
		for(GridObject o : objects) if(o.opaque) return true;
		return false;
	}
	
	/**
	 * Is this object currently visible on the screen
	 * @param viewPoint
	 * @return
	 */
	boolean isVisible(){
		//Offset our position to get  a 0,0 at the center of the screen
		pos.x += Rogue.floor(Grid.COLS_VISIBLE / 2);
		pos.y += Rogue.floor(Grid.ROWS_VISIBLE / 2);
		
		//By default assume we're visible
		boolean visible = true;
		if(pos.x - grid.viewPoint.x > Grid.COLS_VISIBLE|| pos.y - grid.viewPoint.y > Grid.ROWS_VISIBLE) visible = false;
		else if(pos.x - grid.viewPoint.x < 0 || pos.y - grid.viewPoint.y < 0) visible = false;
		
		//Translate position back
		pos.x-= Rogue.floor(Grid.COLS_VISIBLE / 2);
		pos.y-= Rogue.floor(Grid.ROWS_VISIBLE / 2);
		
		//Return the result: if we are visible
		return visible;
	}
	
	/**
	 * Empties this GridCell, removes all objects
	 */
	public void empty() {
		objects.clear();
	}
	
	/**
	 * Called to update this GridCell, all its objects and do list maintenane
	 */
	public void update() {
		//First do listMaintenance
		listMaintenance();
		
		//Then update all objects
		for(GridObject o : objects) o.update();
	}
	
	/**
	 * Checks if we need to add or remove any objects
	 */
	private void listMaintenance() {
		if(toRemove.size() > 0 || toAdd.size() > 0) {
			if(toRemove.size() > 0) {
				for(GridObject o : toRemove) objects.remove(o);
				toRemove.clear();
			}
			if(toAdd.size() > 0) {
				for(GridObject o : toAdd) objects.add(o);
				toAdd.clear();
			}
		}
	}
	
	/**
	 * Animates all animated GridObjects
	 */
	public void animate() {
		for(GridObject o : objects) {
			if(o.animated) o.animate();
		}
	}
	
	/**
	 * Interact with all the objects in this GridCell
	 */
	public void interact() {
		for(GridObject o : objects) o.interact();
	}
	
	/**
	 * Renders all the GridObjects in this cell
	 */
	public void render(PGraphics g) {
		//First do list maintenance
		listMaintenance();
		//Then render
		g.tint(lighting);
		for(GridObject o : objects) o.render(g);
	}
	
	/**
	 * Lists all objects in this cell in the Debug window
	 */
	public void listObjects() {
		Debug.println("Objects in this cell:", this);
		for(GridObject o : objects) {
			Debug.println(" - " + o.texName, this);
		}
	}
	
	/**
	 * Adds one or more GridObjects to this cell
	 * @param objs
	 */
	public void add(GridObject... objs) {
		for(GridObject o : objs) {
			toAdd.add(o);
			o.parent = this;
		}
	}
	
	/**
	 * Removes one or more GridObjects from this cell
	 * @param objs
	 */
	public void remove(GridObject... objs) {
		for(GridObject o : objs) { toRemove.add(o);}
	}
	
	/**
	 * Parses the provided tile and adds it to this cell
	 * @param tile
	 */
	public void parseTile(String tile) {
		GridObject o = new GridObject(pos, this);
		o.parse(tile);
		add(o);
	}
	
}