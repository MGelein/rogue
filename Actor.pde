class Actor extends GridObject{
  Actor(int x, int y, GridCell cell){
    super(x, y, cell);
  }
  
  /*Every actor can act*/
  void act(){};
}