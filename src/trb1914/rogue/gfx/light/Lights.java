package trb1914.rogue.gfx.light;

import trb1914.rogue.io.Registry;

/**
 * Easy way to access LightTemplates
 * @author trb1914
 */
public abstract class Lights {
	/**
	 * Returns the parsed lightTemplate for the provided lightType
	 * @param s
	 * @return
	 */
	public static LightTemplate get(String s) {
		return new LightTemplate(Registry.get("light." + s));
	}
}
