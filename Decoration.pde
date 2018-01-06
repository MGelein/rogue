/** Gridobject that holds a light source. Parent for all lightsource objects*/
class LightSource extends GridObject{
  Light lightObject;
  boolean lit = false;
  LightSource(int x, int y, GridCell parent, String s){
    super(x, y, parent);
    parse(s);
    light(lights.get(s));
  }
  
  void light(LightTemplate template){
    extinguish();
    lightObject = new Light(new Int2D(x, y), template.c, template.str, parentCell.grid);
    parentCell.grid.addLight(lightObject);
    lit = true;
    //If it was previously extinguished
    if(textures.exists(texName.replaceAll("_extinguished", ""))){
      setTexture(texName.replaceAll("_extinguished", ""));
    }
  }
  
  void interact(){
    toggle();
  }
  
  void light(){
    //Check if we have already lit the object before
    if(lightObject == null){
      println("LightObject is undefined");
      return;
    }
    //Else use previous parameters;
    light(new LightTemplate(lightObject.c, lightObject.range));
  }
  
  void extinguish(){
    parentCell.grid.removeLight(lightObject);
    lit = false;
    
    //If extinguished texture exists, set it to it
    if(textures.exists(texName+"_extinguished")){
      setTexture(texName + "_extinguished", false);
    }
  }
  
  void toggle(){
    if(lit) extinguish();
    else light();
  }
}

class Door extends GridObject{
  boolean open = false;
  boolean locked = false;
  Door(int x, int y, GridCell parent, String s){
    super(x, y, parent);
    parse(s);
    if(s.indexOf("locked") != -1) locked = true;
    setTexture(texName, false);
    walkable = false; 
    opaque = true;
  }
  
  void interact(){
    if(locked) return;
    open = !open;
    animate();
    
    //Set cell walkable
    walkable = open;
    opaque = !open;
    parentCell.calcWalkable();
    parentCell.calcOpaque();
    
    //Also update all lighting now the door has opened
    parentCell.grid.lightingUpdate = true;
  }
}

class Bones extends GridObject{
  String[] boneTypes = new String[]{
  "decor.skull_white", "decor.skull_white_long",
  "decor.bones_white", "decor.bones_white_long"};
  
  Bones(int x, int y, GridCell parent){
    super(x, y, parent);
    parse(random(boneTypes));
    opaque = walkable = false;
  }
  
  void interact(){
    walkable = true;
    opaque = false;
    texName = null;
    tex = null;
    parentCell.calcWalkable();
    parentCell.calcOpaque();
    parentCell.remove(this);
  }
}