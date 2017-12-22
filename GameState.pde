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
    //New game button
    BigTextButton newGameButton = new BigTextButton(0, 112, GREEN, "New Game");
    newGameButton.centerX();
    
    //Dugeon editor button
    BigTextButton editorButton = new BigTextButton(0, 176, BLUE, "Dungeon Editor");
    editorButton.centerX();
    
    //Close button
    BigTextButton closeButton = new BigTextButton(0, 240, RED, "Exit");
    closeButton.addMouseHandler(new MouseHandler(){
      void mouseDown(int button){ exit();}
    });
    closeButton.centerX();
    
    int bottomY = stage.height - 40;
    final FancyButton musicButton = new FancyButton(8, bottomY, YELLOW, "Music: ON", textures.get("music.trumpet"));
    musicButton.addMouseHandler(new MouseHandler(){
      void mouseDown(int button){
        musicButton.text = (musicButton.text == "Music: ON") ? "Music: OFF" : "Music: ON";
        musicButton.recalc = true;
      }
    });
    
    addRender(newGameButton, editorButton, closeButton, musicButton);
    addMouse(newGameButton, editorButton, closeButton, musicButton);
  }
  
  void render(PGraphics g){
    //Paint the BG
    g.tint(255, 100);
    g.image(textures.mainMenuBG, 0, 0, g.width, g.height);
    g.tint(255);
    
    //Draw the title lettering
    drawTitle(g);
    
    
    //Finally paint the elements
    super.render(g);   
  }
  
  void drawTitle(PGraphics g){
    g.textSize(36);//Set size to title size
    String title = "Rogue 2D";
    float titleWidth = g.textWidth(title);
    float posX = (g.width - titleWidth) * 0.5f;
    float posY = 100;
    int shadowOffset = 3;
    g.fill(0);//Black bg for the text, sort of dropshadow
    g.text(title, posX + shadowOffset, posY + shadowOffset);
    g.fill(255, 0, 0);
    g.text(title, posX, posY);
    g.fill(255);
    g.text(title.substring(0, 5), posX, posY);
    g.fill(255);
    g.textSize(8);//Reset to normal size
  }
}