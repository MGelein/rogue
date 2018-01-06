class Textures{
  HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
  HashMap<String, AnimatedSheet> animSheets = new HashMap<String, AnimatedSheet>();
  PImage mainMenuBG;
  //Holds the theme modifiers
  Theme theme = new Theme();
  private Int2D currentTheme = theme.none;
  
  Textures(){
    //Load the bgImage
    mainMenuBG = loadImage("img/mainmenu.jpg");
    
    //Objects
    animSheets.put("decor", new AnimatedSheet(SIZE, "Objects/Decor0.png", "Objects/Decor1.png"));
    animSheets.put("door", new AnimatedSheet(SIZE, "Objects/Door0.png", "Objects/Door1.png"));
    animSheets.put("effect", new AnimatedSheet(SIZE, "Objects/Effect0.png", "Objects/Effect1.png"));
    spriteSheets.put("fence", new SpriteSheet(SIZE, loadImage("Dawnlike/Objects/Fence.png")));
    spriteSheets.put("floor", new SpriteSheet(SIZE, loadImage("Dawnlike/Objects/Floor.png")));
    animSheets.put("ground", new AnimatedSheet(SIZE, "Objects/Ground0.png", "Objects/Ground1.png"));
    animSheets.put("hill", new AnimatedSheet(SIZE, "Objects/Hill0.png", "Objects/Hill1.png"));
    animSheets.put("map", new AnimatedSheet(SIZE, "Objects/Map0.png", "Objects/Map1.png"));
    animSheets.put("ore", new AnimatedSheet(SIZE, "Objects/Ore0.png", "Objects/Ore1.png"));
    animSheets.put("pit", new AnimatedSheet(SIZE, "Objects/Pit0.png", "Objects/Pit1.png"));
    spriteSheets.put("tile", new SpriteSheet(SIZE, loadImage("Dawnlike/Objects/Tile.png")));
    animSheets.put("trap", new AnimatedSheet(SIZE, "Objects/Trap0.png", "Objects/Trap1.png"));
    animSheets.put("tree", new AnimatedSheet(SIZE, "Objects/Tree0.png", "Objects/Tree1.png"));
    spriteSheets.put("wall", new SpriteSheet(SIZE, loadImage("Dawnlike/Objects/Wall.png")));
    
    //GUI
    animSheets.put("gui", new AnimatedSheet(SIZE, "GUI/GUI0.png", "GUI/GUI1.png"));
    
    //Items
    spriteSheets.put("ammo", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Ammo.png")));
    spriteSheets.put("amulet", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Amulet.png")));
    spriteSheets.put("armor", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Armor.png")));
    spriteSheets.put("book", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Book.png")));
    spriteSheets.put("boot", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Boot.png")));
    animSheets.put("chest", new AnimatedSheet(SIZE, "Items/Chest0.png", "Items/Chest1.png"));
    spriteSheets.put("flesh", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Flesh.png")));
    spriteSheets.put("food", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Food.png")));
    spriteSheets.put("glove", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Glove.png")));
    spriteSheets.put("hat", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Hat.png")));
    spriteSheets.put("light", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Light.png")));
    spriteSheets.put("long_weapon", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/LongWep.png")));
    spriteSheets.put("medium_weapon", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/MedWep.png")));
    spriteSheets.put("short_weapon", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/ShortWep.png")));
    spriteSheets.put("money", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Money.png")));
    spriteSheets.put("music", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Music.png")));
    spriteSheets.put("potion", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Potion.png")));
    spriteSheets.put("ring", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Ring.png")));
    spriteSheets.put("rock", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Rock.png")));
    spriteSheets.put("scroll", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Scroll.png")));
    spriteSheets.put("shield", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Shield.png")));
    spriteSheets.put("tool", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Tool.png")));
    spriteSheets.put("wand", new SpriteSheet(SIZE, loadImage("Dawnlike/Items/Wand.png")));
    
    //Characters
    animSheets.put("aquatic", new AnimatedSheet(SIZE, "Characters/Aquatic0.png", "Characters/Aquatic1.png"));
    animSheets.put("avian", new AnimatedSheet(SIZE, "Characters/Avian0.png", "Characters/Avian1.png")); 
    animSheets.put("cat", new AnimatedSheet(SIZE, "Characters/Cat0.png", "Characters/Cat1.png"));
    animSheets.put("demon", new AnimatedSheet(SIZE, "Characters/Demon0.png", "Characters/Demon1.png"));
    animSheets.put("dog", new AnimatedSheet(SIZE, "Characters/Dog0.png", "Characters/Dog1.png"));
    animSheets.put("elemental", new AnimatedSheet(SIZE, "Characters/Elemental0.png", "Characters/Elemental1.png"));
    animSheets.put("humanoid", new AnimatedSheet(SIZE, "Characters/Humanoid0.png", "Characters/Humanoid1.png"));
    animSheets.put("misc", new AnimatedSheet(SIZE, "Characters/Misc0.png", "Characters/Misc1.png"));
    animSheets.put("pest", new AnimatedSheet(SIZE, "Characters/Pest0.png", "Characters/Pest1.png"));
    animSheets.put("plant", new AnimatedSheet(SIZE, "Characters/Plant0.png", "Characters/Plant1.png"));
    animSheets.put("player", new AnimatedSheet(SIZE, "Characters/Player0.png", "Characters/Player1.png"));
    animSheets.put("quadraped", new AnimatedSheet(SIZE, "Characters/Quadraped0.png", "Characters/Quadraped1.png"));
    animSheets.put("reptile", new AnimatedSheet(SIZE, "Characters/Reptile0.png", "Characters/Reptile1.png"));
    animSheets.put("rodent", new AnimatedSheet(SIZE, "Characters/Rodent0.png", "Characters/Rodent1.png"));
    animSheets.put("slime", new AnimatedSheet(SIZE, "Characters/Slime0.png", "Characters/Slime1.png"));
    animSheets.put("undead", new AnimatedSheet(SIZE  , "Characters/Undead0.png", "Characters/Undead1.png"));
  }
  
  /** Sets the texture coord modifier. Set to THEME_DEFAULT to reset*/
  void setThemeModifier(Int2D mod){
    currentTheme = mod.copy();
  }
  
  /** Looks for the provided string without a modifier*/
  PImage get(String s){
    return get(s, currentTheme);
  }
  
  /** Tries to parse the provided string as a texture identifier*/
  PImage get(String s, Int2D themeModifier){
    if(isAnimated(s)) return get(s, 0, themeModifier);
    return spriteSheets.get(s.substring(0, s.indexOf(".")).trim().toLowerCase()) //get sheet name
                        .get(registry.getInt2D("tex." + s).copy().add(themeModifier));                     //get sheet coords
  }
  
  /** If the texture described by the provided id is in the animated list or not */
  boolean isAnimated(String s){
    if(s.indexOf(".") == -1) {
      println("Invalid texture name, no sheet name: " + s);
      return false;
    }
    return animSheets.containsKey(s.substring(0, s.indexOf(".")).trim().toLowerCase());
  }
  
  /** Looks for animated spriteSheet with default theme (0,0)*/
  PImage get(String s, int frame){
    return get(s, frame, currentTheme);
  }
  
  /** Looks for animated spritesheets to find a match*/
  PImage get(String s, int frame, Int2D themeModifier){
    return animSheets.get(s.substring(0, s.indexOf(".")).trim().toLowerCase()) //get sheet name
                        .get(registry.getInt2D("tex." + s).copy().add(themeModifier), frame);            //get sheet coords
  }
  
  boolean exists(String s){
    if(s.indexOf(".") == -1) {
      println("Invalid texture name, no sheet name: " + s);
      return false;
    }
    String sheetName = s.substring(0, s.indexOf(".")).trim().toLowerCase();
    if(animSheets.containsKey(sheetName)|| spriteSheets.containsKey(sheetName)){
      if(registry.get("tex." + s) != null) return true; 
    }
    return false;
  }
}

class AnimatedSheet{
  ArrayList<SpriteSheet> sheets = new ArrayList<SpriteSheet>();
  
  AnimatedSheet(int tileSize, String... sheetUrls){
    for(String url : sheetUrls){
      addSheet(new SpriteSheet(tileSize, loadImage("Dawnlike/" + url)));
    }
  }
  
  PImage get(Int2D pos, int frame){
    return sheets.get(frame % sheets.size()).get(pos);
  }
  
  PImage get(int index, int frame){
    return sheets.get(frame % sheets.size()).get(index);
  }
  
  PImage get(int x, int y, int frame){
    return sheets.get(frame % sheets.size()).get(x, y);
  }
  
  void addSheet(SpriteSheet s){
    sheets.add(s);
  }
  
  void removeSheet(SpriteSheet s){
    sheets.remove(s);
  }
}

class SpriteSheet{
  PImage sheet;
  PImage[] sprites;
  int size;
  int cols;
  int rows;
  
  SpriteSheet(int tileSize, PImage sheet){
    load(tileSize, sheet);
  }
  
  void load(int tileSize, PImage sheet){
    this.size = tileSize;
    this.sheet = sheet;
    
    cols = floor(sheet.width / size);
    rows = floor(sheet.height / size);
    
    sprites = new PImage[cols * rows];
    for(int i = 0; i < sprites.length; i++){
      int x = i % cols;
      int y = floor(i / cols);
      sprites[i] = sheet.get(x * size, y * size, size, size);
    }
  }
  
  PImage get(int index){
    return sprites[index % sprites.length]; 
  }
  
  PImage get(int x, int y){
    return get(x + y * cols);
  }
  
  PImage get(Int2D pos){
    return get(pos.x, pos.y);
  }
}

class Theme{
  final Int2D none = new Int2D();
  
  //brick
  final Int2D brick_light = new Int2D();
  final Int2D brick = new Int2D(0, 3);
  final Int2D brick_dark = new Int2D(0, 6);
  final Int2D brick_darker = new Int2D(0, 9);
  
  //sandstone
  final Int2D sandstone_light = new Int2D(0, 12);
  final Int2D sandstone = new Int2D(0, 15);
  final Int2D sandstone_dark = new Int2D(0, 18);
  final Int2D sandstone_darker = new Int2D(0, 21);
  
  //wood
  final Int2D wood_light = new Int2D(7, 0);
  final Int2D wood = new Int2D(7, 3);
  final Int2D wood_dark = new Int2D(7, 6);
  final Int2D wood_darker = new Int2D(7, 9);
  
  //rock
  final Int2D rock_light = new Int2D(7, 12);
  final Int2D rock = new Int2D(7, 15);
  final Int2D rock_dark = new Int2D(7, 18);
  final Int2D rock_darker = new Int2D(7, 21);
  
  //temple
  final Int2D temple_light = new Int2D(14, 0);
  final Int2D temple = new Int2D(14, 3);
  final Int2D temple_dark = new Int2D(14, 6);
  final Int2D temple_darker = new Int2D(14, 9);
  
  //dirt
  final Int2D dirt_light = new Int2D(14, 12);
  final Int2D dirt = new Int2D(14, 15);
  final Int2D dirt_dark = new Int2D(14, 18);
  final Int2D dirt_darker = new Int2D(14, 21);
  
  //liquid
  final Int2D water_brick = new Int2D(0, 0);
  final Int2D water_dirt = new Int2D(0, 2);
  final Int2D water_wood = new Int2D(0, 4);
  final Int2D poison_brick = new Int2D(0, 12);
  final Int2D poison_dirt = new Int2D(0, 14);
  final Int2D poison_wood = new Int2D(0, 16);
}