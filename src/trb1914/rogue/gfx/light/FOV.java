package trb1914.rogue.gfx.light;

import java.util.ArrayList;

import trb1914.rogue.actor.Actor;
import trb1914.rogue.math.Int2D;

/**
 * Holds the FOV for an actor
 * @author trb1914
 */
public class FOV {
	
	/** The actual visible part of the FOV*/
	private ArrayList<FOVPoint> pts = new ArrayList<FOVPoint>();
	
	/** A reference to the actor who's FOV this is*/
	private Actor actor;
	
	/** Them inimal coords (top left)*/
	private Int2D min;
	/** The maximum coords*/
	private Int2D max;
	
	/**
	 * Calculates the FOV of this actor
	 */
	public void calculate() {
		ArrayList<FOVPoint> open = new ArrayList<FOVPoint>();
		open.add(new FOVPoint(actor.pos, actor.vision));
		pts.clear();
		
		//Now expand untill we're done
		while(open.size() > 0) {
			//List of all generated neighbors
			ArrayList<FOVPoint> neighbors = new ArrayList<FOVPoint>();
			//Generate neighbros from all open points
			for(int i = open.size(); i >= 0; i--) {
				neighbors.addAll(open.get(i).expand());
				//This point has expanded, add it to the list
				Int2D pos = open.get(i).pos;
				if(pos.x < min.x) min.x = pos.x;
				if(pos.x > max.x) max.x = pos.x;
				if(pos.y < min.y) min.y = pos.y;
				if(pos.y > max.y) max.y = pos.y; 
				pts.add(open.remove(i));
			}
		}
	}
}
