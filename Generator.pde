class DungeonGenerator{
  //Tile types
  int WALL = 0;
  int FLOOR = 1;
  int ROOM = 2;
  int VOID = 3;
  int LIQUID = 4;
  
  //Wall types
  int WALL_FLAT = 94;
  int WALL_T_TOP = 95;
  int WALL_T_BOTTOM = 96;
  int WALL_T_LEFT = 97;
  int WALL_T_RIGHT = 98;
  int WALL_X = 99;
  int WALL_STRAIGHT_V = 910;
  int WALL_STRAIGHT_H = 911;
  int WALL_CORNER_TL = 912;
  int WALL_CORNER_BL = 913;
  int WALL_CORNER_TR = 914;
  int WALL_CORNER_BR = 915;
  int WALL_END_BOTTOM = 916;
  int WALL_END_TOP = 917;
  int WALL_END_LEFT = 918;
  int WALL_END_RIGHT = 919;
  int WALL_END = 920;
  
  //Liquid types
  int LIQ_TL = 201;
  int LIQ_TM = 202;
  int LIQ_TR = 203;
  int LIQ_BL = 204;
  int LIQ_BM = 205;
  int LIQ_BR = 206;
  
  //Floor types
  int FLOOR_TL = 121;
  int FLOOR_TM = 122;
  int FLOOR_TR = 123;
  int FLOOR_ML = 124;
  int FLOOR_MM = 125;
  int FLOOR_MR = 126;
  int FLOOR_BL = 127;
  int FLOOR_BM = 128;
  int FLOOR_BR = 129;
  int FLOOR_END_TOP = 130;
  int FLOOR_END_BOTTOM = 131;
  int FLOOR_END_LEFT = 132;
  int FLOOR_END_RIGHT = 133;
  int FLOOR_STRAIGHT_H = 134;
  int FLOOR_STRAIGHT_V = 135;
  int FLOOR_SINGLE = 136;
  
  //Object types
  int CHEST_LARGE = 236;
  int LANTERN = 237;
  
  
  //Amt of tries to place a room in the grid
  int roomDensity = 60;
  //One in $deadRoomChance chance to not have any other exits to one room
  int deadRoomChance = 2;
  //One in $wallHoleChance chance to have a hole in a divider wall
  int wallHoleChance = 100;
  //Maximum size of a generated room
  int max_size = 11;
  //Minimum size of a generated room
  int min_size = 5;
  
  //Integer grid. Every integer is of one of the tile types
  int[] grid;
  //Amt of columns in this grid
  int cols;
  //Amt of rows in this grid
  int rows;
  //List of all rooms in this dungeon
  ArrayList<DungeonRoom> rooms = new ArrayList<DungeonRoom>();
  
  //Integer grid, every integer is one of the objcts
  int[] decoration;
  
  /** Returns a cell from the grid, prevents out of bounds exceptions*/
  int getCell(int x, int y){
    return grid[constrain(x + y * cols, 0, grid.length - 1)];
  }
  /** Returns a cell from the grid, prevents out of bounds exceptions*/
  int getCell(Int2D pos){ return getCell(pos.x, pos.y);}
  
  /** Sets the cell at the specified coordinate to the specified value*/
  void setCell(int x, int y, int value){
    if(x < 0 || x >= cols || y < 0 || y>= rows) return;
    grid[x + y * cols] = value;
  }
  /** Sets the cell at the specified coordinate to the specified value*/
  void setCell(Int2D pos, int value){ setCell(pos.x, pos.y, value);}
  
  /** Generates a new dungeon with the specified size*/
  int[] generate(int c, int r){
    //STEP 0: Set everythings as solid walls and create the lists
    cols = c;
    rows = r;
    grid = new int[cols * rows];
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
        Int2D neighbor = random(neighbors);
        open.add(neighbor);
        //Make the path to it floor
        setCell(neighbor, FLOOR);
        setCell(neighbor.copy().add(pos).half(), FLOOR);
      }
    }
    
    //STEP 4: For every room, punch a hole to the nearest path
    for(DungeonRoom room : rooms) room.punchHoles();
    
    //STEP 5: Randomly remove some divider walls (isStraightWall)
    for(int i = 0; i < grid.length; i++){
      pos.x = i % cols;
      pos.y = floor(i / cols);
      //Only continue on walls
      if(getCell(pos) == WALL){
        //Only walls that are not outside bounds
        if(pos.x > 0 && pos.x <= cols - 2 && pos.y > 0 && pos.y <= rows - 2){
          //Only walls that are dividers (isStraightWall)
          if(isStraightWall(pos)){
            if(oneIn(wallHoleChance)) setCell(pos, FLOOR);
          }
        }
      }
    }
    
    //STEP 6: Remove dead ends
    int found;
    do{
      found = 0;
      for(int i = 0; i < grid.length; i++){
        if(grid[i] == FLOOR){//On all floors check if they are dead ends
          pos.x = i % cols;
          pos.y = floor(i / cols);
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
      pos.y = floor(i / cols);
      //Only check wall cells
      if(getCell(pos) == WALL){
        int left = getCell(pos.x - 1, pos.y);
        int tl = getCell(pos.x - 1, pos.y - 1);
        int right = getCell(pos.x + 1, pos.y);
        int tr = getCell(pos.x + 1, pos.y - 1);
        int up = getCell(pos.x, pos.y - 1);
        int bl = getCell(pos.x - 1, pos.y + 1);
        int down = getCell(pos.x, pos.y + 1);
        int br = getCell(pos.x + 1, pos.y + 1);
        if(tl + left + tr + up + down + bl + br + right <= 0){
          voids.add(i);
        }
      }
    }
    for(int index: voids) grid[index] = VOID;
    
    //STEP 8: Liquid creation
    for(DungeonRoom room : rooms){
      //If a room has more than 50 blocks, have a chance to spawn water
      if(room.tiles.size() > 50){
        if(random(1) < 1.0){//Random chance to spawn water
          //Calculate the maximum size possible
          Int2D maxSize = room.dim.copy().add(new Int2D(-2, -2));
          Int2D minSize = new Int2D(3, 3);
          //Get a random size between the two possible extremes
          Int2D size = new Int2D(floor(random(minSize.x, maxSize.x)),
                                 floor(random(minSize.y, maxSize.y)));
                                 
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
      if(grid[i] == WALL){
        grid[i] = convertWall(new Int2D(i % cols, floor(i / cols)));
      }else if(grid[i] == FLOOR){
        grid[i] = convertFloor(new Int2D(i % cols, floor(i / cols)));
      }else if(grid[i] == LIQUID){
        grid[i] = convertLiquid(new Int2D(i % cols, floor(i / cols)));
      }
    }
    
    //STEP FINAL: Return the grid
    return grid;
  }
  
  int[] generateDecoration(){
    //STEP 0: Setup for creation
    decoration = new int[cols * rows];
    for(int i = 0; i < decoration.length; i++) decoration[i] = VOID;
    
    //STEP 1: Generate chests
    for(int i = 0; i < grid.length; i++){
      if(isFloor(grid[i])){
        if(oneIn(50)){
          decoration[i] = LANTERN;
        }
      }
    }
    
    //STEP FINAL: return the grid
    return decoration;
  }
  
  /**
  Converts the wall at the specified position into the right type of wall
  **/
  int convertWall(Int2D pos){
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
  
  /** Converts floor into proper tiled floors */
  int convertFloor(Int2D pos){
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
  
  int convertLiquid(Int2D pos){
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
  
  boolean isWalkAble(int n){
    return isFloor(n) || n == VOID;
  }
  
  boolean isOpaque(int n){
    return isFloor(n) || isLiquid(n);
  }
  
  boolean isWall(int n){
    return n == WALL || n == WALL_FLAT || n == WALL_T_TOP || n == WALL_T_BOTTOM || n == WALL_T_LEFT
    || n == WALL_T_RIGHT || n == WALL_X || n == WALL_STRAIGHT_V || n == WALL_STRAIGHT_H
    || n == WALL_CORNER_TL || n == WALL_CORNER_BL || n == WALL_CORNER_TR || n == WALL_CORNER_BR
    || n == WALL_END_BOTTOM || n == WALL_END_TOP || n == WALL_END_LEFT || n == WALL_END_RIGHT;
  }
  
  boolean isLiquid(int n){
    return n == LIQUID || n == LIQ_TL || n == LIQ_TM || n == LIQ_TR || n == LIQ_BL || n == LIQ_BM || n == LIQ_BR;
  }
  
  boolean isFloor(int n){
    return n == FLOOR || n == FLOOR_TL || n == FLOOR_TM || n == FLOOR_TR
    || n == FLOOR_ML || n == FLOOR_MM || n == FLOOR_MR || n == FLOOR_BL || n == FLOOR_BM
    || n == FLOOR_BR || n == FLOOR_END_TOP || n == FLOOR_END_BOTTOM || n == FLOOR_END_LEFT
    || n == FLOOR_END_RIGHT || n == FLOOR_STRAIGHT_V || n == FLOOR_STRAIGHT_H
    || n == FLOOR_SINGLE;
  }
  
  /**Checks if the provided piece of path is a dead end*/
  boolean isDeadEnd(Int2D pos){
    int wallCount = 0;
    if(getCell(pos.x - 1, pos.y) == WALL) wallCount ++;
    if(getCell(pos.x + 1, pos.y) == WALL) wallCount ++;
    if(getCell(pos.x, pos.y - 1) == WALL) wallCount ++;
    if(getCell(pos.x, pos.y + 1) == WALL) wallCount ++;
    //If we find more than 2 walls, this is a dead end
    return wallCount >= 3;
  }
  
  /** If this is a divider wall (straight wall with floors on either side)*/
  boolean isStraightWall(Int2D pos){
    int left = getCell(pos.x - 1, pos.y);
    int right = getCell(pos.x + 1, pos.y);
    int up = getCell(pos.x, pos.y - 1);
    int down = getCell(pos.x, pos.y + 1);
    return left == right && up == down && left != up;
  }
  
  /** Finds neighbors that are only walls in the surround 9 pixels*/
  ArrayList<Int2D> findNeighbors(Int2D pos){
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
  
  /**
  If this position is surrounded by only wall
  **/
  boolean isAllWall(Int2D p){
    for(int x = p.x - 1; x < p.x + 1; x++){
      for(int y = p.y - 1; y < p.y + 1; y++){
        if(getCell(x, y) != WALL) return false;
      }
    }
    return true;
  }
  
  /**
  Tries to place a room in the integer grid
  **/
  void placeRoom(){
    //Random 2D position for the top left of the room
    Int2D pos = new Int2D().rndOdd(cols, rows);
    //Random width and height between min and max room size
    Int2D dim = new Int2D().rndOdd(min_size, max_size, min_size, max_size);
    //Check if the room is not out of bounds, if so return
    if(pos.x + dim.x >= cols || pos.y + dim.y >= rows) return;
    
    //Now check if any of the tiles intersect with already created tiles.
    //If not, add the tile to the a new dungeonroom.
    DungeonRoom r = new DungeonRoom();
    r.dim = dim.copy();
    for(int x = pos.x; x < pos.x + dim.x; x++){
      for(int y = pos.y; y < pos.y + dim.y; y++){
        if(getCell(x, y) == ROOM) return;
        //Add this tile to the room
        r.tiles.add(new Int2D(x, y));
      }
    }
    
    //If we made it to here, we can add this room to the list accepted of rooms
    rooms.add(r);
    for(Int2D tile : r.tiles) setCell(tile, ROOM);
  }
  
  
  /** Inner class contains the dungeon room*/
  class DungeonRoom{
    Int2D dim;
    /** List of coords of the tiles in this room*/
    ArrayList<Int2D> tiles = new ArrayList<Int2D>();
    
    /** Punches at least one hole, and possibly more than one to path*/
    void punchHoles(){
      //At least punch one hole
      punchHole();
      
      //Now deadRoom chance percent to make more holes
      while(oneIn(deadRoomChance)){
        punchHole();
      }
      
      //Finally set the room to be floor now we're done with it
      for(Int2D tile : tiles){setCell(tile, FLOOR);}
    }
    
    /** Does the actual punching of holes*/
    private void punchHole(){
      //Grab a random odd position in the room
      Int2D pos = random(tiles);
      while(!pos.isOdd()){
        pos = random(tiles);
      }      
      //Pick a random direction
      Int2D dir = new Int2D().rndDir();
      pos = pos.copy();
      
      //Punch through untill we reach FLOOR
      do{
        //Only change type of cell if we're carving through wall
        if(getCell(pos) == WALL) setCell(pos, FLOOR);
        //Continue in that direction
        pos.add(dir);
        
        //Edge case, try again and discard this attempt
        if(pos.x <= 1 || pos.x >= cols - 1 || pos.y <= 1 || pos.y >= rows - 1){
          //Try again
          punchHole();
          //And ignore this attempt
          return;
        }
      }while(getCell(pos) != FLOOR);
    }
  }
}