/** Gridobject that holds a light source. Parent for all lightsource objects*/
class LightSource extends GridObject{
  private HashMap<String, LightTemplate> templates = new HashMap<String, LightTemplate>();
  {
    templates.put("light.lantern", new LightTemplate(color(25, 255, 180), 6));
  }
  Light lightObject;
  boolean lit = false;
  LightSource(int x, int y, GridCell parent, String s){
    super(x, y, parent);
    parse(s);
    light(templates.get(s));
  }
  
  void light(LightTemplate template){
    extinguish();
    lightObject = new Light(new Int2D(x, y), template.c, template.str, parentCell.grid);
    parentCell.grid.addLight(lightObject);
    lit = true;
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
/** Used to define lightcolor and lightrange template*/
class LightTemplate{
  color c;
  int str;
  LightTemplate(color col, int strength){
    c = col;
    str = strength;
  }
}