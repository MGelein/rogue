package trb1914.rogue.gfx.light;

import trb1914.rogue.Rogue;

/**
 * Holds a template for a light to use. This is basically just the color and range
 * @author trb1914
 */
public class LightTemplate {
	/** The color of this lightSource*/
	public int c;
	/** The range of this lightSource*/
	public int str;

	/**
	 * Creates a new LightTemplate from scratch.
	 * @param col
	 * @param strength
	 */
	public LightTemplate(int col, int strength) {
		c = col;
		str = strength;
	}

	/**
	 * Builds a LigthTemplate from the ini file definition
	 * @param s
	 */
	public LightTemplate(String s) {
		if(s != null && s.length() > 1){
			String[] parts = s.split(":");
			String[] colorStrings = parts[0].split(",");
			int[] colorNumbers = new int[3];
			for(int i = 0; i < colorStrings.length; i++){
				colorNumbers[i] = Rogue.parseInt(colorStrings[i].trim());
			}
			c = Rogue.app.color(colorNumbers[0], colorNumbers[1], colorNumbers[2]);
			str = Rogue.parseInt(parts[1]);
		}else{
			c = Rogue.app.color(0, 255, 100);
			str = 6;
		}
	}
}
