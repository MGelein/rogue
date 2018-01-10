package trb1914.rogue.grid;

import processing.core.PGraphics;
import trb1914.rogue.actor.Actor;
import trb1914.rogue.gen.DunGen;
import trb1914.rogue.gfx.Tex;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.gfx.Theme;
import trb1914.rogue.gfx.light.Light;
import trb1914.rogue.gfx.light.LightTemplate;
import trb1914.rogue.math.Int2D;
import trb1914.rogue.render.RenderAble;

/**
 * Every single object and / or tile on the grid is a gridObject
 * @author trb1914
 */
public class GridObject extends RenderAble{

	/** The position of this GridObject*/
	public Int2D pos;
	/** The texture we're using to render this object*/
	public Tex tex;
	/** By default you can't walk on a GridOjbect*/
	protected boolean walkable = false;
	/** By default you can see through a GridObject*/
	protected boolean opaque = false;
	/** By default a GridObject is not animated*/
	protected boolean animated = false;
	/** A reference to the containing parent*/
	public GridCell parent;

	/***
	 * Creates a new GridObject at the specified position
	 * @param x
	 * @param y
	 * @param parent
	 */
	public GridObject(int x, int y, GridCell parent) {
		pos = new Int2D(x, y);
		this.parent = parent;
	}

	/***
	 * Creates a new GridObject at the specified position
	 * @param pos
	 * @param parent
	 */
	public GridObject(Int2D pos, GridCell parent) {
		this.pos = pos.copy();
		this.parent = parent;
	}

	/**
	 * By default a GridObject can be interacted with. It just doesn't
	 * do anything meaningful untill overriden
	 * @param actor the actor that is doing the interacting
	 */
	public void interact(Actor actor) {};

	/**
	 * By default GridObjects can be updated, it just doesn't do anything useful
	 */
	public void update() {};

	/**
	 * It sets the texture to the provided value and updates if this
	 * gridobject is animated
	 * @param s
	 * @param anim
	 */
	public void setTexture(Tex t) {
		tex = t;
	}

	/**
	 * Animates this gridObject, provided it is possible
	 */
	public void animate() {
		//First see if we even have a texture loaded
		if(tex == null || !tex.animated) return;
		//Advance animation by one frame
		tex.animate();
	}

	/**
	 * Renders this GridObject at the specified coords
	 */
	public void render(PGraphics g) {
		//Can't render a null texture
		if(tex == null) return;
		//Now render the image
		g.image(tex.img, pos.x * Grid.GRID_SIZE, pos.y * Grid.GRID_SIZE, tex.size, tex.size);
	}

	/**
	 * Parses tile information for walkability, opacity and theme application
	 * @param dungeonTile
	 * @return
	 */
	public GridObject parse(String dungeonTile){
		//According to tileType set walkability to false if not already false
		walkable = DunGen.isWalkAble(dungeonTile);
		opaque = DunGen.isOpaque(dungeonTile);
		if(DunGen.isFloor(dungeonTile) || DunGen.isWall(dungeonTile)){
			Textures.setTheme(Theme.temple);
		}

		if(DunGen.isLiquid(dungeonTile)){
			Textures.setTheme(Theme.lava);
			//If this is lava, also be sure to add a light source
			LightTemplate lavaTemplate = Light.get("lava");
			parent.grid.addLight(new Light(pos, lavaTemplate, parent.grid));
		}

		//If it is not a void tile, set the texture
		if(!DunGen.isType(DunGen.VOID, dungeonTile)){
			setTexture(new Tex(dungeonTile));
		}

		//Reset theme
		Textures.setTheme(Theme.none);
		return this;
	}
}
