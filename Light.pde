class Light{
  color c;
  int range;
  ArrayList<LightPoint> points;
  Int2D pos;
  Grid grid;
  
  Light(Int2D pos, color col, int r, Grid g){
    c = col;
    range = r;
    grid = g;
    setPos(pos);
  }
  
  void setPos(Int2D p){
    this.pos = p.copy();
    calculatePoints();
  }
  
  private void calculatePoints(){    
    ArrayList<LightPoint> open = new ArrayList<LightPoint>();
    open.add(new LightPoint(this, pos, range));
    points = new ArrayList<LightPoint>();
    
    while(open.size() > 0){
      ArrayList<LightPoint> neighbors = new ArrayList<LightPoint>();
      for(int i = open.size() - 1; i >= 0; i--){
        neighbors.addAll(open.get(i).getViableNeighbors());
        //Add it to the finished points list;
        points.add(open.remove(i));
      }
      //Now make sure none of the neighbors overlap with open or points
      for(LightPoint n : neighbors){
        boolean found = false;
        for(LightPoint l : points){
          if(l.pos.x == n.pos.x && l.pos.y == n.pos.y){
            found = true;
            break;
          }
        }
        for(int i = 0; i < open.size(); i++){
          LightPoint l = open.get(i);
          if(l.pos.x == n.pos.x && l.pos.y == n.pos.y){
            found = true;
            if(l.str < n.str){//better light value, replace the old one
              open.set(i, n);
              found = true;
            }
          }
        }
        if(! found){
          open.add(n);
        }
      }
    }
  }
  
  void calculate(){
    //For each of the lightpoints
    for(LightPoint l : points){
      color origColor = grid.get(l.pos.x, l.pos.y).lighting;
      color targetColor = c;
      origColor = lerpColor(origColor, targetColor, l.getStrength() / 2);
      grid.get(l.pos.x, l.pos.y).lighting = origColor;
    }
  }
}

class LightPoint{
  Int2D pos;
  int str;
  Light l;
  boolean propagated = false;//If light has already spread from this point
  
  LightPoint(Light parent, Int2D pos, int strength){
    this.pos = pos;
    this.str = strength;
    l = parent;
  }
  
  float getStrength(){
    return (str + 0.001f) / (l.range + 0.001f);
  }
  
  ArrayList<LightPoint> getViableNeighbors(){
    propagated = true;
    //Initialize the list of possible neighbors
    ArrayList<LightPoint> pts = new ArrayList<LightPoint>();
    //Don't propagate if i'm an unwalkable tile and not source of the light, return empty list
    if(!l.grid.get(pos.x, pos.y).walkable && l.range != str || str == 1) return pts;
    
    //Add all possible directions
    pts.add(new LightPoint(l, new Int2D(pos.x, pos.y - 1), str - 1));
    pts.add(new LightPoint(l, new Int2D(pos.x - 1, pos.y), str - 1));
    pts.add(new LightPoint(l, new Int2D(pos.x + 1, pos.y), str - 1));
    
    //Only allow downward light when it is walkable
    if(l.grid.get(pos.x, pos.y + 1).walkable){
      pts.add(new LightPoint(l, new Int2D(pos.x, pos.y + 1), str - 1));
    }
    
    
    //Return the populated list
    return pts;
  }
}