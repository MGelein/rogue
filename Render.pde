class RenderAble{
  boolean highlighted = false;
  
  void render(PGraphics g){}
}
class MouseAble extends RenderAble{
  protected ArrayList<MouseHandler> mouseHandlers = new ArrayList<MouseHandler>();
  void mouseMove(int x, int y){
    for(MouseHandler m : mouseHandlers) m.mouseMove(x, y);
  };
  void mouseDown(int x, int y){
    if(! highlighted) return;
    for(MouseHandler m : mouseHandlers) m.mouseDown(x, y);
  };
  void mouseUp  (int x, int y){
    if(! highlighted) return;
    for(MouseHandler m : mouseHandlers) m.mouseUp(x, y);
  };
  void addMouseHandler(MouseHandler handler){
    handler.target = this;
    mouseHandlers.add(handler);
  }
  void removeMouseHandler(MouseHandler handler){
    mouseHandlers.remove(handler);
  }
}
class MouseHandler{
  MouseAble target;
  void mouseMove(int x, int y){};
  void mouseDown(int x, int y){};
  void mouseUp  (int x, int y){};
}