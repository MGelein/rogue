package trb1914.rogue.astar;

import java.util.ArrayList;

import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * Simple AStar Node class
 * @author trb1914
 */
public class PNode {

	/** g is the cost it took to get to this node*/
	public float g = 0;
	/** heuristic guess of distance to target*/
	public float h = 0;
	/** g + h*/
	public float f = 0;
	/** The previous PNode, if null this means this is the startNode*/
	public PNode parent;
	/** The location of this node*/
	public Int2D pos;
	
	/**
	 * Creates a new PNode using the provided parameters. F will be calculated
	 * @param pos
	 * @param g
	 * @param h
	 */
	public PNode(PNode parent, Int2D pos) {
		this.parent = parent;
		this.pos = pos.copy();
		g = parent.g + 1;//add the distance between me and parent to the g
	}
	
	/**
	 * Creates the starting node at the specified position
	 * @param pos
	 */
	public PNode(Int2D pos) {
		//Don't seet parent, this is the starting node
		this.pos = pos.copy();
		g = 0;//No cost yet
	}
	
	/**
	 * Returns the 4 successors, less if we go off the grid
	 * @return
	 */
	public ArrayList<PNode> getSuccessors(){
		//List of possible successors
		ArrayList<PNode> nodes = new ArrayList<PNode>();
		//If left is possible
		if(Grid.current.get(pos.left()).isWalkable()) {
			nodes.add(new PNode(this, pos.left()));
		}
		//If right is possible
		if(Grid.current.get(pos.right()).isWalkable()) {
			nodes.add(new PNode(this, pos.right()));
		}
		//If up is possible
		if(Grid.current.get(pos.up()).isWalkable()) {
			nodes.add(new PNode(this, pos.up()));
		}
		//If down is possible
		if(Grid.current.get(pos.down()).isWalkable()) {
			nodes.add(new PNode(this, pos.down()));
		}		
		//Return it
		return nodes;
	}
}
