package trb1914.rogue.gfx;

import trb1914.rogue.math.Int2D;

/**
 * Holds the themes statically and at some point may be instantiated to also hold
 * complete theme collections
 * @author trb1914
 */
public class Theme {
	/** Empty theme, no modifier. 0,0*/
	public static final Int2D none = new Int2D();

	//brick
	public static final Int2D brick_light = new Int2D();
	public static final Int2D brick = new Int2D(0, 3);
	public static final Int2D brick_dark = new Int2D(0, 6);
	public static final Int2D brick_darker = new Int2D(0, 9);

	//sandstone
	public static final Int2D sandstone_light = new Int2D(0, 12);
	public static final Int2D sandstone = new Int2D(0, 15);
	public static final Int2D sandstone_dark = new Int2D(0, 18);
	public static final Int2D sandstone_darker = new Int2D(0, 21);

	//wood
	public static final Int2D wood_light = new Int2D(7, 0);
	public static final Int2D wood = new Int2D(7, 3);
	public static final Int2D wood_dark = new Int2D(7, 6);
	public static final Int2D wood_darker = new Int2D(7, 9);

	//rock
	public static final Int2D rock_light = new Int2D(7, 12);
	public static final Int2D rock = new Int2D(7, 15);
	public static final Int2D rock_dark = new Int2D(7, 18);
	public static final Int2D rock_darker = new Int2D(7, 21);

	//temple
	public static final Int2D temple_light = new Int2D(14, 0);
	public static final Int2D temple = new Int2D(14, 3);
	public static final Int2D temple_dark = new Int2D(14, 6);
	public static final Int2D temple_darker = new Int2D(14, 9);

	//dirt
	public static final Int2D dirt_light = new Int2D(14, 12);
	public static final Int2D dirt = new Int2D(14, 15);
	public static final Int2D dirt_dark = new Int2D(14, 18);
	public static final Int2D dirt_darker = new Int2D(14, 21);

	//liquid
	public static final Int2D water_brick = new Int2D(0, 0);
	public static final Int2D water_dirt = new Int2D(0, 2);
	public static final Int2D water_wood = new Int2D(0, 4);
	public static final Int2D poison_brick = new Int2D(0, 12);
	public static final Int2D poison_dirt = new Int2D(0, 14);
	public static final Int2D poison_wood = new Int2D(0, 16);
	public static final Int2D lava = new Int2D(0, 18);
}
