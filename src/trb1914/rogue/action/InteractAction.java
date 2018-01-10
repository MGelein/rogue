package trb1914.rogue.action;

import trb1914.rogue.actor.Actor;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * This type of interaction includes opening doors, lighting and extinguishing
 * torches etc.
 * @author trb1914
 */
public class InteractAction extends Action{

	/**The energy cost of an interact*/
	public static final float ENERGY_COST = 8;
	/** The direction we're moving in*/
	private Int2D dir;
	
	/**
	 * A new interaction action
	 * @param actor
	 * @param dir
	 */
	public InteractAction(Actor actor, Int2D dir) {
		super(actor, ENERGY_COST);
		this.dir = dir.copy();
	}
	
	/**
	 * Tries to perform an interact action
	 */
	public Action perform() {
		//Try to interact with the provided cell
		Int2D targetPos = actor.pos.copy().add(dir);
		Grid.current.get(targetPos).interact(actor);
		
		//And deduct energy
		return super.perform();
	}

}
