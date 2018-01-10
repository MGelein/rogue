package trb1914.rogue.util;

import java.util.ArrayList;

/**
 * A managed list is a list that can safely remove and 
 * add any of its contents during maintenance
 * @author trb1914
 */
public class ManagedList<E> extends ArrayList<E> {

	/** Serial Versian UID, don't know why its necessary, but ah well...*/
	private static final long serialVersionUID = 6695625910274840616L;
	
	/** The list of items to add*/
	private ArrayList<E> toAdd = new ArrayList<E>();
	/** The list of items to remove*/
	private ArrayList<E> toRem = new ArrayList<E>();
	
	
	/**
	 * Does the maintenance on this list
	 */
	public void doMaintenance() {
		if(toAdd.size() > 0) {
			for(E e : toAdd) super.add(e);
			toAdd.clear();
		}
		
		if(toRem.size() > 0) {
			for(E e : toRem) super.remove(e);
			toRem.clear();
		}
	}
	
	@Override
	public boolean add(E e) {
		toAdd.add(e);
		return true;
	}
	
	@Override
	public boolean remove(Object o) {
		toRem.add((E) o);
		return true;
	}
	

}
