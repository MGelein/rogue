int SCL = 2;
int SIZE = 16;
int RED = 0, HP = 0;
int BLUE = 1, MANA = 1;
int GREEN = 2, STAMINA = 2;
int YELLOW = 3, GOLD = 3;

Registry registry = new Registry();
GameState currentState;
Textures textures;

PGraphics stage;

void setup(){
  size(1280, 720);
  //fullScreen();
  noSmooth();
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
  
  //Now that we've drawn to the buffer, render it to screen
  image(stage, 0, 0, width, height);
}

/**
RENDER & UPDATE interface
**/
interface IRender{
  void render(PGraphics g);
}
interface IUpdate{
  void update();
}

/**
MOUSE Input handling
**/

interface IMouse{
  boolean isHighlighted();
  void mouseDown();
  void mouseUp();
  void addMouseHandler(MouseHandler h);
}
class MouseHandler{
  void mouseDown(int button){};
  void mouseUp(int button){};
}
void mousePressed(){ currentState.mouseDown(floor(mouseX / SCL), floor(mouseY / SCL)); }
void mouseReleased(){ currentState.mouseUp(floor(mouseX / SCL), floor(mouseY / SCL)); }

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