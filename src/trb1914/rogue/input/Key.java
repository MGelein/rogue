package trb1914.rogue.input;

import java.util.ArrayList;

/**
 * This class will handle any key input received from the 
 * main PApplet. Handles both keycodes and letters
 * @author trb1914
 */
public abstract class Key {

	/** List with the pressed keyCodes*/
	private static ArrayList<Integer> keyCodes = new ArrayList<Integer>();
	/** List with the names of the pressed keys*/
	private static ArrayList<Character> keyNames = new ArrayList<Character>();
	
	
	/**
	 * Press the provided key (and corresponding code)
	 * @param key		the representation of the key (for example 'a')
	 * @param keyCode	the code of the key (for example: 37)
	 */
	public static void pressKey(char key, int keyCode) {
		keyCodes.add(keyCode);
		keyNames.add(key);
	}
	
	/**
	 * Release the provided key (and corresponding code)
	 * @param key		the representation of the key (for example 'a')
	 * @param keyCode	the code of the key (for example: 37)
	 */
	public static void releaseKey(char key, int keyCode) {
		keyCodes.remove(keyCode);
		keyNames.remove(key);
	}
	
	/**
	 * Checks whether the key with the provided keyCode is down
	 * @param keyCode the keyCode to check
	 * @return
	 */
	boolean isDown(int keyCode) {
		return keyCodes.contains(keyCode);
	}
	
	/**
	 * Checks whether the provided keyCode is pressed, if so,
	 * immediately resets it
	 * @param keyCode the keyCode to check
	 * @return
	 */
	boolean isDownOnce(int keyCode) {
		if(keyCodes.contains(keyCode)) {
			keyCodes.remove(keyCode);
			return true;
		}
		return false;
	}
	
	/**
	 * Checks whether the key with the provided keyname is down
	 * @param keythe keyto check
	 * @return
	 */
	boolean isDown(char key) {
		return keyNames.contains(key);
	}
	
	/**
	 * Checks whether the provided keyname is pressed, if so,
	 * immediately resets it
	 * @param key the key to check
	 * @return
	 */
	boolean isDownOnce(char key) {
		if(keyNames.contains(key)) {
			keyNames.remove(key);
			return true;
		}
		return false;
	}
}
