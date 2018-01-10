package trb1914.rogue.math;

import trb1914.rogue.Rogue;

/**
 * 2D integer coordinates, useful for pixel art, grid coordinates
 * and such. Small and lightweight
 * @author trb1914
 */
public class Int2D {
	
	public final static Int2D LEFT = new Int2D(-1, 0); 
	public final static Int2D RIGHT = new Int2D(1, 0); 
	public final static Int2D DOWN = new Int2D(0, 1); 
	public final static Int2D UP = new Int2D(0, -1); 
	
	
	
	/** The x-part of this object*/
	public int x;
	/** The y-part of this object*/
	public int y;
	
	/**
	 * Initializes a new (0, 0) Int2D
	 */
	public Int2D(){
		x = y = 0;
	}
	
	/**
	 * Initializes a new Int2D instance with the provided coords
	 * @param x	the x-part
	 * @param y the y-part
	 */
	public Int2D(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Tries to parse the provided definition to create a new instance
	 * of Int2D
	 * @param definition the definition to parse
	 * @throws Exception 
	 */
	public Int2D(String definition){
		if(definition == null || definition.indexOf(',') == -1) {
			System.err.println("Illegal Int2D definition: " + definition);
			x = y = 0;
		}else {
			if(definition.indexOf("OR") != -1) {//This means texture variants
				definition = Rogue.app.random(definition.split("OR"));//Pick a random one
			}
			String[] parts = definition.split(",");
			x = Rogue.parseInt(parts[0].trim(), 0);
			y = Rogue.parseInt(parts[1].trim(), 0);
		}
	}
	
	/**
	 * If this Int2D has the same value as the provided object
	 * @param b
	 * @return
	 */
	public boolean equals(Int2D b) {
		return b.x == x && b.y == y;
	}
	
	/**
	 * Returns the distance between two Int2D instances
	 * @return
	 */
	public float dist(Int2D b) {
		float dx = x - b.x;
		float dy = y - b.y;
		return Rogue.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * Returns a Int2D that is x - 1, y
	 * @return
	 */
	public Int2D left() {
		return new Int2D(x - 1, y);
	}
	
	/**
	 * Returns an Int2D that is x + 1, y
	 * @return
	 */
	public Int2D right() {
		return new Int2D(x + 1, y);
	}
	
	/**
	 * Returns an Int2D that is x, y - 1
	 * @return
	 */
	public Int2D up() {
		return new Int2D(x, y - 1);
	}
	
	/**
	 * Returns an Int2D that is x, y + 1
	 * @return
	 */
	public Int2D down() {
		return new Int2D(x, y + 1);
	}
	
	/**
	 * Sets the parts of this vector to a random, odd number between min and max
	 * @param minX
	 * @param maxX
	 * @param minY
	 * @param maxY
	 * @return
	 */
	public Int2D rndOdd(int minX, int maxX, int minY, int maxY) {
		//Pick random coords
		x = Rogue.floor(Rogue.app.random(minX, maxX - 1));
		y = Rogue.floor(Rogue.app.random(minY, maxY - 1));
		//Then make them odd if they are not yet odd
		x += (x % 2 == 0) ? 1 : 0;
		y += (y % 2 == 0) ? 1 : 0;		
		//Return this for method chaining
		return this;
	}
	
	/**
	 * Sets the parts of this vector to random odd, between 0 and max
	 * @param maxX
	 * @param maxY
	 * @return
	 */
	public Int2D rndOdd(int maxX, int maxY) {
		return rndOdd(0, maxX, 0, maxY);
	}
	
	/**
	 * Returns a new Int2D instance that represents the difference
	 * between this instance and the provided instance
	 * @param b	the instance to compare it to
	 * @return
	 */
	public Int2D diff(Int2D b) {
		return new Int2D(x - b.x, y - b.y);
	}
	
	/**
	 * Sets the parts of this Int2D instance to mathc the provided 
	 * x and y
	 * @param x
	 * @param y
	 * @return
	 */
	public Int2D set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Sets this instance to match the parts of the provided instance
	 * @param b
	 * @return
	 */
	public Int2D set(Int2D b) {
		return set(b.x, b.y);
	}
	
	/**
	 * Adds the provided parts to this instance's parts
	 * @param x
	 * @param y
	 * @return
	 */
	public Int2D add(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Adds the provided instance parts to this instance's parts
	 * @param b
	 * @return
	 */
	public Int2D add(Int2D b) {
		return add(b.x, b.y);
	}
	
	/**
	 * Subtracts the provided parts from this instance parts
	 * @param x
	 * @param y
	 * @return
	 */
	public Int2D sub(int x, int y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Subtracts the parts of the provided instance from this instance
	 * @param b
	 * @return
	 */
	public Int2D sub(Int2D b) {
		return sub(b.x, b.y);
	}
	
	/**
	 * Halves this Int2D's parts
	 * @return
	 */
	public Int2D half() {
		x *= 0.5f;
		y *= 0.5f;
		return this;
	}
	
	/**
	 * Returns a one length vector in some kind of multiple
	 * of 90 degrees (ie, North, south, west or east
	 * @return
	 */
	public Int2D rndDir() {
		int rnd = Rogue.floor(Rogue.app.random(4.0f));
		x = y = 0;
		if(rnd == 0) x = -1;
		else if(rnd == 1) x = 1;
		else if(rnd == 2) y = -1;
		else if(rnd == 3) y = 1;
		return this;
	}
	
	/**
	 * If both parts of this Int2D are odd
	 * @return
	 */
	public boolean isOdd() {
		return (x % 2 == 0) && (y % 2 == 0);
	}
	
	/**
	 * Used for debugging
	 */
	public String toString() {
		return "Int2D(x: " + x + ", y: " + y + ")";
	}
	
	/**
	 * Returns an exact copy (not the same instance) of this instance
	 * @return
	 */
	public Int2D copy() {
		return new Int2D(x, y);
	}
}
