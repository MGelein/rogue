package trb1914.rogue.actor;

import trb1914.rogue.action.Action;
import trb1914.rogue.action.MoveAction;
import trb1914.rogue.ai.Faction;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;
import trb1914.rogue.math.Int2D;

/**
 * A gridObject that can move, act etc. Basis for all monsters
 * @author trb1914
 */
public class Actor extends GridObject{
	
	/** Next actor in the linked list */
	protected Actor next = null;
	/** Previous actor in the linked list*/
	protected Actor prev = null;

	/** By default actors are not part of a faction*/
	public Faction faction = Faction.NONE; 
	
	/** The energy level of this actor. If energy reaches threshold, actions can be taken*/
	public float energy = 0;
	
	/** The amount of energy received every time we're reached*/
	public float speed = 10;
	
	/** The next action we're going to take. Null if we haven't selected an action yet*/
	protected Action nextAction = null;
	
	/**
	 * Creates a new Actor at the specified position
	 * @param parent
	 */
	public Actor(GridCell parent) {
		super(parent.pos, parent);
		Director.addActor(this);
		opaque = false;
	}
	
	/**
	 * Sets the next action of the actor to be to move in the provided direction
	 * @param dir
	 */
	public void move(Int2D dir) {
		nextAction = new MoveAction(this, dir);
	}
	
	/**
	 * Temporary override of the interact mechanism to allow possession
	 */
	public void interact(Actor actor) {
		if(actor == Grid.current.focusActor) {
			Grid.current.focusActor = this;
		}
	}
	
	/** Moves the actor left*/
	public void left() {move(Int2D.LEFT);}
	
	/** Moves the actor right*/
	public void right() {move(Int2D.RIGHT);}
	
	/**Moves the actor up */
	public void up() {move(Int2D.UP);}
	
	/** Moves the actor down */
	public void down() {move(Int2D.DOWN);}
}
