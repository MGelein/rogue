package trb1914.rogue.decor;

import trb1914.rogue.Rogue;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;

/**
 * Just a pile of bones
 * @author trb1914
 */
public class Bones extends GridObject{
	/** All the bone types this bone pile can be*/
	private static String[] boneTypes = new String[]{
			"decor.skull_white", "decor.skull_white_long",
			"decor.bones_white", "decor.bones_white_long"};

	/**
	 * Creates a new pile of bones at the provided location
	 * @param parent
	 */
	public Bones(GridCell parent){
		super(parent.pos, parent);
		parse(Rogue.app.random(boneTypes));
		opaque = walkable = false;
	}

	/**
	 * Interacting with the pile of bones turns it to dust
	 */
	public void interact(){
		walkable = true;
		opaque = false;
		texName = null;
		tex = null;
		parent.remove(this);
	}
}