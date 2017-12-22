/**
GameState class
**/
class GameState implements IRender{
  /** List of objects that will listen for mouse clicks*/
  ArrayList<IMouse> mouseObjects = new ArrayList<IMouse>();
  
  /** List of objects to render*/
  ArrayList<IRender> renderObjects = new ArrayList<IRender>();
  
  /** List of objects to update*/
  ArrayList<IUpdate> updateObjects = new ArrayList<IUpdate>();
  
  /** Every game state has an update function*/
  void update(){
    for(IUpdate u : updateObjects) u.update();
  };
  
  /** Every game state can render using the provided buffer*/
  void render(PGraphics g){
    for(IRender r : renderObjects) r.render(g);
  };
  
  /** Every game state can receive mouse down events*/
  void mouseDown(int x, int y){
    for(IMouse i : mouseObjects){
      if(i.isHighlighted()) i.mouseDown();
    }
  };
  /** Every game state can receive mouse up events*/
  void mouseUp(int x, int y){
    for(IMouse i : mouseObjects){
      if(i.isHighlighted()) i.mouseUp();
    }
  };
  /** Add object to list of objects that are listened to by the mouse*/
  void addMouse(IMouse... m){
    for(IMouse mObj : m) mouseObjects.add(mObj);
  }
  /** Remove object from list of mouse objects*/
  void removeMousee(IMouse... m){
    for(IMouse mObj : m) mouseObjects.remove(mObj);
  }
  
  /** Adds the object to the list of objects to render*/
  void addRender(IRender... r){
    for(IRender rObj : r) renderObjects.add(rObj);
  }
  
  /** Removes the specified object from the list of objects to render*/
  void removeRender(IRender... r){
    for(IRender rObj : r) renderObjects.remove(rObj);
  }
  
  /** Adds a new item to the list of items to be updated*/
  void addUpdate(IUpdate... u){
    for(IUpdate uObj : u) updateObjects.add(uObj);
  }
  
  /** Removes the specified item from the list of objects to update*/
  void removeUpdate(IUpdate... u){
    for(IUpdate uObj : u) updateObjects.remove(uObj);
  }
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
    addRender(grid);
  } 
}
/**
The main menu class
**/
class MainMenu extends GameState{
  
  MainMenu(){
    TileButton health = new TileButton(16, 16, RED, textures.get("gui.hp_heart_full"));
    TileButton stamina = new TileButton(32, 16, GREEN, textures.get("gui.stamina_heart_full"));
    TileButton mana = new TileButton(48, 16, BLUE, textures.get("gui.mana_heart_full"));
    addRender(health, stamina, mana);
    addMouse(health, stamina, mana);
  }
}