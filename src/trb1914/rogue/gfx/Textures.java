package trb1914.rogue.gfx;

import java.io.File;
import java.util.HashMap;

import processing.core.PImage;
import trb1914.rogue.Rogue;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.io.Registry;
import trb1914.rogue.math.Int2D;

/**
 * This class globally provides access to all the spriteSheets, animated or not
 * @author trb1914
 */
public abstract class Textures {

	/** Stores every sprite sheet under its own name*/
	private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
	/** Stores every animated sheet under its own name*/
	private static HashMap<String, AnimatedSheet> animSheets = new HashMap<String, AnimatedSheet>();
	/** Reference to the mainMenu background picture*/
	public static PImage mainMenuBG;
	/** The current texture modifier*/
	public static Int2D theme = Theme.none;

	/**
	 * This function will load all the spriteSheets, animated sheets and other images
	 */
	public static void load() {
		//Load the bgImage
		mainMenuBG = Rogue.app.loadImage("./data/img/mainmenu.jpg");

		//Objects
		animSheets.put("decor", new AnimatedSheet(Grid.SIZE, "Objects/Decor0.png", "Objects/Decor1.png"));
		animSheets.put("door", new AnimatedSheet(Grid.SIZE, "Objects/Door0.png", "Objects/Door1.png"));
		animSheets.put("effect", new AnimatedSheet(Grid.SIZE, "Objects/Effect0.png", "Objects/Effect1.png"));
		spriteSheets.put("fence", new SpriteSheet(Grid.SIZE, "Dawnlike/Objects/Fence.png"));
		spriteSheets.put("floor", new SpriteSheet(Grid.SIZE, "Dawnlike/Objects/Floor.png"));
		animSheets.put("ground", new AnimatedSheet(Grid.SIZE, "Objects/Ground0.png", "Objects/Ground1.png"));
		animSheets.put("hill", new AnimatedSheet(Grid.SIZE, "Objects/Hill0.png", "Objects/Hill1.png"));
		animSheets.put("map", new AnimatedSheet(Grid.SIZE, "Objects/Map0.png", "Objects/Map1.png"));
		animSheets.put("ore", new AnimatedSheet(Grid.SIZE, "Objects/Ore0.png", "Objects/Ore1.png"));
		animSheets.put("pit", new AnimatedSheet(Grid.SIZE, "Objects/Pit0.png", "Objects/Pit1.png"));
		spriteSheets.put("tile", new SpriteSheet(Grid.SIZE, "Dawnlike/Objects/Tile.png"));
		animSheets.put("trap", new AnimatedSheet(Grid.SIZE, "Objects/Trap0.png", "Objects/Trap1.png"));
		animSheets.put("tree", new AnimatedSheet(Grid.SIZE, "Objects/Tree0.png", "Objects/Tree1.png"));
		spriteSheets.put("wall", new SpriteSheet(Grid.SIZE, "Dawnlike/Objects/Wall.png"));

		//GUI
		animSheets.put("gui", new AnimatedSheet(Grid.SIZE, "GUI/GUI0.png", "GUI/GUI1.png"));

		//Items
		spriteSheets.put("ammo", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Ammo.png"));
		spriteSheets.put("amulet", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Amulet.png"));
		spriteSheets.put("armor", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Armor.png"));
		spriteSheets.put("book", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Book.png"));
		spriteSheets.put("boot", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Boot.png"));
		animSheets.put("chest", new AnimatedSheet(Grid.SIZE, "Items/Chest0.png", "Items/Chest1.png"));
		spriteSheets.put("flesh", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Flesh.png"));
		spriteSheets.put("food", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Food.png"));
		spriteSheets.put("glove", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Glove.png"));
		spriteSheets.put("hat", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Hat.png"));
		spriteSheets.put("light", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Light.png"));
		spriteSheets.put("long_weapon", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/LongWep.png"));
		spriteSheets.put("medium_weapon", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/MedWep.png"));
		spriteSheets.put("short_weapon", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/ShortWep.png"));
		spriteSheets.put("money", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Money.png"));
		spriteSheets.put("music", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Music.png"));
		spriteSheets.put("potion", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Potion.png"));
		spriteSheets.put("ring", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Ring.png"));
		spriteSheets.put("rock", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Rock.png"));
		spriteSheets.put("scroll", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Scroll.png"));
		spriteSheets.put("shield", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Shield.png"));
		spriteSheets.put("tool", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Tool.png"));
		spriteSheets.put("wand", new SpriteSheet(Grid.SIZE, "Dawnlike/Items/Wand.png"));

		//Characters
		animSheets.put("aquatic", new AnimatedSheet(Grid.SIZE, "Characters/Aquatic0.png", "Characters/Aquatic1.png"));
		animSheets.put("avian", new AnimatedSheet(Grid.SIZE, "Characters/Avian0.png", "Characters/Avian1.png")); 
		animSheets.put("cat", new AnimatedSheet(Grid.SIZE, "Characters/Cat0.png", "Characters/Cat1.png"));
		animSheets.put("demon", new AnimatedSheet(Grid.SIZE, "Characters/Demon0.png", "Characters/Demon1.png"));
		animSheets.put("dog", new AnimatedSheet(Grid.SIZE, "Characters/Dog0.png", "Characters/Dog1.png"));
		animSheets.put("elemental", new AnimatedSheet(Grid.SIZE, "Characters/Elemental0.png", "Characters/Elemental1.png"));
		animSheets.put("humanoid", new AnimatedSheet(Grid.SIZE, "Characters/Humanoid0.png", "Characters/Humanoid1.png"));
		animSheets.put("misc", new AnimatedSheet(Grid.SIZE, "Characters/Misc0.png", "Characters/Misc1.png"));
		animSheets.put("pest", new AnimatedSheet(Grid.SIZE, "Characters/Pest0.png", "Characters/Pest1.png"));
		animSheets.put("plant", new AnimatedSheet(Grid.SIZE, "Characters/Plant0.png", "Characters/Plant1.png"));
		animSheets.put("player", new AnimatedSheet(Grid.SIZE, "Characters/Player0.png", "Characters/Player1.png"));
		animSheets.put("quadraped", new AnimatedSheet(Grid.SIZE, "Characters/Quadraped0.png", "Characters/Quadraped1.png"));
		animSheets.put("reptile", new AnimatedSheet(Grid.SIZE, "Characters/Reptile0.png", "Characters/Reptile1.png"));
		animSheets.put("rodent", new AnimatedSheet(Grid.SIZE, "Characters/Rodent0.png", "Characters/Rodent1.png"));
		animSheets.put("slime", new AnimatedSheet(Grid.SIZE, "Characters/Slime0.png", "Characters/Slime1.png"));
		animSheets.put("undead", new AnimatedSheet(Grid.SIZE  , "Characters/Undead0.png", "Characters/Undead1.png"));
	}

	/**
	 * Sets the current theme modifier for the textures to the provided modifier. These
	 * modifiers can be found under Theme.*
	 * @param mod
	 */
	public static void setTheme(Int2D mod) {
		theme = mod.copy();
	}

	/**
	 * Tries to parse the provided String as a texture identifier.
	 * Uses the currently active Theme (Textures.theme)
	 * @param s
	 * @return
	 */
	public static PImage get(String s){
		return get(s, theme);
	}

	/**
	 * Tries to parse the provided String as texture identifier, 
	 * and applies the theme shift modifier
	 * @param s
	 * @param themeModifier
	 * @return
	 */
	public static PImage get(String s, Int2D themeModifier){
		if(isAnimated(s)) return get(s, 0, themeModifier);
		return spriteSheets.get(s.substring(0, s.indexOf(".")).trim().toLowerCase()) //get sheet name
				.get(Registry.getInt2D("tex." + s).copy().add(themeModifier));                     //get sheet coords
	}


	/**
	 * Looks for the animated spritesheets, uses currentTheme
	 * @param s
	 * @param frame
	 * @return
	 */
	public static PImage get(String s, int frame){
		return get(s, frame, theme);
	}

	/**
	 * Looks for the animated spriteSheets to find a match
	 * @param s
	 * @param frame
	 * @param themeModifier
	 * @return
	 */
	public static PImage get(String s, int frame, Int2D themeModifier){
		return animSheets.get(s.substring(0, s.indexOf(".")).trim().toLowerCase()) //get sheet name
				.get(Registry.getInt2D("tex." + s).copy().add(themeModifier), frame);            //get sheet coords
	}

	/**
	 * Returns whether the provided texture is an animated one or not
	 * @param s
	 * @return
	 */
	public static boolean isAnimated(String s){
		if(s.indexOf(".") == -1) {
			System.err.println("Invalid texture name, no sheet name: " + s);
			return false;
		}
		return animSheets.containsKey(s.substring(0, s.indexOf(".")).trim().toLowerCase());
	}

	/**
	 * Returns wheter a texture with the provided name actually exists
	 * @param s
	 * @return
	 */
	public static boolean exists(String s){
		if(s.indexOf(".") == -1) {
			System.err.println("Invalid texture name, no sheet name: " + s);
			return false;
		}
		String sheetName = s.substring(0, s.indexOf(".")).trim().toLowerCase();
		if(animSheets.containsKey(sheetName)|| spriteSheets.containsKey(sheetName)){
			if(Registry.get("tex." + s) != null) return true; 
		}
		return false;
	}
}
