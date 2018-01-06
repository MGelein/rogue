class Grid extends MouseAble implements IUpdate{
  
  private ArrayList<Light> lights = new ArrayList<Light>();
  private GridCell[] cells;
  int cols;
  int rows;
  private int animCounter = 0;
  private int animRate = 60;
  private int ambientLight = color(0, 0, 60);
  Int2D viewPoint = new Int2D();
  boolean renderLines = false;
  boolean lightingUpdate = false;
  
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
    lights = new ArrayList<Light>();
    String[] dungeon = generator.generate(cols, rows);
    for(int i = 0; i < dungeon.length; i++){
      get(i).empty();
      get(i).parseTile(dungeon[i]);
    }
    
    //Now generate decoration for this dungeon
    String[] decoration = generator.generateDecoration();
    for(int i = 0; i < decoration.length; i++){
      get(i).parseDecoration(decoration[i]);
    }
    calcLighting();
  }
  
  void calcLighting(){
    //Reset all cells to ambient
    for(GridCell c : cells) c.lighting = ambientLight;
    
    //Recalculate all lighting
    for(Light l : lights) {l.refresh(); l.calculate();};
    
    //Once the lighting has been calculated, reset trigger
    lightingUpdate = false;
  }
  
  void mouseDown(int x, int y){
    Int2D clickPos = new Int2D(floor(x / GRID_SIZE), floor(y / GRID_SIZE));
    clickPos.add(viewPoint);
    get(clickPos).click();
  }
  
  void addLight(Light l){
    lights.add(l);
    lightingUpdate = true;
  }
  
  void removeLight(Light l){
    lights.remove(l);
    lightingUpdate = true;
  }
  
  void update(){
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
  
  void render(PGraphics g){
    //Translate matrix for map viewing.
    g.pushMatrix();
    g.translate(-viewPoint.x * GRID_SIZE, -viewPoint.y * GRID_SIZE);
        
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
  GridCell get(Int2D pos){
    return get(pos.x, pos.y);
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
    if(x - viewPoint.x > COLS_VISIBLE || y - viewPoint.y > ROWS_VISIBLE) return false;
    else if(x - viewPoint.x < 0 || y - viewPoint.y < 0) return false;
    return true;
  }
  
  /** Called to empty this gridCell, completely removes all*/
  void empty(){
    objects = new ArrayList<GridObject>();
    walkable = true;
    opaque = false;
  }
  
  void update(){
    for(GridObject o : objects) {o.update();}
  }
  
  void animate(){
    for(GridObject o : objects) {if(o.animated) o.animate();}
  }
  
  void click(){
    for(GridObject o : objects) o.interact();
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
  
  void parseDecoration(String tile){
    if(dungeonGenerator.isType("light", tile)) add(new LightSource(x, y, this, tile));
    else if(dungeonGenerator.isType("door", tile)) add(new Door(x, y, this, tile));
  }
  
  void parseTile(String tile){
    GridObject o = new GridObject(x, y, this);
    o.parse(tile);
    add(o);
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
  
  void interact(){};
  
  void setTexture(String s){
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
  
  GridObject parse(String dungeonTile){
    //According to tileType set walkability to false if not already false
    walkable = dungeonGenerator.isWalkAble(dungeonTile);
    opaque = dungeonGenerator.isOpaque(dungeonTile);
    
    //If it is not a void tile, set the texture
    if(!dungeonGenerator.isType(dungeonGenerator.VOID, dungeonTile)){
      setTexture(dungeonTile);
    }
    
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
  
  String toString(){
    return "x: " + x + ", y: " + y;
  }
}