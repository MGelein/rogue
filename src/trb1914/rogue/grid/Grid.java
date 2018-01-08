package trb1914.rogue.grid;

import java.util.ArrayList;

import processing.core.PGraphics;
import trb1914.rogue.Rogue;
import trb1914.rogue.actor.Actor;
import trb1914.rogue.gen.DunGen;
import trb1914.rogue.gfx.light.Light;
import trb1914.rogue.interfaces.IUpdate;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.MouseAble;

/**
 * Holds the grid related methods. This is a collection of cells.
 * Each GridCell has a list of GridObjects
 * @author trb1914
 */
public class Grid extends MouseAble implements IUpdate{

	/** The extra scaling to make the grid better visible*/
	public static final int SCL = 2;
	/** The amount of pixels all the tiles are wide and high*/
	public static final int SIZE = 16;
	/** The calculated size of a grid tile in the normal stage*/
	public static final int GRID_SIZE = SCL * SIZE;
	/** The amount of cols that could  theoretically fit on the screen*/
	public static int COLS_VISIBLE = 0;
	/** The amount of rows that could  theoretically fit on the screen*/
	public static int ROWS_VISIBLE = 0;

	/** List of all light sources in this grid*/
	private ArrayList<Light> lights = new ArrayList<Light>();
	/** List of all the GridCells in this grid*/
	private GridCell[] cells;
	/** The amount of columns in this grid*/
	protected int cols;
	/** The amount of rows in this grid*/
	protected int rows;

	/** How many frames have passed since last animation update*/
	private int animCounter = 0;
	/** How many frames between animation update*/
	private int animRate = 60;
	/** The ambient lighting (darkness) color*/
	private int ambientLight = Rogue.app.color(0, 0, 60);
	/** The location the camera focusses on */
	public Int2D viewPoint = new Int2D();
	/** If we need a lighting update to happen ASAP*/
	public boolean lightingUpdate = false;
	/** This actor is followed by the camera*/
	public Actor focusActor;

	/**
	 * Creates a new Grid of the provided size
	 * @param maxCols
	 * @param maxRows
	 */
	public Grid(int maxCols, int maxRows) {
		rows = maxRows;
		cols = maxCols;

		//Initialize the grid
		cells = new GridCell[cols * rows];
		for(int x = 0; x < cols; x++) {
			for(int y = 0; y < rows; y++) {
				cells[x + y * cols] = new GridCell(x, y, this);
			}
		}
		
		//After creation automatically load first dungeon
		load();
	}

	/**
	 * Renders all the cells in the grid if they are visible
	 * @param g
	 */
	public void render(PGraphics g){
		//Translate matrix for map viewing.
		g.pushMatrix();
		g.translate((-viewPoint.x + (COLS_VISIBLE / 2)) * GRID_SIZE,
				(-viewPoint.y + (ROWS_VISIBLE / 2)) * GRID_SIZE);

		for(GridCell c : cells) {
			if(c.isVisible()) c.render(g);
		}
		//Reset tint
		g.tint(255);
		g.popMatrix();
	}

	/**
	 * Updates all the cells in the Grid
	 */
	public void update(){
		for(GridCell c : cells) c.update();

		//Also forward animation
		animCounter++;
		if(animCounter > animRate){
			animCounter -= animRate;
			for(GridCell c: cells) c.animate();
		}

		//Also check if lighting needs updating
		if(lightingUpdate) calcLighting();
	}

	/**
	 * What happens when you click the grid. Temporarily for debug you 
	 * interact with any cell
	 * @param x
	 * @param y
	 */
	public void mouseDown(int x, int y){
		Int2D clickPos = Grid.screenToGrid(x, y);
		clickPos.sub(Rogue.floor(COLS_VISIBLE / 2), Rogue.floor(ROWS_VISIBLE / 2));
		clickPos.add(viewPoint);
		get(clickPos).interact();
		//get(clickPos).listObjects();
	}

	/**
	 * Loads a new randomly generated level into the grid
	 */
	public void load(){
		lights = new ArrayList<Light>();
		String[] dungeon = DunGen.generateDungeon(cols, rows);
		for(int i = 0; i < dungeon.length; i++){
			get(i).empty();
			get(i).parseTile(dungeon[i]);
		}

		//Now generate decoration for this dungeon
		ArrayList<GridObject> decoration = DunGen.generateDecoration(this);
		for(GridObject o : decoration){
			get(o.pos).add(o);
		}
		
		//Do the lighting update
		calcLighting();
	}

	/**
	 * Resets and recalculates all lighting
	 */
	private void calcLighting() {
		//Reset all cells to ambient
		for(GridCell c : cells) c.lighting = ambientLight;

		//Recalculate all lighting
		for(Light l : lights) {l.refresh(); l.calculate();};

		//Once the lighting has been calculated, reset trigger
		lightingUpdate = false;
	}

	/**
	 * Adds a light to the list of ligths and requests an update
	 * @param l
	 */
	public void addLight(Light l) {
		lights.add(l);
		lightingUpdate = true;
	}

	/**
	 * Removes a lightsource from the list of lights and requests
	 * an update to the lighting
	 * @param l
	 */
	public void removeLight(Light l) {
		lights.remove(l);
		lightingUpdate = true;
	}



	/**
	 * Returns the GridCell at the provided index
	 * @param index
	 * @return
	 */
	public GridCell get(int index) {
		return cells[Rogue.constrain(index, 0, cells.length - 1)];
	}

	/**
	 * Returns the GridCell at the provided location
	 * @param x
	 * @param y
	 * @return
	 */
	public GridCell get(int x, int y) {
		return get(x + y * cols);
	}

	/**
	 * Returns the GridCell ath the provided location
	 * @param pos
	 * @return
	 */
	public GridCell get(Int2D pos) {
		return get(pos.x, pos.y);
	}

	/**
	 * Translates the provided screen coordinates to a gridCell coordinate
	 * @param x
	 * @param y
	 * @return
	 */
	public static Int2D screenToGrid(int x, int y) {
		return new Int2D(Rogue.floor(x / GRID_SIZE), Rogue.floor(y / GRID_SIZE));
	}
}
