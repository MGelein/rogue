class Button implements IMouse, IRender{
  PImage[] normalTiles = new PImage[10];
  PImage[] highlightTiles = new PImage[10];
  Int2D pos;
  Int2D dim = new Int2D(1, 1);
  
  Button(int x, int y, int type){
    pos = new Int2D(x, y);
    
    if(type == BLUE) fetchTiles("black_blue", "blue_orange");
    else if(type == GREEN) fetchTiles("black_green", "green_red");
    else if(type == YELLOW) fetchTiles("black_yellow", "yellow_black");
    else fetchTiles("black_red", "red_green");
  }
  
  void fetchTiles(String normal, String highlight){
    normalTiles[0] = textures.get("gui.box_" + normal + "_tl");
    normalTiles[1] = textures.get("gui.box_" + normal + "_tm");
    normalTiles[2] = textures.get("gui.box_" + normal + "_tr");
    normalTiles[3] = textures.get("gui.box_" + normal + "_ml");
    normalTiles[4] = textures.get("gui.box_" + normal + "_mm");
    normalTiles[5] = textures.get("gui.box_" + normal + "_mr");
    normalTiles[6] = textures.get("gui.box_" + normal + "_bl");
    normalTiles[7] = textures.get("gui.box_" + normal + "_bm");
    normalTiles[8] = textures.get("gui.box_" + normal + "_br");
    normalTiles[9] = textures.get("gui.box_" + normal);
    
    highlightTiles[0] = textures.get("gui.box_" + highlight + "_tl");
    highlightTiles[1] = textures.get("gui.box_" + highlight + "_tm");
    highlightTiles[2] = textures.get("gui.box_" + highlight + "_tr");
    highlightTiles[3] = textures.get("gui.box_" + highlight + "_ml");
    highlightTiles[4] = textures.get("gui.box_" + highlight + "_mm");
    highlightTiles[5] = textures.get("gui.box_" + highlight + "_mr");
    highlightTiles[6] = textures.get("gui.box_" + highlight + "_bl");
    highlightTiles[7] = textures.get("gui.box_" + highlight + "_bm");
    highlightTiles[8] = textures.get("gui.box_" + highlight + "_br");
    highlightTiles[9] = textures.get("gui.box_" + highlight);
  }
  
  void render(PGraphics g){
    //Special case for 1x1 buttons
    if(dim.x == 1 && dim.y == 1){
      renderTile(g, 9, 0, 0);
      return;
    }
    
    //In all other cases, check for each location how to build the button
    for(int x = 0; x < dim.x; x++){
      for(int y = 0; y < dim.y; y++){
        if(x == 0 && y == 0) renderTile(g, 0, x, y);                    //TL
        else if(x == 0 && y == dim.y - 1) renderTile(g, 6, x, y);       //BL
        else if(x == dim.x - 1 && y == 0) renderTile(g, 2, x, y);       //TR
        else if(x == dim.x - 1 && y == dim.y - 1) renderTile(g, 8, x, y);//BR
        else if(x == 0) renderTile(g, 3, x, y);                         //ML
        else if(x == dim.x - 1) renderTile(g, 5, x, y);                 //MR
        else if(y == 0) renderTile(g, 1, x, y);                         //TM
        else if(y == dim.y - 1) renderTile(g, 7, x, y);                 //BM
        else renderTile(g, 4, x, y);                                    //MM
      }
    }
  }
  
  boolean isHighlighted(){
    float x = mouseX / SCL;
    float y = mouseY / SCL;
    return x > pos.x && x < pos.x + (dim.x * SIZE) && y > pos.y && y < pos.y + (dim.y * SIZE);
  }
  
  void renderTile(PGraphics g, int tileIndex, int x, int y){
    g.image(
      (isHighlighted() ? highlightTiles[tileIndex] : normalTiles[tileIndex]),
      pos.x + (x * SIZE),
      pos.y + (y * SIZE)
    );
  }
  
  void mouseDown(){};
  void mouseUp(){};
}

/**
Button with text on it
**/
class TextButton extends Button{
  String text;
  boolean recalc = false;
  PVector off = new PVector();//offset of text to button top left
  
  TextButton(int x, int y, int type, String text){
    super(x, y, type);
    this.text = text;
    recalc = true;
  }
  
  void updateDim(PGraphics g){
    recalc = false;
    dim.x = floor((g.textWidth(text) / SIZE) + 2);
    off.x = ((dim.x * SIZE) - g.textWidth(text)) * 0.5f;
    off.y = (dim.y * 0.66f * SIZE);
    
  }
  
  void render(PGraphics g){
    super.render(g);
    //Check if we need to recalc width
    if(recalc) updateDim(g);
    
    if(isHighlighted()){ //If highlighted, also draw a text shadow
      g.fill(0);
      g.text(text, pos.x + off.x + 1, pos.y + off.y + 1);
      g.fill(255);
    }
    g.text(text, pos.x + off.x, pos.y + off.y);
  }
}

/** Works for the icons used in the game (8 x 8)*/
class IconButton extends Button{
  PImage img;  
  
  IconButton(int x, int y, int type, PImage image){
    super(x, y, type);
    img = image;
  }
  
  void render(PGraphics g){
    super.render(g);    
    g.image(img, pos.x - 4, pos.y - 4);
  }
}

/** A tile button also resizes its image to 8x8*/
class TileButton extends Button{
  PImage img;
  TileButton(int x, int y, int type, PImage image){
    super(x, y, type);
    img = image;
    img.resize(8, 8);
  }
  
  void render(PGraphics g){
    super.render(g);
    g.image(img, pos.x + 4, pos.y + 4);
  }
}