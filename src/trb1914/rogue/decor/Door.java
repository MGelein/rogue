package trb1914.rogue.decor;

import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;
/**
 * Holds any kind of door
 * @author trb1914
 */
public class Door extends GridObject {
	/** If this door is currently opened*/
	protected boolean open = false;
	/** If this door is currently locked*/
	protected boolean locked = false;
	
	/**
	 * Creates a new door at the provided position with the provided texture
	 * @param parent
	 * @param s
	 */
	public Door(GridCell parent, String s){
		super(parent.pos.x, parent.pos.y, parent);
		parse(s);
		if(s.indexOf("locked") != -1) locked = true;
		setTexture(texName, false);
		walkable = false; 
		opaque = true;
	}
	
	/**
	 * Interacting with the door opens and closes it
	 */
	public void interact(){
		if(locked) return;
		open = !open;
		animate();

		//Set cell walkable
		walkable = open;
		opaque = !open;

		//Also update all lighting now the door has opened
		parent.grid.lightingUpdate = true;
	}
}
