package trb1914.rogue.state;

import processing.core.PConstants;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.MiniMap;
import trb1914.rogue.input.Key;

/**
 * The main Game Class
 * @author trb1914
 */
public class Game extends GameState {

	/** The Grid we're playing on */
	private Grid grid;
	/** The minimap*/
	public MiniMap miniMap;

	/**
	 * Creates a new Game
	 */
	public Game(){
		grid = new Grid(51, 51);
		miniMap = new MiniMap(grid);

		addRender(grid, miniMap);
		addUpdate(grid);
		addMouse(grid);
	}
	
	/**
	 * Updates all elements in the game. Also houses focusActor control
	 */
	public void update(){
		//First update all other elements
		super.update();
		//Then give control to focusActor
		if(Key.isDownOnce(PConstants.ENTER)) grid.load();
		if(Key.isDownOnce(PConstants.UP)) grid.focusActor.up();
		if(Key.isDownOnce(PConstants.DOWN)) grid.focusActor.down();
		if(Key.isDownOnce(PConstants.LEFT)) grid.focusActor.left();
		if(Key.isDownOnce(PConstants.RIGHT)) grid.focusActor.right();
	}
}
