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
  }
  
  void interact(){
    if(locked) return;
    open = !open;
    animate();
    
    //Set cell walkable
    parentCell.walkable = open;
    
    //Also update all lighting now the door has opened
    parentCell.grid.lightingUpdate = true;
  }
}