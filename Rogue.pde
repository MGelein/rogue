int SCL = 2;
int SIZE = 16;
int RED = 0, HP = 0;
int BLUE = 1, MANA = 1;
int GREEN = 2, STAMINA = 2;
int YELLOW = 3, GOLD = 3;

boolean DRAW_FPS = false;  
float FRAME_RATE = 60;
boolean FULLSCREEN = true;

MouseDistributor mouseDistributor = new MouseDistributor();
Registry registry = new Registry();
GameState currentState;
Textures textures;
DungeonGenerator dungeonGenerator = new DungeonGenerator();

PGraphics stage;

void settings(){
  //First load the game ini file
  registry.load("game.ini", "game");
  FULLSCREEN = registry.getBoolean("game.fullscreen");
  DRAW_FPS = registry.getBoolean("game.show_fps");  
 
  if(FULLSCREEN){
    fullScreen();
  }else{
    size(registry.getInteger("game.window_width"), registry.getInteger("game.window_height"));
  }
  //No smoothing to allow pixelated look
  noSmooth();
}

void setup(){
  //Black background while waiting to load
  background(0);
  
  //Load the ini files for the textures (spritesheet indexing)
  registry.load("textures.ini", "tex");
 
  //Create the canvas to draw on
  stage = createGraphics(floor(1280 / 2), floor(720 / 2));
  SCL = width / stage.width;
  
  //Load all texture objects
  textures = new Textures();
  
  //Load a font to use for all drawing of text (to set it we must be drawing)
  stage.beginDraw();
  stage.textFont(createFont("Dawnlike/GUI/SDS_8x8.ttf", 8));
  stage.endDraw();
  //Set size for frameRate font
  textSize(16);
  
  //Finally set the current game state to the main menu
  currentState = new MainMenu();
}

void draw(){
  //Do the rendering
  stage.beginDraw();
  stage.background(0);
  currentState.render(stage);
  stage.endDraw();
  
  //Do the updating
  currentState.update();
  
  //Update the mouse events
  mouseDistributor.update();
  
  //Now that we've drawn to the buffer, render it to screen
  image(stage, 0, 0, width, height);
  
  //Draw FPS during development
  if(DRAW_FPS){
    fill(0);
    text(floor(FRAME_RATE) + " fps", 2, 16);
    fill(255, 255, 0);
    text(floor(FRAME_RATE) + " fps", 3, 17);
    FRAME_RATE -= (FRAME_RATE - frameRate) * 0.1f;
  }
}

/**
RENDER & UPDATE interface
**/
interface IUpdate{
  void update();
}

/**
MOUSE Input handling
**/
class MouseDistributor{
  private ArrayList<MouseAble> subscribers = new ArrayList<MouseAble>();
  float oX = 0;
  float oY = 0;
  boolean reset = false;
  
  void update(){
    if(mouseX != oX || mouseY != oY){
      for(MouseAble m : subscribers){
        m.mouseMove(floor(mouseX / SCL), floor(mouseY / SCL));
      }
      oX = mouseX;
      oY = mouseY;
    }
    if(reset) subscribers.clear();
  }
  
  void mouseDown(){
    for(MouseAble m : subscribers){
      m.mouseDown(floor(mouseX / SCL), floor(mouseY / SCL));
    }
  }
  
  void mouseUp(){
    for(MouseAble m : subscribers){
      m.mouseUp(floor(mouseX / SCL), floor(mouseY / SCL));
    }
  }
  
  void reset(){
    reset = true;
  }
  
  void add(MouseAble m){ subscribers.add(m);}
  void remove(MouseAble m){ subscribers.remove(m);}
}
void mousePressed(){ mouseDistributor.mouseDown();}
void mouseReleased(){ mouseDistributor.mouseUp();}

/**
KEY HANDLING
**/
Keys keys = new Keys();
class Keys{
  boolean[] keys = new boolean[256];
  
  void setState(int code, boolean state){
    keys[code] = state;
  }
  
  boolean isDown(int code){
    return keys[code] == true;
  }
  
  boolean isDownOnce(int code){
    if(keys[code] == true){
      keys[code] = false;
      return true;
    }
    return false;
  }
}

void keyPressed(){
  keys.setState(keyCode, true);
}

void keyReleased(){
  keys.setState(keyCode, false);
}