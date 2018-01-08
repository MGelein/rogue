package trb1914.rogue.gen;

import java.util.ArrayList;

import trb1914.debug.Debug;
import trb1914.rogue.Rogue;
import trb1914.rogue.math.Int2D;

/**
 * A room in the dungeon generator. Takes care of exits and
 * tile conversion
 * @author trb1914
 */
public class Room {
	/** The maximum size a room can be wide or high*/
	public static int MAX_SIZE = 11;
	/** The minimum size a room can be wide or high*/
	public static int MIN_SIZE = 5;
	
	/** All the locations on the grid that are part of this room*/
	public ArrayList<Int2D> tiles = new ArrayList<Int2D>();
	/** Dimensions of this room*/
	public Int2D dim;
	
	/**
	 * Creates a new Room with the specified dimension
	 * @param dim
	 */
	protected Room(Int2D dim) {
		this.dim = dim.copy();
	}
	
	/**
	 * Starts to punch at least one hole to the nearest path, but has a rnadom
	 * chance to dig more
	 */
	public void punchHoles() {
		//At least punch one hole
		punchHole();
		
		//Now try to create more holes
		while(Rogue.oneIn(DunGen.deadRoomChance)) { punchHole();}
		
		//Finally set the room to be floor tiles now that we're done
		for(Int2D tile : tiles) DunGen.setCell(tile, DunGen.FLOOR);
	}
	
	/**
	 * Actaully does the punching of the hole to the corridors
	 */
	private void punchHole() {
		//Grab a random odd position in the room and keep trying till its odd
		Int2D pos = Rogue.app.random(tiles);
		while(!pos.isOdd()) { pos = Rogue.app.random(tiles);}
		
		//Pick a random direction
		Int2D dir = new Int2D().rndDir();
		
		//Punch through untill we reach FLOOR
		do {
			//Only change type of Cell if we're carving through the wall
			if(DunGen.isWall(DunGen.getCell(pos))) DunGen.setCell(pos, DunGen.FLOOR);
			//Continue in that direction
			pos.add(dir);
			
			//If we reach the edge, discard attempt, try again
			if(pos.x <= 1 || pos.x >= DunGen.cols - 1|| pos.y <= 1 || pos.y >= DunGen.rows - 1) {
				punchHole();
				return;//Try again, discard this attempt
			}
			
		}while(!DunGen.isFloor(DunGen.getCell(pos)));
	}
}
