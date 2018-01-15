package trb1914.rogue.gfx.light;

import java.util.ArrayList;

import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.math.Int2D;

/**
 * Small class that is a small point in someones FOV,
 * it contains coordinates and a vision strength left
 * @author trb1914
 */
public class FOVPoint {
	
	/** Cost of vision to see one tile in complete darkness*/
	public static int TILE_COST = 2;
	
	/**
	 * Loads a new FOVPoint, uses pooling
	 * @param pos
	 * @param str
	 * @return
	 */
	public static FOVPoint create(Int2D pos, float str) {
		if(pool.size() >= 0) {
			return new FOVPoint(pos, str);
		}else {
			return pool.remove(pool.size() - 1).set(pos, str);
		}
	}
	
	/** List of FOV points that have been invalidated and can be reused*/
	private static ArrayList<FOVPoint> pool = new ArrayList<FOVPoint>();

	/** The location of this Int2D*/
	protected Int2D pos;
	
	/** The strength of vision left*/
	protected float str;
	
	/***List of viable neighbors*/
	private ArrayList<FOVPoint> pts = new ArrayList<FOVPoint>();
	
	/**
	 * Creates a new FOV point using the provided strength and position
	 */
	public FOVPoint(Int2D pos, float str) {
		set(pos, str);
	}
	
	/**
	 * Sets this FOVPoint to the provided state
	 * @param pos
	 * @param str
	 * @return
	 */
	public FOVPoint set(Int2D pos, float str) {
		this.pos = pos.copy();
		this.str = str;
		return this;
	}
	
	/**
	 * Returns [0-1] how strong the vision is here
	 * @return
	 */
	public float getStrength() {
		return str / FOV.vision;
	}
	
	/**
	 * Exadns this FOV point to all sides (even without LOS)
	 * @return
	 */
	public ArrayList<FOVPoint> expand(){
		//The list of new points that are visible
		pts.clear();
		
		//Don't expand if this is a opaque tile, return empty list
		if(Grid.current.get(pos).isOpaque()) return pts;
		
		//The light value of this cell, must be between 0 and 1
		float light = Rogue.constrain(Grid.current.get(pos).lightness, 0, 1);
		float newStr = str - (TILE_COST - light);
		//If the strength is lower than 0, return
		if(newStr < 0) return pts;
		
		//Only allow upward expansion if we're not opaque
		if(!Grid.current.get(pos).isOpaque()) {
			pts.add(FOVPoint.create(pos.copy().add(Int2D.UP), newStr));
		}
		
		//Left and right, and down
		pts.add(FOVPoint.create(pos.copy().add(Int2D.LEFT), newStr));
		pts.add(FOVPoint.create(pos.copy().add(Int2D.RIGHT), newStr));
		pts.add(FOVPoint.create(pos.copy().add(Int2D.DOWN), newStr));
		
		//Return the list of FOV points
		return pts;
	}
	
	/**
	 * Invalidates this FOVPoint and adds it to the pool
	 */
	protected void invalidate() {
		pool.add(this);
	}
	
}
