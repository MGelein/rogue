package trb1914.rogue.ai;

import java.util.HashMap;

import trb1914.debug.Debug;
import trb1914.rogue.io.Ini;

/**
 * All possible factions are contained within this Enum.
 * A faction also has relations to other factions
 * @author trb1914
 */
public class Faction{
	
	/** Constant defining this is a friendly relationship*/
	public static final int FRIENDLY = 1;
	/** Constant defining this is a enemy relationship*/
	public static final int ENEMY = -1;
	/** Constant defining this is a neutral relationship*/
	public static final int NEUTRAL = 0;

	/** All the factions that have been parsed*/
	private static HashMap<String, Faction> factions = new HashMap<String, Faction>();

	/**
	 * Loads the values from the registry into the 
	 */
	public static void load() {
		Ini ini = new Ini("factions.ini");
		for(String category : ini.categories) {
			String enemies = ini.get(category + ".enemies");
			String friends = ini.get(category + ".friends");
			//Add it to the hashmap
			factions.put(category, new Faction(category, enemies, friends));
		}
		//Now quickly check if all the factions were correctly defined
		for(String key : factions.keySet()) {
			factions.get(key).checkValid();
		}
	}

	/**
	 * Returns the faction object for the specified faction names
	 * @param name
	 * @return
	 */
	public static Faction get(String name) {
		//In case of bad input, return none
		if(name == null || name.length() == 0) return factions.get("none");

		//Lower case the name to be sure
		name = name.toLowerCase();

		//Check if the faction exists
		if(factions.containsKey(name)) {
			return factions.get(name);
		}else {
			//By default return the none faction
			Debug.println("Faction not found (" + name + "), using faction 'none'", Faction.class);
			return factions.get("none");
		}
	}

	/**
	 * Checks if the provided string exists
	 * @param faction
	 * @return
	 */
	private static boolean exists(String faction) {
		return factions.containsKey(faction);
	}

	/** The list of enemies*/
	private String[] enemies;
	/** The list of friends*/
	private String[] friends;
	/** The name of this faction*/
	private String name;

	/**
	 * Creates a new faction using the provided definition of the relations of this faction
	 * @param enemies
	 * @param neutral
	 * @param friends
	 */
	private Faction(String name, String enemies, String friends) {
		//Sanitize the input, toLowercase and remove all spaces
		enemies = enemies.toLowerCase().replaceAll(" ", "");
		friends = friends.toLowerCase().replaceAll(" ", "");
		//Assign it to the arrays
		if(enemies.length() > 0) this.enemies = enemies.split(",");
		if(friends.length() > 0) this.friends = friends.split(",");
		this.name = name;
	}

	/**
	 * Quickly checks if all enemies, neutral and friends acutally are existing factions
	 * @return
	 */
	private void checkValid() {
		//Check enemies
		if(enemies != null) {
			for(String e : enemies) {
				if(!Faction.exists(e)) {
					Debug.println("WARNING: '" + e + "' can't be enemy of '" + name + "' since '" + e + "' is not an existing faction", this);
				}
			}
		}
		//Check friends
		if(friends != null) {
			for(String f : friends) {
				if(!Faction.exists(f)) {
					Debug.println("WARNING: '" + f + "' can't be neutral of '" + name + "' since '" + f + "' is not an existing faction", this);
				}
			}
		}
	}
	
	/**
	 * Checks if the provided faction is an enemy of this faction
	 * @param f
	 * @return
	 */
	public boolean isEnemy(Faction fac) {
		for(String e : enemies) {
			if(e.equals(fac.name)) return true;
		}
		return false;
	}
	
	/**
	 * Returns if the provided faciton is a friend of this faction
	 * @param f
	 * @return
	 */
	public boolean isFriendly(Faction fac) {
		for(String f : friends) {
			if(f.equals(fac.name)) return true;
		}
		return false;
	}
	
	/**
	 * A faction is neutral if it is neither enemy nor friend
	 * @param fac
	 * @return
	 */
	public boolean isNeutral(Faction fac) {
		return !isFriendly(fac) && !isEnemy(fac);
	}
	
	/**
	 * Returns an integer representing the relation to the provided faction. These
	 * numbers are also encoded as public static variables in this class: Faction.ENEMY,
	 * Faction.NEUTRAL and Faction.FRIENDLY. -1 means enemy, 0 means neutral, and 1 means friendly
	 * @param fac
	 * @return
	 */
	public int getRelation(Faction fac) {
		return isFriendly(fac) ? Faction.FRIENDLY : (isEnemy(fac) ? Faction.ENEMY : Faction.NEUTRAL);
	}
}
