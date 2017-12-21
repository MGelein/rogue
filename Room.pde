class Room{
  ArrayList<GridObject> objects = new ArrayList<GridObject>();
  
  Room(String url){
    XML data = loadXML(url);
    XML[] layers = data.getChildren("layer");
    for(XML l : layers) parseLayer(l);
  }
  
  void parseLayer(XML layer){
    XML[] cellsXML = layer.getChildren("cell");
    for(XML c : cellsXML) parseCell(c);
  }
  
  void parseCell(XML cell){
    int x = cell.getInt("x");
    int y = cell.getInt("y");
    GridObject go = new GridObject(x, y, cell);
    go.setTexture(cell.getContent());
    objects.add(go);
  }
}