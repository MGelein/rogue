package trb1914.rogue.ai;

import trb1914.rogue.actor.Actor;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.math.Int2D;

/**
 * This actor is controlled by the AI
 * @author trb1914
 */
public class AIActor extends Actor{

	/**
	 * Creates a new AI controlled actor at the provided
	 * location
	 * @param parent
	 */
	public AIActor(GridCell parent) {
		super(parent);
		parse("undead.mummy");
	}
	
	/**
	 * In the update we also  check if we have selected a movement yet
	 */
	public void update() {
		//First do super update
		super.update();
		
		//Now check if we have a movement yet
		if(nextAction == null && Grid.current.focusActor != this) {
			move(new Int2D().rndDir());
		}
	}
}
