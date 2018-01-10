package trb1914.rogue.actor;

/**
 * The static Director takes care of all the actions 
 * and the actors energy levels
 * @author trb1914
 */
public abstract class Director {

	/** The head of the linked list*/
	private static Actor firstActor = null;
	/** All actors are tied together in a linked list, here is the current one*/
	private static Actor currentActor = null;
	
	/**
	 * Updates the energy levels of the current actor and 
	 * does list maintenance
	 * @return returns true untill we reach the end of the list
	 */
	public static boolean process() {
		if(currentActor == null && firstActor == null) return false;
		if(currentActor == null) {
			currentActor = firstActor;
			return false;
		}
		
		//First check if we are waiting for this actor to select an action, if so return false
		if(currentActor.nextAction == null) {
			//First try to get an action from them by updating more
			currentActor.update();
			return false;
		}
		
		//Grant this actor a bit of energy
		currentActor.energy += currentActor.speed;
		//If we have the energy for the current action, try it, if it fails try another as
		//long as we have the energy
		while(currentActor.energy >= currentActor.nextAction.energy) {
			currentActor.nextAction = currentActor.nextAction.perform();
			if(currentActor.nextAction == null) break;
		}
		
		//Now continue on to the next person
		currentActor = currentActor.next;
		
		//We're still processing
		return true;
	}
	
	/**
	 * Processes all actions of  all the actors
	 */
	public static void update() {
		//Process all actors in the linked list
		boolean actorsLeft = process();
		while(actorsLeft) {
			actorsLeft = process();
		}
	}
	
	/**
	 * Adds this actor to be a part of the acting
	 * @param a
	 */
	public static void addActor(Actor a) {
		if(firstActor == null) {
			firstActor = a;
		}else {
			//Add the new element as head element
			firstActor.prev = a;
			a.next = firstActor;
			firstActor = a;
		}
	}
	
	/**
	 * Removes the specified actor from the acting
	 * @param a
	 */
	public static void removeActor(Actor a) {
		if(a == firstActor) {
			firstActor = firstActor.next;
			firstActor.prev = null;
		}else {
			//Update their references to no longer include provided actor
			a.next.prev = a.prev;
			a.prev.next = a.next;
		}
	}
}
