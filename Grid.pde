class Grid extends RenderAble implements IUpdate{
  
  private ArrayList<Light> lights = new ArrayList<Light>();
  private GridCell[] cells;
  int cols;
  int rows;
  private int animCounter = 0;
  private int animRate = 60;
  private int ambientLight = color(0, 0, 60);
  Int2D viewPoint = new Int2D();
  boolean renderLines = false;
  
  Grid(int maxCols, int maxRows){
    rows = maxRows;
    cols = maxCols;
    
    //initialize the grid
    cells = new GridCell[maxCols * maxRows];
    for(int col = 0; col < maxCols; col++){
      for(int row = 0; row < maxRows; row++){
        cells[col + row * cols] = new GridCell(col, row);
        cells[col + row * cols].grid = this;
      }
    }
  }
  
  void load(DungeonGenerator generator){
    int[] dungeon = generator.generate(cols, rows);
    for(int i = 0; i < dungeon.length; i++){
      int x = i % cols;
      int y = floor(i / cols);
      get(i).empty();
      get(i).add(new GridObject(x, y, get(i)).parse(dungeon[i]));
    }
    
    //Now generate decoration for this dungeon
    int[] decoration = generator.generateDecoration();
    for(int i = 0; i < decoration.length; i++){
      int x = i % cols;
      int y = floor(i / cols);
      get(i).add(new GridObject(x, y, get(i)).parse(decoration[i]));
    }
    
    calcLighting();
  }
  
  void calcLighting(){
    //Reset all cells to ambient
    for(GridCell c : cells) c.lighting = ambientLight;
    
    //Recalculate all lighting
    for(Light l : lights) l.calculate();
  }
  
  void addLight(Light l){
    lights.add(l);
  }
  
  void removeLight(Light l){
    lights.remove(l);
  }
  
  void update(){
    for(GridCell c : cells) c.update();
    
    //Also forward animation
    animCounter++;
    if(animCounter > animRate){
      animCounter -= animRate;
      for(GridCell c: cells) c.animate();
    }
  }
  
  void render(PGraphics g){
    //Translate matrix for map viewing.
    g.pushMatrix();
    g.translate(viewPoint.x * GRID_SIZE, viewPoint.y * GRID_SIZE);
        
    for(GridCell c : cells) {
      if(c.isVisible(viewPoint)) c.render(g);
    }
    if(renderLines){
      g.noFill();
      g.stroke(255, 100);
      for(GridCell c : cells){
        c.renderGrid(g);
      }
    }
    g.popMatrix();
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
  Grid grid;
  color lighting;
  boolean walkable = true;
  boolean opaque = false;
  int size = GRID_SIZE;
  ArrayList<GridObject> objects = new ArrayList<GridObject>();
  
  GridCell(int x, int y){
    this.x = x;
    this.y = y;
  }
  
  /** Check if we should render you*/
  boolean isVisible(Int2D viewPoint){
    if(x + viewPoint.x > COLS_VISIBLE || y + viewPoint.y > ROWS_VISIBLE) return false;
    else if(x + viewPoint.x < 0 || y + viewPoint.y < 0) return false;
    return true;
  }
  
  /** Called to empty this gridCell, completely removes all*/
  void empty(){
    objects = new ArrayList<GridObject>();
  }
  
  void update(){
    for(GridObject o : objects) {o.update();}
  }
  
  void animate(){
    for(GridObject o : objects) {if(o.animated) o.animate();}
  }
  
  void render(PGraphics g){
    g.tint(lighting);
    for(GridObject o : objects) o.render(g);
    g.tint(color(255));
  }
  
  void renderGrid(PGraphics g){
    g.rect(x * size, y * size, x * size + size, y * size + size);
  }
  
  void add(GridObject o){
    o.parentCell = this;
    objects.add(o);
    if(!o.walkable) walkable = false;
    if(!o.opaque) opaque = false;
  }
  
  void remove(GridObject o){
    objects.remove(o);
  }
}

/** Every single object on one tile is a gridObject*/
class GridObject extends RenderAble{
  int x;
  int y;
  PImage tex;
  String texName;
  int texFrame = 0;
  Int2D texMod = new Int2D();
  boolean walkable = false;
  boolean opaque = false;
  boolean animated = false;
  GridCell parentCell;
  
  GridObject(int x, int y, GridCell cell){
    this.x = x;
    this.y = y;
    parentCell = cell;
  }
  
  void setTexture(String s){
    if(s == "light.lantern"){
      parentCell.grid.addLight(new Light(new Int2D(x, y), color(25, 255, 200), 6, parentCell.grid));
    }
    tex = textures.get(s);
    texMod = textures.currentTheme;
    animated = textures.isAnimated(s);
    texName = s;
  }
  
  void setTexture(String s, boolean anim){
    setTexture(s);
    animated = anim;
  }
  
  void update(){
  };
  
  void animate(){
    texFrame ++;
    textures.setThemeModifier(texMod);
    tex = textures.get(texName, texFrame);
    textures.setThemeModifier(textures.theme.none);
  }
  
  void render(PGraphics g){
    if(tex == null) return;
    g.image(tex, x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
  }
  
  GridObject parse(int dungeonTile){
    //According to tileType set walkability to false if not already false
    walkable = dungeonGenerator.isWalkAble(dungeonTile);
    opaque = dungeonGenerator.isOpaque(dungeonTile);
    
    //Set theme
    textures.setThemeModifier(textures.theme.none);
    //WALLS
    if(dungeonTile == dungeonGenerator.FLOOR) setTexture("floor.brick_mm");
    else if(dungeonTile == dungeonGenerator.WALL_FLAT) setTexture("wall.brick_flat");
    else if(dungeonTile == dungeonGenerator.WALL_T_TOP) setTexture("wall.brick_tjunction_top");
    else if(dungeonTile == dungeonGenerator.WALL_T_BOTTOM  ) setTexture("wall.brick_tjunction_bottom");
    else if(dungeonTile == dungeonGenerator.WALL_T_LEFT) setTexture("wall.brick_tjunction_left");
    else if(dungeonTile == dungeonGenerator.WALL_T_RIGHT) setTexture("wall.brick_tjunction_right");
    else if(dungeonTile == dungeonGenerator.WALL_X) setTexture("wall.brick_xjunction");
    else if(dungeonTile == dungeonGenerator.WALL_STRAIGHT_V) setTexture("wall.brick_straight_v");
    else if(dungeonTile == dungeonGenerator.WALL_STRAIGHT_H) setTexture("wall.brick_straight_h");
    else if(dungeonTile == dungeonGenerator.WALL_CORNER_TL) setTexture("wall.brick_corner_tl");
    else if(dungeonTile == dungeonGenerator.WALL_CORNER_BL) setTexture("wall.brick_corner_bl");
    else if(dungeonTile == dungeonGenerator.WALL_CORNER_TR) setTexture("wall.brick_corner_tr");
    else if(dungeonTile == dungeonGenerator.WALL_CORNER_BR) setTexture("wall.brick_corner_br");
    else if(dungeonTile == dungeonGenerator.WALL_END_TOP) setTexture("wall.brick_end_top");
    else if(dungeonTile == dungeonGenerator.WALL_END_BOTTOM) setTexture("wall.brick_end_down");
    else if(dungeonTile == dungeonGenerator.WALL_END_LEFT) setTexture("wall.brick_end_left");
    else if(dungeonTile == dungeonGenerator.WALL_END_RIGHT) setTexture("wall.brick_end_right");
    else if(dungeonTile == dungeonGenerator.WALL_END) setTexture("wall.brick_end");
    
        //FLOORS
    else if(dungeonTile == dungeonGenerator.FLOOR_TL) setTexture("floor.brick_tl");
    else if(dungeonTile == dungeonGenerator.FLOOR_TM) setTexture("floor.brick_tm");
    else if(dungeonTile == dungeonGenerator.FLOOR_TR) setTexture("floor.brick_tr");
    else if(dungeonTile == dungeonGenerator.FLOOR_ML) setTexture("floor.brick_ml");
    else if(dungeonTile == dungeonGenerator.FLOOR_MM) setTexture("floor.brick_mm");
    else if(dungeonTile == dungeonGenerator.FLOOR_MR) setTexture("floor.brick_mr");
    else if(dungeonTile == dungeonGenerator.FLOOR_BL) setTexture("floor.brick_bl");
    else if(dungeonTile == dungeonGenerator.FLOOR_BM) setTexture("floor.brick_bm");
    else if(dungeonTile == dungeonGenerator.FLOOR_BR) setTexture("floor.brick_br");
    else if(dungeonTile == dungeonGenerator.FLOOR_END_TOP) setTexture("floor.brick_end_top");
    else if(dungeonTile == dungeonGenerator.FLOOR_END_BOTTOM) setTexture("floor.brick_end_bottom");
    else if(dungeonTile == dungeonGenerator.FLOOR_END_LEFT) setTexture("floor.brick_end_left");
    else if(dungeonTile == dungeonGenerator.FLOOR_END_RIGHT) setTexture("floor.brick_end_right");
    else if(dungeonTile == dungeonGenerator.FLOOR_STRAIGHT_V) setTexture("floor.brick_straight_v");
    else if(dungeonTile == dungeonGenerator.FLOOR_STRAIGHT_H) setTexture("floor.brick_straight_h");
    else if(dungeonTile == dungeonGenerator.FLOOR_SINGLE) setTexture("floor.brick_single");
    
    
    //Reset theme for non themed tiles
    textures.setThemeModifier(textures.theme.none);
    
    //DECORATION
    if(dungeonTile == dungeonGenerator.CHEST_LARGE) setTexture("chest.large", false);
    else if(dungeonTile == dungeonGenerator.LANTERN) setTexture("light.lantern");
    
    textures.setThemeModifier(textures.theme.poison_brick);
    //LIQUID
    if(dungeonTile == dungeonGenerator.LIQUID) setTexture("pit.water_brick");
    else if(dungeonTile == dungeonGenerator.LIQ_TL) setTexture("pit.water_brick_tl");
    else if(dungeonTile == dungeonGenerator.LIQ_TM) setTexture("pit.water_brick_tm");
    else if(dungeonTile == dungeonGenerator.LIQ_TR) setTexture("pit.water_brick_tr");
    else if(dungeonTile == dungeonGenerator.LIQ_BL) setTexture("pit.water_brick_bl");
    else if(dungeonTile == dungeonGenerator.LIQ_BM) setTexture("pit.water_brick_bm");
    else if(dungeonTile == dungeonGenerator.LIQ_BR) setTexture("pit.water_brick_br");
    
    //Reset theme
    textures.setThemeModifier(textures.theme.none);
    
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
  
  Int2D diff(Int2D b){
    return new Int2D(x - b.x, y - b.y);
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