package trb1914.rogue.state;

import processing.core.PConstants;
import processing.core.PGraphics;
import trb1914.debug.Debug;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.input.Key;

/**
 * The main Game Class
 * @author trb1914
 */
public class Game extends GameState {

	/** The Grid we're playing on */
	private Grid grid;

	/**
	 * Creates a new Game
	 */
	public Game(){
		grid = new Grid(51, 51);

		addRender(grid);
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
