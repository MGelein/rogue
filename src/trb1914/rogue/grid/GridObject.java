package trb1914.rogue.grid;

import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.gen.DunGen;
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
	protected PImage tex;
	/** The frame of animation we're on*/
	private int texFrame = 0;
	/** The modifier used to load a texture*/
	private Int2D texMod = new Int2D();
	/** The name of this texture we're drawing with*/
	public String texName;
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
	 * do anything meaningful 
	 */
	public void interact() {};

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
	public void setTexture(String s, boolean anim) {
		setTexture(s);
		animated = anim;
	}

	/**
	 * It loads a texture from the Texture memory using the provided
	 * texture identifier
	 * @param s
	 */
	public void setTexture(String s) {
		tex = Textures.get(s);
		texMod = Textures.theme;
		animated = Textures.isAnimated(s);
		texName = s;
		//Debug.println(texName + ": " + tex);
	}

	/**
	 * Animates this gridObject, provided it is possible
	 */
	public void animate() {
		//First see if we even have a texture loaded
		if(tex == null) return;
		//Advance animation by one frame
		texFrame ++;
		//Set the theme to the right modifier
		Textures.setTheme(texMod);
		//Retrieve the texture
		tex = Textures.get(texName, texFrame);
		//Reset the theme to default
		Textures.setTheme(Theme.none);
	}

	/**
	 * Renders this GridObject at the specified coords
	 */
	public void render(PGraphics g) {
		//Can't render a null texture
		if(tex == null) return;
		//Now render the image
		g.image(tex, pos.x * Grid.GRID_SIZE, pos.y * Grid.GRID_SIZE, Grid.GRID_SIZE, Grid.GRID_SIZE);
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
			setTexture(dungeonTile);
		}

		//Reset theme
		Textures.setTheme(Theme.none);
		return this;
	}
}
