/**
GameState class
**/
class GameState{
  /** Every game state has an update function*/
  void update(){};
  /** Every game state can render using the provided buffer*/
  void render(PGraphics g){};
  /** Every game state can receive mouse down events*/
  void mouseDown(int x, int y){};
  /** Every game state can receive mouse up events*/
  void mouseUp(int x, int y){};
}

/**
The main game class
**/
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
/**
The main menu class
**/
class MainMenu extends GameState{
  
  Button b;
  
  MainMenu(){
    b = new Button(100, 100, "Testbutton");
  }
  
  
  void update(){
    
  }
  
  void render(PGraphics g){
    b.render(g);
  }
  
  /** Called when the mouse is pressed. Passed coordinates are translated using game scale*/
  void mouseDown(int x, int y){
  }
  
  /** Called when the mouse is released. Passed coordinates are translated using game scale*/
  void mouseUp(int x, int y){
  }
}