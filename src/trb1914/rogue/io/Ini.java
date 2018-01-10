package trb1914.rogue.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import trb1914.rogue.Rogue;
import trb1914.rogue.math.Int2D;

/**
 * This class handles the loading and parsing of .ini files
 * @author trb1914
 */
public class Ini {
	
	/** Holds the data values ordered by key*/
	private HashMap<String, String> map = new HashMap<String, String>();
	/** A list of Strings that are the different category prefixes found in this file*/
	public ArrayList<String> categories = new ArrayList<String>();
	
	/**
	 * Creates a new Ini object and tries to load and parse the file
	 * that is described by the provided url
	 * @param url the location of the ini file to read
	 */
	public Ini(String url){
		parse(url);
	}
	
	/**
	 * Tries to parse and load the ini file that is described by the
	 * provided url
	 * @param url the location of the ini file to read
	 */
	public void parse(String url) {
		//Make sure the map is clear
		map.clear();
		//Load file from disk
		String [] lines = Rogue.loadStrings(new File("./data/" + url));
		//The prefix used for a category in a .ini file
		String prefix = "";
		//Then read each line
		for(String line : lines) {
			//Trim the line
			line = line.trim();
			
			//Skip empty line or comment line
			if(line.length() == 0 || line.charAt(0) == '#') continue;
			//In the case of a new category in a ini file
			else if(line.charAt(0) == '[') {
				//Parse the prefix from the line
				prefix = line.substring(1, line.length() - 1) + ".";
				categories.add(prefix.substring(0, prefix.indexOf(".")));
			}
			//If this is a keyValue pair
			else if(line.indexOf('=') != -1) {
				//Split the pair in two
				String[] parts = line.split("=");
				//Now put the split pair into the map, and put an empty string if the second part is not defined
				map.put(prefix + parts[0], (parts.length != 1) ? parts[1] : "");
			}	
		}
	}
	
	/**
	 * Returns the value associated with the provided key
	 * @param key the key to check for
	 * @return
	 */
	public String get(String key) {
		return map.get(key);
	}
	
	/**
	 * Returns the float value associated with the provided key.
	 * If this value can not be parsed as float, returns 0
	 * @param key	the key to check for
	 * @return
	 */
	public float getFloat(String key) {
		return Rogue.parseFloat(get(key), 0.0f);
	}
	
	/**
	 * Returns the boolean value associated with the provided key
	 * @param key the key to check for
	 * @return
	 */
	public boolean getBoolean(String key) {
		return Rogue.parseBoolean(get(key));
	}
	
	/**
	 * Returns the Int2D value associated with the provided key
	 * @param key the key to check for
	 * @return
	 */
	public Int2D getInt2D(String key) {
		return new Int2D(get(key));
	}
	
	/**
	 * Returns the intenger value associated with the provide key.
	 * If the provided value can not be parsed, returns 0
	 * @param key
	 * @return
	 */
	public int getInteger(String key) {
		return Rogue.parseInt(get(key), 0);
	}
}
