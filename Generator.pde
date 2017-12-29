class DungeonGenerator{
  //Tile types
  int WALL = 0;
  int FLOOR = 1;
  int ROOM = 2;
  int VOID = 3;
  
  //Amt of tries to place a room in the grid
  int roomDensity = 500;
  //One in $deadRoomChance chance to not have any other exits to one room
  int deadRoomChance = 2;
  //One in $wallHoleChance chance to have a hole in a divider wall
  int wallHoleChance = 50;
  //Maximum size of a generated room
  int max_size = 13;
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
  void generate(int c, int r){
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
        if(isAllWall(pos)) voids.add(i);
      }
    }
    for(int index: voids) grid[index] = VOID;
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
    for(int x = pos.x; x < pos.x + dim.x; x++){
      for(int y = pos.y; y < pos.y + dim.y; y++){
        if(getCell(x, y) == WALL) return;
        //Add this tile to the room
        r.tiles.add(new Int2D(x, y));
      }
    }
    
    //If we made it to here, we can add this room to the list accepted of rooms
    rooms.add(r);
  }
  
  
  /** Inner class contains the dungeon room*/
  class DungeonRoom{
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