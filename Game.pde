class Game extends GameState{
  
  Grid grid;
  
  Game(){
    //Create a new grid at the specified dimensions
    grid = new Grid(24, 18);
    grid.load(new Room("rooms/room.xml"));
  }
  
  void update(){
    //Update the grid
    grid.update();
  }
  
  /** Render using the provided graphics buffer */
  void render(PGraphics g){
    //Render the grid
    grid.render(g);
  }
  
  /** Called when the mouse is pressed. Passed coordinates are translated using game scale*/
  void mouseDown(int x, int y){
  }
  
  /** Called when the mouse is released. Passed coordinates are translated using game scale*/
  void mouseUp(int x, int y){
  }
}