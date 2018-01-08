package trb1914.rogue.gen;

import java.util.ArrayList;

import trb1914.rogue.Rogue;
import trb1914.rogue.actor.Player;
import trb1914.rogue.decor.Bones;
import trb1914.rogue.decor.Door;
import trb1914.rogue.decor.LightSource;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;
import trb1914.rogue.math.Int2D;

/**
 * The Dungeon generator, that generates both decoration and the tile layer
 * for the grid to load
 * @author trb1914
 */
public abstract class DunGen {
	//Basic Tile types for generation
	public static final String WALL = "wall";
	public static final String FLOOR = "floor";
	public static final String ROOM = "room";
	public static final String VOID = "void";
	public static final String LIQUID = "pit";
	public static final String CONN = "conn";

	//Door types
	public static final String DOOR_H = "door.iron_h";
	public static final String DOOR_V = "door.iron_v";

	//Wall types for conversion
	public static final String WALL_FLAT = "wall.brick_flat";
	public static final String WALL_T_TOP = "wall.brick_tjunction_top";
	public static final String WALL_T_BOTTOM = "wall.brick_tjunction_bottom";
	public static final String WALL_T_LEFT = "wall.brick_tjunction_left";
	public static final String WALL_T_RIGHT = "wall.brick_tjunction_right";
	public static final String WALL_X = "wall.brick_xjunction";
	public static final String WALL_STRAIGHT_V = "wall.brick_straight_v";
	public static final String WALL_STRAIGHT_H = "wall.brick_straight_h";
	public static final String WALL_CORNER_TL = "wall.brick_corner_tl";
	public static final String WALL_CORNER_BL = "wall.brick_corner_bl";
	public static final String WALL_CORNER_TR = "wall.brick_corner_tr";
	public static final String WALL_CORNER_BR = "wall.brick_corner_br";
	public static final String WALL_END_BOTTOM = "wall.brick_end_down";
	public static final String WALL_END_TOP = "wall.brick_end_top";
	public static final String WALL_END_LEFT = "wall.brick_end_left";
	public static final String WALL_END_RIGHT = "wall.brick_end_right";
	public static final String WALL_END = "wall.brick_end";

	//Liquid types for converison
	public static final String LIQ_TL = "pit.water_brick_tl";
	public static final String LIQ_TM = "pit.water_brick_tm";
	public static final String LIQ_TR = "pit.water_brick_tr";
	public static final String LIQ_BL = "pit.water_brick_bl";
	public static final String LIQ_BM = "pit.water_brick_bm";
	public static final String LIQ_BR = "pit.water_brick_br";

	//Floor types for conversion
	public static final String FLOOR_TL = "floor.brick_tl";
	public static final String FLOOR_TM = "floor.brick_tm";
	public static final String FLOOR_TR = "floor.brick_tr";
	public static final String FLOOR_ML = "floor.brick_ml";
	public static final String FLOOR_MM = "floor.brick_mm";
	public static final String FLOOR_MR = "floor.brick_mr";
	public static final String FLOOR_BL = "floor.brick_bl";
	public static final String FLOOR_BM = "floor.brick_bm";
	public static final String FLOOR_BR = "floor.brick_br";
	public static final String FLOOR_END_TOP = "floor.brick_end_top";
	public static final String FLOOR_END_BOTTOM = "floor.brick_end_bottom";
	public static final String FLOOR_END_LEFT = "floor.brick_end_left";
	public static final String FLOOR_END_RIGHT = "floor.brick_end_right";
	public static final String FLOOR_STRAIGHT_H = "floor.brick_straight_h";
	public static final String FLOOR_STRAIGHT_V = "floor.brick_straight_v";
	public static final String FLOOR_SINGLE = "floor.brick_single";

	//Object types for decoration
	public static final String CHEST_LARGE = "chest.large";
	public static final String LANTERN = "light.lantern";
	public static final String TORCH = "decor.torch_wood";

	//ALGORITHM VARIABLES	
	/** Amount of tries to place a room in the grid*/
	public static int roomDensity = 60;
	/** One in deadRoomChance chance to have more than one exit to a room*/
	public static int deadRoomChance = 2;
	/** One in wallHoleChance to have a hole in a dividing wall*/
	public static int wallHoleChance = 100;
	/** Amount of cycles of the Cellular Automata algorithm we're applying*/
	public static int maxCycles = 6;
	/** Limit of cells beneath which a living cell dies*/
	public static int deathLimit = 3;
	/** Limit of cells over which a dead cell gets revived*/
	public static int birthLimit = 3;
	/** One in startAlive amount of cells start as floor*/
	public static int startAlive = 5;

	/** The grid we're going to be filling and working with. Contains the String tile types defined above*/
	private static String[] grid;
	/** The amount of columns in the grid we're generating*/
	protected static int cols;
	/** The amount of rows in the grid we're generating*/
	protected static int rows;

	/** List of all the rooms that have been placed during roomPlacement*/
	private static ArrayList<Room> rooms = new ArrayList<Room>();
	/** List of all the gridObjects we placed during decoration of the grid*/
	private static ArrayList<GridObject> decoration = new ArrayList<GridObject>();

	/**
	 * Generates a new Dungeon with the specified width and height
	 * @param c
	 * @param r
	 * @return
	 */
	public static String[] generateDungeon(int c, int r) {
		//STEP 0: Set everythings as solid walls and create the lists
		cols = c;
		rows = r;
		grid = new String[cols * rows];
		for(int i = 0; i < grid.length; i++) grid[i] = WALL;
		rooms.clear();

		//STEP 1: Try to place rooms, roomDensity times
		for(int i = 0; i < roomDensity; i++) placeRoom();

		//STEP 2: Pick a random wall at an uneven starting point, push to open list
		ArrayList<Int2D> open = new ArrayList<Int2D>();
		Int2D pos = new Int2D();
		do{pos.rndOdd(cols, rows);}
		while(getCell(pos) != WALL);
		open.add(pos);

		//STEP 3: tunnel randomly from here, pick an available neighbor and make a maze
		ArrayList<Int2D> neighbors = new ArrayList<Int2D>();
		//As long as there are items on the open list
		while(open.size() > 0){
			//Grab the last element from the list
			pos = open.get(open.size() - 1);

			//Find its neighbors
			neighbors = findNeighbors(pos);

			//If there are no neighbors, this is a dead end, remove from open
			if(neighbors.size() == 0){
				open.remove(open.size() - 1);
			}else{//Pick a random neighbor and connect to it and add it to open
				//Grab a random one and add to list
				Int2D neighbor = Rogue.app.random(neighbors);
				open.add(neighbor);
				//Make the path to it floor
				setCell(neighbor, FLOOR);
				setCell(neighbor.copy().add(pos).half(), FLOOR);
			}
		}

		//STEP 4: For every room, punch a hole to the nearest path
		for(Room room : rooms) room.punchHoles();

		//STEP 5: Randomly remove some divider walls (isStraightWall)
		for(int i = 0; i < grid.length; i++){
			pos.x = i % cols;
			pos.y = Rogue.floor(i / cols);
			//Only continue on walls
			if(isWall(getCell(pos))){
				//Only walls that are not outside bounds
				if(pos.x > 0 && pos.x <= cols - 2 && pos.y > 0 && pos.y <= rows - 2){
					//Only walls that are dividers (isStraightWall)
					if(isStraightWall(pos)){
						if(Rogue.oneIn(wallHoleChance)) setCell(pos, FLOOR);
					}
				}
			}
		}

		//STEP 6: Remove dead ends
		int found;
		do{
			found = 0;
			for(int i = 0; i < grid.length; i++){
				if(isFloor(grid[i])){//On all floors check if they are dead ends
					pos.x = i % cols;
					pos.y = Rogue.floor(i / cols);
					if(isDeadEnd(pos)){
						found ++; 
						grid[i] = WALL;
					}
				}
			}
		}while(found > 0);//Remove dead ends as long as we find them

		//STEP 7: Mark all pure wall cells for void creation and create void from them
		ArrayList<Integer> voids = new ArrayList<Integer>();
		for(int i = 0; i < grid.length; i++){
			pos.x = i % cols;
			pos.y = Rogue.floor(i / cols);
			//Only check wall cells
			if(isWall(getCell(pos))){
				boolean left = isWall(getCell(pos.x - 1, pos.y));
				boolean tl = isWall(getCell(pos.x - 1, pos.y - 1));
				boolean right = isWall(getCell(pos.x + 1, pos.y));
				boolean tr = isWall(getCell(pos.x + 1, pos.y - 1));
				boolean up = isWall(getCell(pos.x, pos.y - 1));
				boolean bl = isWall(getCell(pos.x - 1, pos.y + 1));
				boolean down = isWall(getCell(pos.x, pos.y + 1));
				boolean br = isWall(getCell(pos.x + 1, pos.y + 1));
				if(tl && left && tr && up && down && bl && br && right){
					voids.add(i);
				}
			}
		}
		for(int index: voids) grid[index] = VOID;

		//STEP 8: Liquid creation
		for(Room room : rooms){
			//If a room has more than 50 blocks, have a chance to spawn water
			if(room.tiles.size() > 50){
				if(Rogue.app.random(1) < 1.0){//Random chance to spawn water
					//Calculate the maximum size possible
					Int2D maxSize = room.dim.copy().add(new Int2D(-2, -2));
					Int2D minSize = new Int2D(3, 3);
					//Get a random size between the two possible extremes
					Int2D size = new Int2D(Rogue.floor(Rogue.app.random(minSize.x, maxSize.x)),
							Rogue.floor(Rogue.app.random(minSize.y, maxSize.y)));

					//Find a good starting location and add it to the map
					Int2D startPos = room.tiles.get(0).copy().add(new Int2D(1, 1));
					for(int x = startPos.x; x < startPos.x + size.x; x++){
						for(int y = startPos.y; y < startPos.y + size.y; y++){
							setCell(x, y, LIQUID);
						}
					}
				}
			}
		}

		//STEP 9: Wall & floor & liquid conversion
		for(int i = 0; i < grid.length; i++){
			if(isWall(grid[i])){
				grid[i] = convertWall(new Int2D(i % cols, Rogue.floor(i / cols)));
			}else if(isFloor(grid[i])){
				grid[i] = convertFloor(new Int2D(i % cols, Rogue.floor(i / cols)));
			}else if(isLiquid(grid[i])){
				grid[i] = convertLiquid(new Int2D(i % cols, Rogue.floor(i / cols)));
			}
		}

		//STEP FINAL: Return the grid
		return grid;
	}

	/**
	 * Generates all the dynamic gridObjects on the grid, like torches, enemies,
	 * players, treasure etc.
	 * @param gameGrid
	 * @return
	 */
	public static ArrayList<GridObject> generateDecoration(Grid gameGrid){
		//STEP 0: Setup for creation
		decoration = new ArrayList<GridObject>();

		for(int i = 0; i < grid.length; i++){
			GridCell holder = gameGrid.get(i);
			int x = i % cols;
			int y  = Rogue.floor(i / cols);
			Int2D pos = new Int2D(x, y);

			//First try to spawn some doorways
			if(isFloor(grid[i]) && isDoorway(pos)){
				if(Rogue.app.random(1) < 0.5f){
					String texName = DOOR_V;
					if(!isWall(grid[i - cols])) texName = DOOR_H;
					decoration.add(new Door(holder, texName));
				}


				//Chance to spawn some floor decoration
			}else if(isFloor(grid[i])){
				//One in 200 chance to spawn bones
				if(Rogue.oneIn(20)){
					decoration.add(new Bones(holder));
				}
			}


			else if(isWall(grid[i]) && isTorchWall(pos)){
				//One in 50 chance to spawn a lightsource
				if(Rogue.oneIn(10)){
					decoration.add(new LightSource(holder, TORCH));
				}
			}
		}

		/* Place the player in the startRoom*/
		Room startRoom = Rogue.app.random(rooms);
		Int2D startPos = Rogue.app.random(startRoom.tiles);
		GridCell startHolder = gameGrid.get(startPos.x, startPos.y);
		decoration.add(new Player(startHolder));

		//STEP FINAL: return the grid
		return decoration;
	}

	/**
	 * Generates a more natural cave like dungeon. Still very much WIP
	 * @param c
	 * @param r
	 * @return
	 */
	public static String[] generateCave(int c, int r){
		//STEP 0: Set everythings as random(wall/floor)
		cols = c;
		rows = r;
		grid = new String[cols * rows];
		for(int i = 0; i < grid.length; i++){
			grid[i] = (Rogue.oneIn(startAlive)) ? FLOOR : WALL;
		}

		//STEP 1: Set sides to WALL
		for(int x = 0; x < cols; x++){
			setCell(x, 0, WALL);
			setCell(x, rows - 1, WALL);
		}
		for(int y = 0; y < rows; y++){
			setCell(0, y, WALL);
			setCell(cols - 1, y, WALL);
		}

		//STEP 2: Do the algorithm a few times
		for(int i = 0; i < maxCycles; i++) calculateCellularAutomata();

		//STEP 3: Keep only floodfill connected cells
		keepConnected(0);

		//STEP 7: Mark all pure wall cells for void creation and create void from them
		Int2D pos = new Int2D();
		ArrayList<Integer> voids = new ArrayList<Integer>();
		for(int i = 0; i < grid.length; i++){
			pos.x = i % cols;
			pos.y = Rogue.floor(i / cols);
			//Only check wall cells
			if(isWall(getCell(pos))){
				boolean left = isWall(getCell(pos.x - 1, pos.y));
				boolean tl = isWall(getCell(pos.x - 1, pos.y - 1));
				boolean right = isWall(getCell(pos.x + 1, pos.y));
				boolean tr = isWall(getCell(pos.x + 1, pos.y - 1));
				boolean up = isWall(getCell(pos.x, pos.y - 1));
				boolean bl = isWall(getCell(pos.x - 1, pos.y + 1));
				boolean down = isWall(getCell(pos.x, pos.y + 1));
				boolean br = isWall(getCell(pos.x + 1, pos.y + 1));
				if(tl && left && tr && up && down && bl && br && right){
					voids.add(i);
				}
			}
		}
		for(int index: voids) grid[index] = VOID;

		//STEP 9: Wall & floor & liquid conversion
		for(int i = 0; i < grid.length; i++){
			if(isWall(grid[i])){
				grid[i] = convertWall(new Int2D(i % cols, Rogue.floor(i / cols)));
			}else if(isFloor(grid[i])){
				grid[i] = convertFloor(new Int2D(i % cols, Rogue.floor(i / cols)));
			}else if(isLiquid(grid[i])){
				grid[i] = convertLiquid(new Int2D(i % cols, Rogue.floor(i / cols)));
			}
		}

		//STEP FINAL: return the grid
		return grid;
	}

	/**
	 * Cycles the Cellular automate rules we've devised. 
	 */
	private static void calculateCellularAutomata(){
		//Create the empty buffer to work to
		String[] temp = new String[cols * rows];

		//For each cell calculate the rules
		for(int x = 0; x < cols; x++){
			for(int y = 0; y < rows; y++){
				int floorCount = 0;
				if(isFloor(getCell(x - 1, y - 1))) floorCount ++;
				if(isFloor(getCell(x, y - 1))) floorCount ++;
				if(isFloor(getCell(x + 1, y - 1))) floorCount ++;
				if(isFloor(getCell(x - 1, y))) floorCount ++;
				if(isFloor(getCell(x + 1, y))) floorCount ++;
				if(isFloor(getCell(x - 1, y + 1))) floorCount ++;
				if(isFloor(getCell(x, y + 1))) floorCount ++;
				if(isFloor(getCell(x + 1, y + 1))) floorCount ++;
				if(isFloor(getCell(x, y))){
					temp[x + y * cols] = (floorCount > birthLimit) ? FLOOR : WALL;
				}else{
					temp[x + y * cols] = (floorCount < deathLimit) ? WALL : FLOOR;
				}
			}
		}

		//Now assign the temporary buffer to the grid
		grid = temp;
	}

	/**Only keep cells that are connected to each other*/
	private static void keepConnected(int tryCount){
		String[] connected = new String[cols * rows];
		int amtFloor = 0;
		//Copy the grid and coutn the amt of floors
		for(int i = 0; i < grid.length; i++) {
			connected[i] = grid[i];
			if(isFloor(grid[i])) amtFloor++;
		}

		//Then get a random startCell that is not a wall
		int startCell = Rogue.floor(Rogue.app.random(grid.length));
		while(isWall(grid[startCell])) startCell = Rogue.floor(Rogue.app.random(grid.length));

		//Now do a floodfill
		int count = 0;
		ArrayList<Integer> open = new ArrayList<Integer>();
		//Keep flooding as long as there are pixels left
		while(open.size() > 0){
			//Get the cell index
			int index = open.get(0);
			int x = index % cols;
			int y = Rogue.floor(index / cols);
			if(isFloor(getCell(x - 1, y))){
				connected[index] = CONN;
				open.add(index);
			}
			if(isFloor(getCell(x + 1, y))){
				connected[index] = CONN;
				open.add(index);
			}
			if(isFloor(getCell(x, y - 1))){
				connected[index] = CONN;
				open.add(index);
			}
			if(isFloor(getCell(x, y + 1))){
				connected[index] = CONN;
				open.add(index);
			}
			count ++;
			open.remove(0);
		}

		//Now check if the new area hasn't lost more than two thirds size
		if(count < amtFloor / 3){
			if(tryCount > 100){//If we tried a 100 different points, please generate something new
				return;
			}
			keepConnected(tryCount + 1);
			return;
		}

		//If we make it to here, rewrite the main grid
		for(int i = 0; i < grid.length; i++){
			grid[i] = (isType(CONN, connected[i])) ? FLOOR : WALL;
		}
	}

	/**
	 * Tries to place a room in the grid
	 */
	private static void placeRoom() {
		//Random 2D position
		Int2D pos = new Int2D().rndOdd(cols, rows);
		//Random width and height
		Int2D dim = new Int2D().rndOdd(Room.MIN_SIZE, Room.MAX_SIZE, Room.MIN_SIZE, Room.MAX_SIZE);
		//Check if out of bounds, if so, return
		if(pos.x + dim.x >= cols || pos.y + dim.y >= rows) return;

		//Now check if any of the tiles intersect with an already created room
		Room r = new Room(dim);
		for(int x = pos.x; x < pos.x + dim.x; x++) {
			for(int y = pos.y; y < pos.y + dim.y; y++) {
				if(isRoom(getCell(x, y))) return;
				r.tiles.add(new Int2D(x, y));
			}
		}
		//The room is now accepted
		rooms.add(r);
	    for(Int2D tile : r.tiles) setCell(tile, ROOM);
	}

	/**
	 * Finds neighbors in the surronding pixels that are only walls
	 * @param pos
	 * @return
	 */
	private static ArrayList<Int2D> findNeighbors(Int2D pos){
		//Create the list we will return
		ArrayList<Int2D> neighbors = new ArrayList<Int2D>();

		//Check if a side is out of bounds, if not add it to possible neighbors
		if(pos.x - 2 > 0) neighbors.add(new Int2D(pos.x - 2, pos.y));
		if(pos.x + 2 < cols) neighbors.add(new Int2D(pos.x + 2, pos.y));
		if(pos.y - 2 > 0) neighbors.add(new Int2D(pos.x, pos.y - 2));
		if(pos.y + 2 < rows) neighbors.add(new Int2D(pos.x, pos.y + 2));

		//Now check if they are surrounded by wall, only if so, keep them in list
		for(int i = neighbors.size() - 1; i >= 0; i --){
			if(!isAllWall(neighbors.get(i))){
				neighbors.remove(i);
			}
		}
		//Return the results of the neighbor query
		return neighbors;
	}

	//CONVERSION methods

	/**
	 * Converts all the walls in proper context sensitive walls that, depending
	 * on the surroundings make connections etc.
	 * @param pos
	 * @return
	 */
	private static String convertWall(Int2D pos){
		boolean left = isWall(getCell(pos.x - 1, pos.y));
		if(pos.x == 0) left = false;
		boolean top = isWall(getCell(pos.x, pos.y - 1));
		if(pos.y == 0) top = false;
		boolean bottom = isWall(getCell(pos.x, pos.y + 1));
		if(pos.y == rows - 1) bottom = false;
		boolean right = isWall(getCell(pos.x + 1, pos.y));
		if(pos.x == cols - 1) right = false;
		if(left && top && bottom && right) return WALL_X;
		else if(left && bottom && right) return WALL_T_TOP;
		else if(left && top && right) return WALL_T_BOTTOM;
		else if(top && bottom && right) return WALL_T_LEFT;
		else if(top && bottom && left) return WALL_T_RIGHT;
		else if(top && bottom) return WALL_STRAIGHT_V;
		else if(left && right) return WALL_STRAIGHT_H;
		else if(bottom && right) return WALL_CORNER_TL;
		else if(bottom && left) return WALL_CORNER_TR;
		else if(top && right) return WALL_CORNER_BL;
		else if(top && left) return WALL_CORNER_BR;
		else if(top) return WALL_END_BOTTOM;
		else if(right) return WALL_END_LEFT;
		else if(left) return WALL_END_RIGHT;
		else if(bottom) return WALL_END_TOP;
		else return WALL_END;
	}

	/**
	 * Converts all FLOOR tiles into the proper context sensitive tiles
	 * that have edges, corners etc.
	 * @param pos
	 * @return
	 */
	private static String convertFloor(Int2D pos){
		boolean left = isFloor(getCell(pos.x - 1, pos.y));
		boolean top = isFloor(getCell(pos.x, pos.y - 1));
		boolean bottom = isFloor(getCell(pos.x, pos.y + 1));
		boolean right = isFloor(getCell(pos.x + 1, pos.y));
		if(left && top && bottom && right) return FLOOR_MM;
		else if(left && bottom && right) return FLOOR_TM;
		else if(left && top && right) return FLOOR_BM;
		else if(top && bottom && right) return FLOOR_ML;
		else if(top && bottom && left) return FLOOR_MR;
		else if(top && bottom) return FLOOR_STRAIGHT_V;
		else if(left && right) return FLOOR_STRAIGHT_H;
		else if(bottom && right) return FLOOR_TL;
		else if(bottom && left) return FLOOR_TR;
		else if(top && right) return FLOOR_BL;
		else if(top && left) return FLOOR_BR;
		else if(top) return FLOOR_END_BOTTOM;
		else if(right) return FLOOR_END_LEFT;
		else if(left) return FLOOR_END_RIGHT;
		else if(bottom) return FLOOR_END_TOP;
		else return FLOOR_SINGLE;
	}

	/**
	 * Converts all liquid tiles into the right kind of tiles with corners etc 
	 * depding on the surrounding tiles
	 * @param pos
	 * @return
	 */
	private static String convertLiquid(Int2D pos){
		boolean left = isLiquid(getCell(pos.x - 1, pos.y));
		boolean top = isLiquid(getCell(pos.x, pos.y - 1));
		boolean bottom = isLiquid(getCell(pos.x, pos.y + 1));
		boolean right = isLiquid(getCell(pos.x + 1, pos.y));
		if(left && top && right) return LIQ_BM;
		else if(left && bottom && right) return LIQ_TM;
		else if(top && bottom && right) return LIQ_BL;
		else if(top && bottom && left) return LIQ_BR;
		else if(top && bottom) return LIQ_BM;
		else if(left && right) return LIQ_TM;
		else if(bottom && right) return LIQ_TL;
		else if(bottom && left) return LIQ_TR;
		else if(top && right) return LIQ_BL;
		else if(top && left) return LIQ_BR;
		else return LIQ_BM;
	}

	//Is_TYPE methods
	/**
	 * Tests if the provided tile is of the provided tile type
	 * @param type
	 * @param s
	 * @return
	 */
	public static boolean isType(String type, String s) {
		return  s.substring(0, Rogue.constrain(type.length(), 0, s.length())).equals(type);
	}

	/**
	 * If this is a WALL type of tile
	 * @param s
	 * @return
	 */
	public static boolean isWall(String s) {
		return isType(WALL, s);
	}

	/**
	 * Is this a LIQUID type of tile
	 * @param s
	 * @return
	 */
	public static boolean isLiquid(String s) {
		return isType(LIQUID, s);
	}

	/**
	 * Is this a FLOOR type of tile
	 * @param s
	 * @return
	 */
	public static boolean isFloor(String s) {
		return isType(FLOOR, s);
	}

	/**
	 * Is this a ROOM ype of tile
	 * @param s
	 * @return
	 */
	public static boolean isRoom(String s) {
		return isType(ROOM, s);
	}

	/**
	 * Returns the walkable state of the provided tile
	 * @param s
	 * @return
	 */
	public static boolean isWalkAble(String s){
		return isFloor(s) || s.equals(VOID);
	}

	/**
	 * Returns the opacity (true = no light let through) of the provided tile
	 * @param s
	 * @return
	 */
	public static boolean isOpaque(String s){
		return isWall(s);
	}

	/**
	 * Tests if the provided location is a suitable location to hang a torhc on the wall
	 * @param pos
	 * @return
	 */
	private static boolean isTorchWall(Int2D pos){
		boolean left = isWall(getCell(pos.x - 1, pos.y));
		boolean right = isWall(getCell(pos.x + 1, pos.y));
		boolean bottom = isFloor(getCell(pos.x, pos.y + 1));
		return (left || right) && bottom;
	}

	/**
	 * Can be used to test if a floor tile is a suitable location for a  doorway
	 * @param pos
	 * @return
	 */
	private static boolean isDoorway(Int2D pos){
		String left = getCell(pos.x - 1, pos.y);
		String right = getCell(pos.x + 1, pos.y);
		if(!getType(left).equals(getType(right))){ return false;}
		String up = getCell(pos.x, pos.y - 1);
		String down = getCell(pos.x, pos.y + 1);
		if(!getType(up).equals(getType(down))){ return false;}
		if(getType(left).equals(getType(up))){ return false;}
		int wallCount = 2;//At least two should be walls by now
		if(isWall(getCell(pos.x - 1, pos.y - 1))) wallCount ++;
		if(isWall(getCell(pos.x + 1, pos.y - 1))) wallCount ++;
		if(isWall(getCell(pos.x + 1, pos.y + 1))) wallCount ++;
		if(isWall(getCell(pos.x - 1, pos.y + 1))) wallCount ++;
		return (wallCount >= 2 && wallCount <= 3);
	}

	/**
	 * Test to see if this 3x3 is only walls
	 * @param pos
	 * @return
	 */
	private static boolean isAllWall(Int2D pos) {
		for(int x = pos.x - 1; x < pos.x + 1; x++) {
			for(int y = pos.y - 1; y < pos.y + 1; y++) {
				if(!isWall(getCell(x, y))) return false;
			}
		}
		return true;
	}

	/**
	 * Checks to see if the provided piece of tile (floor)
	 * is a dead end (has more or equal to 3 walls surrounding it)
	 * @param pos
	 * @return
	 */
	private static boolean isDeadEnd(Int2D pos){
		int wallCount = 0;
		if(isWall(getCell(pos.x - 1, pos.y))) wallCount ++;
		if(isWall(getCell(pos.x + 1, pos.y))) wallCount ++;
		if(isWall(getCell(pos.x, pos.y - 1))) wallCount ++;
		if(isWall(getCell(pos.x, pos.y + 1))) wallCount ++;
		//If we find more than 2 walls, this is a dead end
		return wallCount >= 3;
	}

	/**
	 * Returns if this is a straight wall (aka, divider wall)
	 * @param pos
	 * @return
	 */
	private static boolean isStraightWall(Int2D pos){
		String left = getCell(pos.x - 1, pos.y);
		String right = getCell(pos.x + 1, pos.y);
		String up = getCell(pos.x, pos.y - 1);
		String down = getCell(pos.x, pos.y + 1);
		return left.equals(right) && up.equals(down) && !left.equals(up);
	}


	/**
	 * Returns the type of this String
	 * @param s
	 * @return
	 */
	public static String getType(String s) {
		if(s.indexOf(".") == -1) return s;
		return s.substring(0, s.indexOf("."));
	}

	//SET CELL METHODS
	/**
	 * Sets the cell at the specified index to the provided value. If
	 * the index value is out of bounds, nothing will happen
	 * @param index
	 * @param val
	 */
	protected static void setCell(int index, String val) {
		if(index < 0 || index >= grid.length) return;
		grid[index] = val;
	}

	/**
	 * Sets the cell at the specified location to the provided value.
	 * If the value is out of bounds, nothing happens
	 * @param x
	 * @param y
	 * @param val
	 */
	protected static void setCell(int x, int y,  String val) {
		if(x < 0 || x >= cols || y < 0 || y >= rows) return;
		setCell(x + y * cols, val);
	}

	/**
	 * Sets the cell at the specified location to the provided value.
	 * If the value is out of bounds, nothing happens
	 * @param pos
	 * @param val
	 */
	protected static void setCell(Int2D pos, String val) {
		setCell(pos.x, pos.y, val);
	}

	//GET CELL METHODS
	/**
	 * Returns the cell at the specified index in the grid, but constrains its
	 * index to prevent outOfBounds errors
	 * @param index
	 * @return
	 */
	protected static String getCell(int index) {
		return grid[Rogue.constrain(index, 0, grid.length - 1)];
	}

	/***
	 * Returns the cell at the specified index in the grid. Out of bounds cells are
	 * assumed to be wall
	 * @param x
	 * @param y
	 * @return
	 */
	protected static String getCell(int x, int y) {
		if(x < 0 || x >= cols || y < 0 || y >= rows) return WALL;
		return getCell(x + y * cols);
	}

	/**
	 * Returns the cell ath the specified position in the grid
	 * @param pos
	 * @return
	 */
	protected static String getCell(Int2D pos) {
		return getCell(pos.x, pos.y);
	}
}
