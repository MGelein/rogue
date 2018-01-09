package trb1914.rogue.astar;

import java.util.ArrayList;

import trb1914.rogue.math.Int2D;

/**
 * Allows you to statically request a pathfinding solution from one location 
 * to the next. It uses the grid
 * @author trb1914
 */
public class Path {
	
	/** The list of nodes that makes up a path*/
	private ArrayList<PNode> nodes = new ArrayList<PNode>();
	/** The start of the generated path*/
	public Int2D start;
	/** The end of the generated path*/
	public Int2D end;

	/**
	 * Requests a path to be returned from the Int2D start location
	 * to the Int2D end location
	 * @param start
	 * @param end
	 */
	public static Path get(Int2D start, Int2D end) {
		//Initialze the open and closed list
		ArrayList<PNode> open = new ArrayList<PNode>();
		ArrayList<PNode> closed = new ArrayList<PNode>();
		PNode startNode = new PNode(start);
		PNode endNode = null;
		
		//While the open list is not empty
		while(open.size() > 0) {
			//Get the lowest node from the list
			PNode q = lowestCost(open);
			//Remove from open list
			open.remove(q);
			//Generate the successors
			ArrayList<PNode> successors = q.getSuccessors();
			//For each successor
			for(PNode succ : successors) {
				if(succ.pos.equals(end)) {// If this is the goal, we're done, this is the head of the path
					break;
				}
				//Set the h to the distance to the end point
				succ.h = end.dist(succ.pos);
				succ.f = succ.g + succ.h;
				
				//If a node with the same position is already in the open list but has a lower f
				boolean found = false;
				for(PNode n : open) {
					if(n.f < succ.f) {//If it has a lower cost
						if(n.pos.equals(succ.pos)) {//But is the same position
							found = true;
							endNode = succ;
							break;//Dont bother adding it to the list
						}
					}
				}
				if(found) continue;
				
				//If a node with the same position is already in the closed list has a lower f
				found = false;
				for(PNode n : closed) {
					if(n.f < succ.f) {//If it has a lower cost
						if(n.pos.equals(succ.pos)) {//But is the same position
							found = true;
							break;//Dont bother adding it to the list
						}
					}
				}
				if(found) continue;
				
				//If we make it to here, this is a good node to add to the open list
				open.add(succ);
			}
			//We're done with q node
			closed.add(q);
			
		}
		//Create a path from the endNode back to the startNode
		return new Path(endNode);
	}
	
	/**
	 * Returns the PNode with the lowest 'f' out of all the nodes in the list
	 * @param list
	 * @return
	 */
	private static PNode lowestCost(ArrayList<PNode> list) {
		PNode lowest = null;
		for(PNode n : list) {
			if(lowest == null || n.f < lowest.f) {
				lowest = n;
			}
		}
		return lowest;
	}
	
	
	/**
	 * Recreates a path from the provided endNode
	 * @param endNode
	 */
	private Path(PNode endNode) {
		end = endNode.pos;
		while(endNode != null) {
			nodes.add(endNode);
			endNode = endNode.parent;
		}
		start = nodes.get(nodes.size() - 1).pos;
	}
	
	/**
	 * Returns the next Tile we're going to
	 * @return
	 */
	public Int2D next() {
		//Get the last element in the list (closest to the original source)
		PNode node = nodes.get(nodes.size() - 1);
		//Remove this node from the list
		nodes.remove(node);
		//Return this node's position
		return node.pos;
	}
}
