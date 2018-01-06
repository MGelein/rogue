class Actor extends GridObject{
  Actor(int x, int y, GridCell cell){
    super(x, y, cell);
    opaque = false;
  }
  
  /*Every actor can act*/
  void act(){};
  
  /** Every actor can move to locations*/
  void moveTo(int x, int y){
    //First check if it is possible to moveTo
    if(!parentCell.grid.get(x, y).walkable) return;
    
    //Remove from previous parent
    parentCell.remove(this);
    this.x = x;
    this.y = y;
    //Get new parent and re-add
    parentCell = parentCell.grid.get(this.x, this.y);
    parentCell.add(this);
  }
  
  void left(){moveTo(x - 1, y);}
  void right(){moveTo(x + 1, y);}
  void up(){moveTo(x, y - 1);}
  void down(){moveTo(x, y + 1);}
}
class Player extends Actor{
  Player(int x, int y, GridCell parent){
    super(x, y, parent);
    parse("player.ranger");
    moveTo(x, y);
    parent.grid.player = this;
  }
  
  void moveTo(int newX, int newY){
    super.moveTo(newX, newY);
    //Now center cam on new position
    parentCell.grid.viewPoint.set(this.x, this.y);
  }
  
  void update(){
    super.update();
  }
}