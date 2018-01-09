package trb1914.rogue.ai;
/**
 * All possible factions are contained within this Enum.
 * A faction also has relations to other factions
 * @author trb1914
 */
public enum Faction {
	/** The player faction. NPC's should be part of this*/
	PLAYER,
	/** The undead faction. Most undead creatures like each other*/
	UNDEAD,
	/** The undefined faction. Everything will be hostile*/
	NONE
}
