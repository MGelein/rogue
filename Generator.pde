class DungeonGenerator{
  //Tile types
  String WALL = "wall";
  String FLOOR = "floor";
  String ROOM = "room";
  String VOID = "void";
  String LIQUID = "pit";
  
  //Door types
  String DOOR_H = "door.iron_h";
  String DOOR_V = "door.iron_v";
  
  //Wall types
  String WALL_FLAT = "wall.brick_flat";
  String WALL_T_TOP = "wall.brick_tjunction_top";
  String WALL_T_BOTTOM = "wall.brick_tjunction_bottom";
  String WALL_T_LEFT = "wall.brick_tjunction_left";
  String WALL_T_RIGHT = "wall.brick_tjunction_right";
  String WALL_X = "wall.brick_xjunction";
  String WALL_STRAIGHT_V = "wall.brick_straight_v";
  String WALL_STRAIGHT_H = "wall.brick_straight_h";
  String WALL_CORNER_TL = "wall.brick_corner_tl";
  String WALL_CORNER_BL = "wall.brick_corner_bl";
  String WALL_CORNER_TR = "wall.brick_corner_tr";
  String WALL_CORNER_BR = "wall.brick_corner_br";
  String WALL_END_BOTTOM = "wall.brick_end_down";
  String WALL_END_TOP = "wall.brick_end_top";
  String WALL_END_LEFT = "wall.brick_end_left";
  String WALL_END_RIGHT = "wall.brick_end_right";
  String WALL_END = "wall.brick_end";
  
  //Liquid types
  String LIQ_TL = "pit.water_brick_tl";
  String LIQ_TM = "pit.water_brick_tm";
  String LIQ_TR = "pit.water_brick_tr";
  String LIQ_BL = "pit.water_brick_bl";
  String LIQ_BM = "pit.water_brick_bm";
  String LIQ_BR = "pit.water_brick_br";
  
  //Floor types
  String FLOOR_TL = "floor.brick_tl";
  String FLOOR_TM = "floor.brick_tm";
  String FLOOR_TR = "floor.brick_tr";
  String FLOOR_ML = "floor.brick_ml";
  String FLOOR_MM = "floor.brick_mm";
  String FLOOR_MR = "floor.brick_mr";
  String FLOOR_BL = "floor.brick_bl";
  String FLOOR_BM = "floor.brick_bm";
  String FLOOR_BR = "floor.brick_br";
  String FLOOR_END_TOP = "floor.brick_end_top";
  String FLOOR_END_BOTTOM = "floor.brick_end_bottom";
  String FLOOR_END_LEFT = "floor.brick_end_left";
  String FLOOR_END_RIGHT = "floor.brick_end_right";
  String FLOOR_STRAIGHT_H = "floor.brick_straight_h";
  String FLOOR_STRAIGHT_V = "floor.brick_straight_v";
  String FLOOR_SINGLE = "floor.brick_single";
  
  //Object types
  String CHEST_LARGE = "chest.large";
  String LANTERN = "light.lantern";
  String TORCH = "decor.torch_wood";
  
  
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
  
  //String grid. Every String is of one of the tile types
  String[] grid;
  //Amt of columns in this grid
  int cols;
  //Amt of rows in this grid
  int rows;
  //List of all rooms in this dungeon
  ArrayList<DungeonRoom> rooms = new ArrayList<DungeonRoom>();
  
  //String grid, every String is one of the objcts
  ArrayList<GridObject> decoration;
  
  /** Returns a cell from the grid, prevents out of bounds exceptions*/
  String getCell(int x, int y){
    return grid[constrain(x + y * cols, 0, grid.length - 1)];
  }
  /** Returns a cell from the grid, prevents out of bounds exceptions*/
  String getCell(Int2D pos){ return getCell(pos.x, pos.y);}
  
  /** Sets the cell at the specified coordinate to the specified value*/
  void setCell(int x, int y, String value){
    if(x < 0 || x >= cols || y < 0 || y>= rows) return;
    grid[x + y * cols] = value;
  }
  /** Sets the cell at the specified coordinate to the specified value*/
  void setCell(Int2D pos, String value){ setCell(pos.x, pos.y, value);}
  
  /** Generates a new dungeon with the specified size*/
  String[] generate(int c, int r){
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
      if(isWall(getCell(pos))){
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
        if(isFloor(grid[i])){//On all floors check if they are dead ends
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
      if(isWall(grid[i])){
        grid[i] = convertWall(new Int2D(i % cols, floor(i / cols)));
      }else if(isFloor(grid[i])){
        grid[i] = convertFloor(new Int2D(i % cols, floor(i / cols)));
      }else if(isLiquid(grid[i])){
        grid[i] = convertLiquid(new Int2D(i % cols, floor(i / cols)));
      }
    }
    
    //STEP FINAL: Return the grid
    return grid;
  }
  
  ArrayList<GridObject> generateDecoration(Grid gameGrid){
    //STEP 0: Setup for creation
    decoration = new ArrayList<GridObject>();
    
    for(int i = 0; i < grid.length; i++){
      GridCell holder = gameGrid.get(i);
      int x = i % cols;
      int y  = floor(i / cols);
      Int2D pos = new Int2D(x, y);
      
      //First try to spawn some doorways
      if(isFloor(grid[i]) && isDoorway(pos)){
        if(random(1) < 0.5f){
          String texName = DOOR_V;
          if(!isWall(grid[i - cols])) texName = DOOR_H;
          decoration.add(new Door(x, y, holder, texName));
        }
        
        
      //Chance to spawn some floor decoration
      }else if(isFloor(grid[i])){
        //One in 200 chance to spawn bones
        if(oneIn(200)){
          decoration.add(new Bones(x, y, holder));
        }
      }
      
      
      else if(isWall(grid[i]) && isTorchWall(pos)){
        //One in 50 chance to spawn a lightsource
        if(oneIn(10)){
          decoration.add(new LightSource(x, y, holder, TORCH));
        }
      }
    }
    
    /* Place the player in the startRoom*/
    DungeonRoom startRoom = random(rooms);
    Int2D startPos = random(startRoom.tiles);
    GridCell startHolder = gameGrid.get(startPos.x, startPos.y);
    decoration.add(new Player(startPos.x, startPos.y, startHolder));
    
    /* Pick a room to decorate*/
    for(DungeonRoom r : rooms){
      
    }
    
    //STEP FINAL: return the grid
    return decoration;
  }
  
  /**
  Converts the wall at the specified position into the right type of wall
  **/
  String convertWall(Int2D pos){
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
  String convertFloor(Int2D pos){
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
  
  String convertLiquid(Int2D pos){
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
  
  boolean isTorchWall(Int2D pos){
    boolean left = isWall(getCell(pos.x - 1, pos.y));
    boolean right = isWall(getCell(pos.x + 1, pos.y));
    boolean bottom = isFloor(getCell(pos.x, pos.y + 1));
    return (left || right) && bottom;
  }
  
  boolean isDoorway(Int2D pos){
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
  
  boolean isWalkAble(String s){
    return isFloor(s) || s.equals(VOID);
  }
  
  boolean isOpaque(String s){
    return (!isWalkAble(s)) || isLiquid(s);
  }
  
  boolean isType(String type, String s){
    return  s.substring(0, constrain(type.length(), 0, s.length())).equals(type);
  }
  
  String getType(String s){
    return s.substring(0, s.indexOf("."));
  }
  
  boolean isWall(String s){
    return  isType(WALL, s);
  }
  
  boolean isLiquid(String s){
    return isType(LIQUID, s);
  }
  
  boolean isFloor(String s){
    return isType(FLOOR, s);
  }
  
  boolean isRoom(String s){
    return isType(ROOM, s);
  }
  
  /**Checks if the provided piece of path is a dead end*/
  boolean isDeadEnd(Int2D pos){
    int wallCount = 0;
    if(isWall(getCell(pos.x - 1, pos.y))) wallCount ++;
    if(isWall(getCell(pos.x + 1, pos.y))) wallCount ++;
    if(isWall(getCell(pos.x, pos.y - 1))) wallCount ++;
    if(isWall(getCell(pos.x, pos.y + 1))) wallCount ++;
    //If we find more than 2 walls, this is a dead end
    return wallCount >= 3;
  }
  
  /** If this is a divider wall (straight wall with floors on either side)*/
  boolean isStraightWall(Int2D pos){
    String left = getCell(pos.x - 1, pos.y);
    String right = getCell(pos.x + 1, pos.y);
    String up = getCell(pos.x, pos.y - 1);
    String down = getCell(pos.x, pos.y + 1);
    return left.equals(right) && up.equals(down) && !left.equals(up);
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
        if(!isWall(getCell(x, y))) return false;
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
        if(isRoom(getCell(x, y))) return;
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
        if(isWall(getCell(pos))) setCell(pos, FLOOR);
        //Continue in that direction
        pos.add(dir);
        
        //Edge case, try again and discard this attempt
        if(pos.x <= 1 || pos.x >= cols - 1 || pos.y <= 1 || pos.y >= rows - 1){
          //Try again
          punchHole();
          //And ignore this attempt
          return;
        }
      }while(!isFloor(getCell(pos)));
    }
  }
}