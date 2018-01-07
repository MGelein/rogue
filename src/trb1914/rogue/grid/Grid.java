package trb1914.rogue.grid;
/**
 * Holds the grid related methods. This is a collection of cells.
 * Each GridCell has a list of GridObjects
 * @author trb1914
 */
public class Grid {

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
}
