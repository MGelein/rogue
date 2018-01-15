package trb1914.rogue.gfx.light;

import java.util.ArrayList;

import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * Holds the FOV for an actor
 * @author trb1914
 */
public class FOV {
	
	/** The actual visible part of the FOV*/
	private static ArrayList<FOVPoint> pts = new ArrayList<FOVPoint>();
	
	/** Position of the actor this FOV was calculated for*/
	private static Int2D lastPos = new Int2D(-1, -1);
	
	/** Last know vision strenght of the focusActor*/
	protected static float vision;
	
	/** Them inimal coords (top left)*/
	private static Int2D min = new Int2D(1000000, 1000000);
	/** The maximum coords*/
	private static Int2D max = new Int2D();
	
	/** List of all expanded points*/
	private static ArrayList<FOVPoint> neighbors = new ArrayList<FOVPoint>();
	/** List of FOVpoints we're currently working with*/
	private static ArrayList<FOVPoint> open = new ArrayList<FOVPoint>();
	
	/**
	 * Checks the last caclulated FOV pos against current actor pos to see if we need
	 * refreshing
	 * @param p
	 */
	public static void refresh() {
		if(!lastPos.equals(Grid.current.focusActor.pos)) calculate();
	}
	
	/**
	 * Calculates the FOV of this actor
	 */
	public static void calculate() {
		vision = Grid.current.focusActor.vision;
		open.add(FOVPoint.create(Grid.current.focusActor.pos, Grid.current.focusActor.vision));
		lastPos = Grid.current.focusActor.pos.copy();
		for(FOVPoint p : pts) p.invalidate();
		pts.clear();
		
		min.set(1000000, 100000);
		max.set(0, 0);
		
		//Now expand untill we're done
		while(open.size() > 0) {
			//List of all generated neighbors
			neighbors.clear();
			//Generate neighbors from all open points
			for(int i = open.size() - 1; i >= 0; i--) {
				neighbors.addAll(open.get(i).expand());
				//This point has expanded, add it to the list
				Int2D pos = open.get(i).pos;
				if(pos.x < min.x) min.x = pos.x;
				if(pos.x > max.x) max.x = pos.x;
				if(pos.y < min.y) min.y = pos.y;
				if(pos.y > max.y) max.y = pos.y;
				pts.add(open.remove(i));
			}
			
			//Now make sure none of the neighbors overlap with open or pts 
			for(FOVPoint f : neighbors) {
				boolean found = false;
				for(FOVPoint p : pts) if(p.pos.equals(f.pos)){ f.invalidate(); found = true; break;}
				for(int i = 0; i < open.size(); i++) {
					FOVPoint p = open.get(i);
					if(p.equals(f)){
						if(p.str  < f.str) {
							open.set(i, f);
							found = true;
							break;
						}
					}
				}
				//If not found add it to the list of visible tiles
				if(!found) {
					open.add(f);
				}else {
					f.invalidate();
				}
			}
		}
	}
	
	/**
	 * Checks if the provided position is contained within this FOV and how visible it is
	 * @param pos
	 * @return
	 */
	public static float getVisibility(Int2D pos) {
		//First check if it is within the rough vicinity
		if(pos.x < min.x || pos.x > max.x || pos.y < min.y || pos.y > max.y) return 0;
		
		//Now check every match for a point
		for(FOVPoint p : pts) {
			if(p.pos.equals(pos)) return p.getStrength();
		}
		
		//If nothing found, return false
		return 0;
	}
}
