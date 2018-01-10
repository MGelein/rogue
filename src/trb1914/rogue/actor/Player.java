package trb1914.rogue.actor;

import trb1914.rogue.action.PassAction;
import trb1914.rogue.ai.Faction;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;

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
		parse("player.rogue");
		parent.grid.focusActor = this;
		//Player is, naturally, part of player faction
		faction = Faction.get("player");
	}
	
	/**
	 * Basic AI in case we're not the focus actor
	 */
	public void update() {
		if(Grid.current.focusActor != this && nextAction == null) {
			nextAction = new PassAction(this);
		}
		super.update();
	}
}
