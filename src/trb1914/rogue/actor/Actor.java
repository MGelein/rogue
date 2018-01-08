package trb1914.rogue.actor;

import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;
import trb1914.rogue.math.Int2D;

/**
 * A gridObject that can move, act etc. Basis for all monsters
 * @author trb1914
 */
public class Actor extends GridObject{

	/**
	 * Creates a new Actor at the specified position
	 * @param pos
	 * @param parent
	 */
	public Actor(Int2D pos, GridCell parent) {
		super(pos, parent);
		opaque = false;
	}

	/**
	 * Every actor has an act method. Mihgt be useless
	 */
	public void act() {};

	/**
	 * Every actor can move to locations
	 * @param x
	 * @param y
	 */
	public void moveTo(int x, int y){
		//First check if it is possible to moveTo
		if(!parent.grid.get(x, y).isWalkable()) {
			//If not walkable, try to interact with it
			parent.grid.get(x, y).interact();
			return;
		}

		//Remove from previous parent
		parent.remove(this);
		pos.x = x;
		pos.y = y;
		//Get new parent and re-add
		parent = parent.grid.get(pos);
		parent.add(this);
	}
	/**
	 * Every actor can move to locations
	 * @param pos
	 */
	public void moveTo(Int2D pos) {
		moveTo(pos.x, pos.y);
	}
	
	/**
	 * Moves this actor to the left
	 */
	public void left(){moveTo(pos.x - 1, pos.y);}
	
	/**
	 * Moves this actor to the right
	 */
	public void right(){moveTo(pos.x + 1, pos.y);}
	
	/**
	 * Moves this actor upwards
	 */
	public void up(){moveTo(pos.x, pos.y - 1);}
	
	/**
	 * Moves this actor downwards
	 */
	public void down(){moveTo(pos.x, pos.y + 1);}
}
