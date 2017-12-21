class Ini{
  HashMap<String, String> keyValue = new HashMap<String, String>();
  
  Ini(String url){
    parse(url);
  }
  
  /** Parses the provided file, empties previous settings*/
  void parse(String url){
    String[] lines = loadStrings(url);
    String prefix = "";
    //Go through each lie
    for(String line : lines){
      line = line.trim();
      if(line.length() == 0) continue;//empty line
      else if(line.charAt(0) == '#') continue; //comment
      else if(line.charAt(0) == '['){//New category, this is now the active prefix
        prefix = line.substring(1, line.length() - 1) + ".";
      }else if(line.indexOf("=") != -1){//If this is a key value pair
        String[] parts = line.split("=");
        keyValue.put(prefix + parts[0], parts[1]);
      }
    }
  }
  
  String get(String k){
    return keyValue.get(k);
  }
  
  float getFloat(String k){
    return float(get(k));
  }
  
  boolean getBoolean(String k){
    return boolean(get(k));
  }
  
  int getInteger(String k){
    return int(get(k));
  }
  
  Int2D getInt2D(String k){
    return new Int2D(get(k));
  }
}

/**
Contains multiple ini files, all registered under their own prefix
**/
class Registry{
  HashMap<String, Ini> files = new HashMap<String, Ini>();
  
  /** Loads the provided url as a Ini file and adds it as the provided name to the file registry*/
  void load(String url, String prefix){
    files.put(prefix, new Ini(url));
  }
  
  String get(String k){
    return files.get(getFileName(k)).get(getQuery(k));
  }
  
  float getFloat(String k){
    return files.get(getFileName(k)).getFloat(getQuery(k));
  }
  
  Boolean getBoolean(String k){
    return files.get(getFileName(k)).getBoolean(getQuery(k));
  }
  
  int getInteger(String k){
    return files.get(getFileName(k)).getInteger(getQuery(k));
  }
  
  Int2D getInt2D(String k){
    return files.get(getFileName(k)).getInt2D(getQuery(k));
  }
  
  private String getQuery(String q){
    return q.substring(q.indexOf(".") + 1);
  }  
  private String getFileName(String q){
    return q.substring(0, q.indexOf("."));
  }
}