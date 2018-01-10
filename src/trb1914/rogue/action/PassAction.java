package trb1914.rogue.action;

import trb1914.rogue.actor.Actor;

/**
 * Action that only passes your turn
 * @author trb1914
 */
public class PassAction extends Action{

	public static final float ENERGY_COST = 5;
	
	/**
	 * Will just pass your turn
	 * @param actor
	 */
	public PassAction(Actor actor) {
		super(actor, ENERGY_COST);
	}
}
