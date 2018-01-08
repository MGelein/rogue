package trb1914.rogue.actor;

import trb1914.rogue.grid.GridCell;
import trb1914.rogue.math.Int2D;

/**
 * The main player class. This is what the player controls. Suprising...
 * @author trb1914
 */
public class Player extends Actor{
	/**
	 * Creates a new player instance ath the provided location
	 * with the provided cell as parent
	 * @param pos
	 * @param parent
	 */
	public Player(GridCell parent){
		super(parent);
		parse("player.mage");
		moveTo(pos);
		parent.grid.focusActor = this;
	}

	/**
	 * Moves the player to the provided position
	 */
	public void moveTo(int newX, int newY){
		super.moveTo(newX, newY);
		//Now center cam on new position
		parent.grid.viewPoint.set(pos.x, pos.y);
	}
	
	/**
	 * Moves the player to the provided position
	 */
	public void moveTo(Int2D pos) {
		moveTo(pos.x, pos.y);
	}
}
