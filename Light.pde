class Light{
  color c;
  int range;
  ArrayList<LightPoint> points;
  Int2D pos;
  Grid grid;
  Light(Int2D pos, color col, int r){
    c = col;
    range = r;
    setPos(pos);
  }
  
  void setPos(Int2D p){
    this.pos = p.copy();
    calculatePoints();
  }
  
  private void calculatePoints(){
    points = new ArrayList<LightPoint>();
    points.add(new LightPoint(this, pos, range));
  }
  
  boolean isInLight(Int2D p){
    for(LightPoint lp : points){
      if(lp.pos.x == p.y && lp.pos.y == p.y) return true;
    }
    return false;
  }
  
  void calculate(){
    
  }
}

class LightPoint{
  Int2D pos;
  float str;
  Light l;
  
  LightPoint(Light parent, Int2D pos, float strength){
    this.pos = pos;
    this.str = strength;
  }
}