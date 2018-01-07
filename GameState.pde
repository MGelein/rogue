/**
GameState class
**/
class GameState{  
  /** List of objects to render*/
  ArrayList<RenderAble> renderObjects = new ArrayList<RenderAble>();
  
  /** List of objects to update*/
  ArrayList<IUpdate> updateObjects = new ArrayList<IUpdate>();
  
  /** Every game state has an update function*/
  void update(){
    //Now update all of them
    for(IUpdate u : updateObjects) u.update();
  };
  
  /** Every game state can render using the provided buffer*/
  void render(PGraphics g){
    for(RenderAble r : renderObjects) r.render(g);
  };
  
  /** Adds the object to the list of objects to render*/
  void addRender(RenderAble... r){
    for(RenderAble rObj : r) renderObjects.add(rObj);
  }
  
  void addMouse(MouseAble... m){
    for(MouseAble mObj : m) mouseDistributor.add(mObj);
  }
  
  void removeMouse(MouseAble... m){
    for(MouseAble mObj : m) mouseDistributor.add(mObj);
  }
  
  /** Removes the specified object from the list of objects to render*/
  void removeRender(RenderAble... r){
    for(RenderAble rObj : r) renderObjects.remove(rObj);
  }
  
  /** Adds a new item to the list of items to be updated*/
  void addUpdate(IUpdate... u){
    for(IUpdate uObj : u) updateObjects.add(uObj);
  }
  
  /** Removes the specified item from the list of objects to update*/
  void removeUpdate(IUpdate... u){
    for(IUpdate uObj : u) updateObjects.remove(uObj);
  }
  
  void change(GameState gs){
    this.unload();
    currentState = gs;
  }
  
  void unload(){
    mouseDistributor.reset();
  }
}

/**
The main game class
**/
class Game extends GameState{
  
  Grid grid;
  
  Game(){
    grid = new Grid(51, 51);
    grid.load(dungeonGenerator);
    
    SmallFancyButton b = new SmallFancyButton(16, 16, YELLOW, "Test", textures.get("music.trumpet"));
    
    addRender(grid, b);
    addUpdate(grid);
    addMouse(grid);
  }
  
  void update(){
    super.update();
    
    if(keys.isDownOnce(ENTER)) grid.load(dungeonGenerator);
    if(keys.isDownOnce(UP)) grid.focusActor.up();
    if(keys.isDownOnce(DOWN)) grid.focusActor.down();
    if(keys.isDownOnce(LEFT)) grid.focusActor.left();
    if(keys.isDownOnce(RIGHT)) grid.focusActor.right();
  }
}
/**
The main menu class
**/
class MainMenu extends GameState{
  
  MainMenu(){
    //New game button
    BigTextButton newGameButton = new BigTextButton(0, 112, GREEN, "New Game");
    newGameButton.addMouseHandler(new MouseHandler(){
      void mouseDown(int x, int y){
        currentState.change(new Game());
      }
    });
    newGameButton.centerX();
    
    //Close button
    BigTextButton closeButton = new BigTextButton(0, 176, RED, "Exit");
    closeButton.addMouseHandler(new MouseHandler(){
      void mouseDown(int x, int y){
        exit();
      }
    });
    closeButton.centerX();
    
    int bottomY = stage.height - 40;
    final FancyButton musicButton = new FancyButton(8, bottomY, YELLOW, "Music: ON", textures.get("music.trumpet"));
    musicButton.addMouseHandler(new MouseHandler(){
      void mouseDown(int x, int y){
        musicButton.text = (musicButton.text == "Music: ON") ? "Music: OFF" : "Music: ON";
        musicButton.recalc = true;
      }
    });
    
    addRender(newGameButton, closeButton, musicButton);
    addMouse(newGameButton, closeButton, musicButton);
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