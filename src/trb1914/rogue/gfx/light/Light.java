package trb1914.rogue.gfx.light;

import java.util.ArrayList;

import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.io.Registry;
import trb1914.rogue.math.Int2D;
/**
 * Every light is an instance of this. Also provides static access to LightTemplates
 * @author trb1914
 */
public class Light {
	/** The color of the ligth*/
	public int c;
	/** The strength / range of the light*/
	public int str;
	/** The position the light is at*/
	private Int2D pos;
	/** A reference to the grid that holds us*/
	protected Grid grid;
	/** List of the spread of light*/
	private ArrayList<LightPoint> points;

	/**
	 * Creates a new light using the provided parameters
	 * @param pos
	 * @param col
	 * @param r
	 * @param g
	 */
	public Light(Int2D pos, int col, int r, Grid g) {
		c = col;
		str = r;
		grid = g;
		setPos(pos);
	}
	/**
	 * Creates a new light using the provided parameters
	 * @param pos
	 * @param template
	 * @param g
	 */
	public Light(Int2D pos, LightTemplate template, Grid g) {
		this(pos, template.c, template.str, g);
	}

	/**
	 * Repoistions this light
	 * @param p
	 */
	public void setPos(Int2D p) {
		pos = p.copy();
		calculatePoints();
	}

	/**
	 * Affects the lighting of the cells
	 */
	public void calculate(){
		//For each of the lightpoints
		for(LightPoint l : points){
			int origColor = grid.get(l.pos.x, l.pos.y).lighting;
			int targetColor = c;
			origColor = Rogue.app.lerpColor(origColor, targetColor, l.getStrength() / 2);
			grid.get(l.pos).lighting = origColor;
			//Add the strength to the lighting strength
			grid.get(l.pos).lightness += l.getStrength();
		}
	}

	/**
	 * Called to refresh the spread of light (for example when
	 * a door opens)
	 */
	public void refresh(){
		calculatePoints();
	}

	/**
	 * Calculates the spread of light on the grid
	 */
	private void calculatePoints() {
		ArrayList<LightPoint> open = new ArrayList<LightPoint>();
		open.add(new LightPoint(this, pos, str));
		points = new ArrayList<LightPoint>();

		while(open.size() > 0){
			ArrayList<LightPoint> neighbors = new ArrayList<LightPoint>();
			for(int i = open.size() - 1; i >= 0; i--){
				neighbors.addAll(open.get(i).getViableNeighbors());
				//Add it to the finished points list;
				points.add(open.remove(i));
			}
			//Now make sure none of the neighbors overlap with open or points
			for(LightPoint n : neighbors){
				boolean found = false;
				for(LightPoint l : points){
					if(l.pos.x == n.pos.x && l.pos.y == n.pos.y){
						found = true;
						break;
					}
				}
				for(int i = 0; i < open.size(); i++){
					LightPoint l = open.get(i);
					if(l.pos.x == n.pos.x && l.pos.y == n.pos.y){
						found = true;
						if(l.str < n.str){//better light value, replace the old one
							open.set(i, n);
							found = true;
						}
					}
				}
				if(! found){
					open.add(n);
				}
			}
		}
	}


	/**
	 * Returns the parsed lightTemplate for the provided lightType
	 * @param s
	 * @return
	 */
	public static LightTemplate get(String s) {
		return new LightTemplate(Registry.get("light." + s));
	}
}
