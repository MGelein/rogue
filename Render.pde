class RenderAble{
  boolean updated = true;//Holds whether this object needs to be re-rendered
  
  void render(PGraphics g){}
}
class MouseAble extends RenderAble{
  private ArrayList<MouseHandler> mouseHandlers = new ArrayList<MouseHandler>();
  void mouseMove(int x, int y){
    for(MouseHandler m : mouseHandlers) m.mouseMove(x, y);
  };
  void mouseDown(int x, int y){
    for(MouseHandler m : mouseHandlers) m.mouseDown(x, y);
  };
  void mouseUp  (int x, int y){
    for(MouseHandler m : mouseHandlers) m.mouseUp(x, y);
  };
  void addMouseHandler(MouseHandler handler){
    mouseHandlers.add(handler);
  }
  void removeMouseHandler(MouseHandler handler){
    mouseHandlers.remove(handler);
  }
}
class MouseHandler{
  void mouseMove(int x, int y){};
  void mouseDown(int x, int y){};
  void mouseUp  (int x, int y){};
}