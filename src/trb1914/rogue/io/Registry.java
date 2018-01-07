package trb1914.rogue.io;

import java.util.HashMap;

import trb1914.rogue.math.Int2D;

/**
 * Abstract class for global access to the .ini files
 * that have been loaded
 * @author trb1914
 */
public abstract class Registry {
	/** Map of the files that have been loaded, ordered by their prefix*/
	private static HashMap<String, Ini> files = new HashMap<String, Ini>();
	
	/**
	 * Loads the provided url as a new Ini file under the provided prefix
	 * @param url		the file to load
	 * @param prefix	the prefix to file it under
	 */
	public static void load(String url, String prefix) {
		files.put(prefix, new Ini(url));
	}
	
	/**
	 * Queries the registry for the provided string value
	 * @param key the key it is registered under
	 * @return
	 */
	public static String get(String key) {
		return files.get(getFileName(key)).get(getQuery(key));
	}
	
	/**
	 * Queries the registry for the provided float value
	 * @param key the key it is registered under
	 * @return
	 */
	public static float getFloat(String key) {
		return files.get(getFileName(key)).getFloat(getQuery(key));
	}
	
	/**
	 * Queries the registry for the provided integer value
	 * @param key the key it is registered under
	 * @return
	 */
	public static int getInteger(String key) {
		return files.get(getFileName(key)).getInteger(getQuery(key));
	}
	
	/**
	 * Queries the registry for the provided boolean value
	 * @param key the key it is registered under
	 * @return
	 */
	public static boolean getBoolean(String key) {
		return files.get(getFileName(key)).getBoolean(getQuery(key));
	}
	
	/**
	 * Queries the registry for the provided Int2D value
	 * @param key the key it is registered under
	 * @return
	 */
	public static Int2D getInt2D(String key) {
		return files.get(getFileName(key)).getInt2D(getQuery(key));
	}
	
	/**
	 * Returns the query part of the get string
	 * @param q	the query to split
	 * @return
	 */
	private static String getQuery(String q) {
		return q.substring(q.indexOf(".") + 1);
	}
	
	/**
	 * Returns the filename part of the query string
	 * @param q	the query to split
	 * @return
	 */
	private static String getFileName(String q) {
		return q.substring(0, q.indexOf("."));
	}
}
