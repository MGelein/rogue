class Grid extends RenderAble{
  
  private GridCell[] cells;
  private int cols;
  private int rows;
  boolean renderLines = false;
  
  Grid(int maxCols, int maxRows){
    rows = maxRows;
    cols = maxCols;
    
    //initialize the grid
    cells = new GridCell[maxCols * maxRows];
    for(int col = 0; col < maxCols; col++){
      for(int row = 0; row < maxRows; row++){
        cells[col + row * cols] = new GridCell(col, row);
      }
    }
  }
  
  void load(DungeonGenerator generator){
    int[] dungeon = generator.generate(cols, rows);
    for(int i = 0; i < dungeon.length; i++){
      int x = i % cols;
      int y = floor(i / cols);
      get(i).empty();
      get(i).objects.add(new GridObject(x, y).parse(dungeon[i]));
    }
  }
  
  void update(){
    for(GridCell c : cells) c.update();
  }
  
  void render(PGraphics g){
    for(GridCell c : cells) {
      c.render(g);
    }
    if(renderLines){
      g.noFill();
      g.stroke(255, 100);
      for(GridCell c : cells){
        c.renderGrid(g);
      }
    }
  }
 
  GridCell get(int x, int y){
    return get(x + y * cols);
  }  
  GridCell get(int index){
     return cells[constrain(index, 0, cells.length - 1)];
  }
}

class GridCell extends RenderAble{
  int x;
  int y;
  int size = SIZE;
  ArrayList<GridObject> objects = new ArrayList<GridObject>();
  
  GridCell(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  /** Called to empty this gridCell, completely removes all*/
  void empty(){
    objects = new ArrayList<GridObject>();
  }
  
  void update(){
    for(GridObject o : objects) o.update();
  }
  
  void render(PGraphics g){
    for(GridObject o : objects) o.render(g);
  }
  
  void renderGrid(PGraphics g){
    g.rect(x * SIZE, y * SIZE, x * SIZE + SIZE, y * SIZE + SIZE);
  }
}

/** Every single object on one tile is a gridObject*/
class GridObject extends RenderAble{
  int x;
  int y;
  PImage tex;
  
  GridObject(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  void setTexture(String s){
    tex = textures.get(s);
  }
  
  void update(){};
  
  void render(PGraphics g){
    if(tex == null) return;
    g.image(tex, x * SIZE, y * SIZE);
  }
  
  GridObject parse(int dungeonTile){
    if(dungeonTile == dungeonGenerator.FLOOR) setTexture("floor.brick_mm");
    if(dungeonTile == dungeonGenerator.WALL_FLAT) setTexture("wall.brick_flat");
    if(dungeonTile == dungeonGenerator.WALL_T_TOP) setTexture("wall.brick_tjunction_top");
    if(dungeonTile == dungeonGenerator.WALL_T_BOTTOM) setTexture("wall.brick_tjunction_bottom");
    if(dungeonTile == dungeonGenerator.WALL_T_LEFT) setTexture("wall.brick_tjunction_left");
    if(dungeonTile == dungeonGenerator.WALL_T_RIGHT) setTexture("wall.brick_tjunction_right");
    if(dungeonTile == dungeonGenerator.WALL_X) setTexture("wall.brick_xjunction");
    if(dungeonTile == dungeonGenerator.WALL_STRAIGHT_V) setTexture("wall.brick_straight_v");
    if(dungeonTile == dungeonGenerator.WALL_STRAIGHT_H) setTexture("wall.brick_straight_h");
    if(dungeonTile == dungeonGenerator.WALL_CORNER_TL) setTexture("wall.brick_corner_tl");
    if(dungeonTile == dungeonGenerator.WALL_CORNER_BL) setTexture("wall.brick_corner_bl");
    if(dungeonTile == dungeonGenerator.WALL_CORNER_TR) setTexture("wall.brick_corner_tr");
    if(dungeonTile == dungeonGenerator.WALL_CORNER_BR) setTexture("wall.brick_corner_br");
    if(dungeonTile == dungeonGenerator.WALL_END) setTexture("wall.brick_end");
    return this;
  }
}

/**
Contains integer coordinates for textures and grids
**/
class Int2D{
  int x;
  int y;
  Int2D(){
    x = y = 0;
  }
  Int2D(int x, int y){
    this.x = x;
    this.y = y;
  }
  Int2D(String def){
    if(def.indexOf(',') != -1){
      String[] parts = def.split(",");
      x = int(parts[0].trim());
      y = int(parts[1].trim());
    }else{
      println("Illegal Int2D definition: " + def);
      x = -1;
      y = -1;
    }
  }
  
   Int2D rndOdd(int minX, int maxX, int minY, int maxY){
    x = floor(random(minX, maxX - 1));
    y = floor(random(minY, maxY - 1));
    //make sure that the coords are odd
    x = (x % 2 == 0) ? x + 1: x;
    y = (y % 2 == 0) ? y + 1: y;
    return this;
  
  }
  
  Int2D rndOdd(int maxX, int maxY){
    return rndOdd(0, maxX, 0, maxY);
  }
  
  Int2D copy(){
    return new Int2D(x, y);
  }
  
  Int2D add(Int2D b){
    x += b.x;
    y += b.y;
    return this;
  }
  
  Int2D half(){
    x *= 0.5f;
    y *= 0.5f;
    return this;
  }
  
  /**Returns a one length 2D 90 degree vector*/
  Int2D rndDir(){
    int rnd = floor(random(4));
    if(rnd == 0) x = -1;
    else if(rnd == 1) x = 1;
    else if(rnd == 2) y = -1;
    else if(rnd == 3) y = 1;
    return this;
  }
  
  boolean isOdd(){
    return (x % 2 != 0) && (y % 2 != 0);
  }
}