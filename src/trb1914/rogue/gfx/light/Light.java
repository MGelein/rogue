package trb1914.rogue.gfx.light;

import trb1914.rogue.io.Registry;
/**
 * Every light is an instance of this. Also provides static access to LightTemplates
 * @author trb1914
 */
public class Light {
	/**
	 * Returns the parsed lightTemplate for the provided lightType
	 * @param s
	 * @return
	 */
	public static LightTemplate get(String s) {
		return new LightTemplate(Registry.get("light." + s));
	}
}
