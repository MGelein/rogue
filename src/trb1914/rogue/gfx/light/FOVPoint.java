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

	/** The location of this Int2D*/
	protected Int2D pos;
	
	/** The strength of vision left*/
	protected float str;
	
	/**
	 * Creates a new FOV point using the provided strength and position
	 */
	public FOVPoint(Int2D pos, float str) {
		pos = pos.copy();
		this.str = str;
	}
	
	/**
	 * Exadns this FOV point to all sides (even without LOS)
	 * @return
	 */
	public ArrayList<FOVPoint> expand(){
		//The list of new points that are visible
		ArrayList<FOVPoint> pts = new ArrayList<FOVPoint>();
		
		//Don't expand if this is a opaque tile, return empty list
		if(Grid.current.get(pos).isOpaque()) return pts;
		
		//The light value of this cell, must be between 0 and 1
		float light = Rogue.constrain(Grid.current.get(pos).lightness, 0, 1);
		float newStr = str - (1 - light);
		//If the strength is lower than 0, return
		if(newStr < 0) return pts;
		
		//Only allow upward expansion if we're not opaque
		if(!Grid.current.get(pos).isOpaque()) {
			pts.add(new FOVPoint(pos.copy().add(Int2D.UP), newStr));
		}
		
		//Left and right
		pts.add(new FOVPoint(pos.copy().add(Int2D.LEFT), newStr));
		pts.add(new FOVPoint(pos.copy().add(Int2D.RIGHT), newStr));
		
		//Only allow downward vision if the tile below is not opaque
		if(!Grid.current.get(pos.copy().add(Int2D.DOWN)).isOpaque()) {
			pts.add(new FOVPoint(pos.copy().add(Int2D.DOWN), newStr));
		}
		
		//Return the list of FOV points
		return pts;
	}
	
}
