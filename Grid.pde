class Grid implements IRender{
  
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
  
  /** Loads the cells from the specified room*/
  void load(Room r){
    for(GridCell c : cells) c.empty();
    for(GridObject o : r.objects){
      get(o.x, o.y).objects.add(o);
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

class GridCell implements IRender{
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
class GridObject implements IRender{
  int x;
  int y;
  PImage tex;
  XML data;
  
  GridObject(int x, int y, XML data){
    this.x = x;
    this.y = y;
    this.data = data;
  }
  
  void setTexture(String s){
    tex = textures.get(s);
  }
  
  void update(){};
  
  void render(PGraphics g){
    g.image(tex, x * SIZE, y * SIZE);
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
}