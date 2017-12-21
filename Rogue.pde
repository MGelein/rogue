int SCL = 2;
int SIZE = 16;

Registry registry = new Registry();
GameState currentState;
Game game;
Textures textures;

PGraphics stage;

void setup(){
  size(1280, 720);
  noSmooth();
  background(0);
  
  //Load the ini files for the textures (spritesheet indexing)
  registry.load("textures.ini", "tex");
  
  //Create the canvas to draw on
  stage = createGraphics(floor(width / SCL), floor(height / SCL));
  
  //Load all texture objects
  textures = new Textures();
  
  //The global game object
  game = new Game();
  currentState = game;
  
  //Load a font to use for all drawing of text (to set it we must be drawing)
  stage.beginDraw();
  stage.textFont(createFont("Dawnlike/GUI/SDS_8x8.ttf", 8));
  stage.endDraw();
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
GameState classs
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
MOUSE Input handling
**/

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