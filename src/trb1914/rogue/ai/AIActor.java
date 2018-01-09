package trb1914.rogue.ai;

import trb1914.rogue.actor.Actor;
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
	 * Every time we animate, make a movement
	 */
	public void animate() {
		super.animate();
		Int2D vel = new Int2D().rndDir();
		Int2D dest = pos.copy().add(vel);
		moveTo(dest);
	}
}
