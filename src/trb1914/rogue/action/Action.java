package trb1914.rogue.action;

import trb1914.rogue.actor.Actor;

/**
 * Every action and actor can take is based on this
 * @author trb1914
 */
public abstract class Action {

	/** The energy required for this action*/
	public float energy;
	/** The actor that performs this action*/
	public Actor actor;
	
	
	/**
	 * Creates a new Action that has the required energy threshold
	 * @param energy
	 */
	protected Action(Actor actor, float energy) {
		this.actor = actor;
		this.energy = energy;
	}
	
	/**
	 * Returns another action to try, this allows action chaining.
	 * In this method, the energy is deducted from the actor
	 * @return
	 */
	public Action perform() {
		actor.energy -= energy;
		return null;
	}
}
