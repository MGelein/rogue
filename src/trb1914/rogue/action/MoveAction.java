package trb1914.rogue.action;

import trb1914.rogue.actor.Actor;
import trb1914.rogue.gfx.light.FOV;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * This action extends the basic move action
 * @author trb1914
 */
public class MoveAction extends Action {
	
	/**The energy cost of a movement */
	public static final float ENERGY_COST = 10;
	/** The direction we're moving in*/
	private Int2D dir;

	/**
	 * Creates a new momvemtn in the provided dir
	 * @param actor
	 * @param dir
	 */
	public MoveAction(Actor actor, Int2D dir) {
		super(actor, ENERGY_COST);
		this.dir = dir.copy();
	}
	
	/**
	 * Performs the movement action. If an unwalkeable tile is met,
	 * we try to interact with it
	 */
	public Action perform() {
		Int2D targetPos = actor.pos.copy().add(dir);
		//If the location we want to move to is not walkable
		if(!Grid.current.get(targetPos).isWalkable()) {
			//Try to interact with the location
			return new InteractAction(actor, dir);
		}
		
		//If we make it to here we can move
		//Remove the actor from its parent cell
		actor.parent.remove(actor);
		//Set its position accordingly
		actor.pos = targetPos.copy();
		//Add it to the new parent
		actor.parent = Grid.current.get(targetPos);
		//Now add it really
		actor.parent.add(actor);
		
		//Check if this was the focusActor, if so, update FOV
		if(actor == Grid.current.focusActor) {
			FOV.refresh(true);
		}
		
		//If the action was succesfull, return null (and deduct energy)
		return super.perform();
	}
}
