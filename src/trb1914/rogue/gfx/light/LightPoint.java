package trb1914.rogue.gfx.light;

import java.util.ArrayList;

import trb1914.debug.Debug;
import trb1914.rogue.math.Int2D;

/**
 * Lightweight class that is part of light. This keeps track of the spreading
 * of light from the source
 * @author trb1914
 */
public class LightPoint {
	/** The position of this lightpoint*/
	protected Int2D pos;
	/** Amount of str (range) left*/
	protected int str;
	/** Reference to the parent light*/
	private Light parent;
	/** Has this light already spread*/
	protected boolean propagated;
	
	/**
	 * Creates a new LightPoint that spreads forward
	 * @param parent
	 * @param pos
	 * @param strength
	 */
	protected LightPoint(Light parent, Int2D pos, int strength) {
		this.pos = pos.copy();
		this.str = strength;
		this.parent = parent;
	}
	
	/**
	 * Returns the relative strength [0-1] of this lightpoint versus the source
	 * @return
	 */
	public float getStrength() {
		return (str + 0.000001f) / (parent.str + 0.000001f);
	}
	
	/**
	 * Returns a list of neighbors that the light can be propagated to
	 * @return
	 */
	protected ArrayList<LightPoint> getViableNeighbors(){
		propagated = true;
		//Init list of possible neighbors
		ArrayList<LightPoint> pts = new ArrayList<LightPoint>();
		
		//Don't propagate if i'm an opaque tile and not source of the light, return empty list
	    if(parent.grid.get(pos.x, pos.y).isOpaque() && parent.str != str || str == 1) return pts;
	    
	    //Only allow upward light when we are not opaque
	    if(!parent.grid.get(pos.x, pos.y).isOpaque()){
	      pts.add(new LightPoint(parent, new Int2D(pos.x, pos.y - 1), str - 1));
	    }
	    
	    //Left right
	    pts.add(new LightPoint(parent, new Int2D(pos.x - 1, pos.y), str - 1));
	    pts.add(new LightPoint(parent, new Int2D(pos.x + 1, pos.y), str - 1));
	    
	    //Only allow downward light when it is not opaque
	    if(!parent.grid.get(pos.x, pos.y + 1).isOpaque()){
	      pts.add(new LightPoint(parent, new Int2D(pos.x, pos.y + 1), str - 1));
	    }
	    
	    //Return the populated list
	    return pts;
	}
}
