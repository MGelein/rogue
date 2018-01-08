package trb1914.rogue.decor;

import trb1914.debug.Debug;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.gfx.light.Light;
import trb1914.rogue.gfx.light.LightTemplate;
import trb1914.rogue.grid.GridCell;
import trb1914.rogue.grid.GridObject;

/**
 * GridObject that holds a lightSource, parent for all lightSource objects.
 * @author trb1914
 */
public class LightSource extends GridObject{
	/** The actual light object of this object*/
	protected Light light;
	/** By default this source is not lit*/
	protected boolean lit = false;

	/**
	 * Creates a new LightSource with the provided cell as the parent
	 * using the provided string as texture
	 * @param parent
	 * @param s
	 */
	public LightSource(GridCell parent, String s) {
		super(parent.pos.x, parent.pos.y, parent);
		parse(s);
		light(Light.get(s));
	}

	/**
	 * Lights this lightSource using the provided template
	 * @param template
	 */
	public void light(LightTemplate template) {
		//First extinguish to prevent double lighting
		extinguish();
		light = new Light(pos.copy(), template, parent.grid);
		parent.grid.addLight(light);
		lit = true;
		//If it was previously extinguished
		if(Textures.exists(texName.replaceAll("_extinguished", ""))){
			setTexture(texName.replaceAll("_extinguished", ""));
		}
	}

	/**
	 * Used to relight an object
	 */
	void light(){
		//Check if we have already lit the object before
		if(light == null){
			Debug.println("LightObject is undefined", this);
			return;
		}
		//Else use previous parameters;
		light(new LightTemplate(light.c, light.str));
	}

	/**
	 * Extinguishes this light source
	 */
	public void extinguish() {
		parent.grid.removeLight(light);
		lit = false;

		//If extinguished texture exists, set it to it
		if(Textures.exists(texName+"_extinguished")){
			setTexture(texName + "_extinguished", false);
		}
	}
	
	/**
	 * Interacts with this lightsource
	 */
	public void interact() {
		toggle();
	}
	
	/**
	 * Toggles the lit status
	 */
	public void toggle() {
		if(lit) extinguish();
		else light();
	}
}
