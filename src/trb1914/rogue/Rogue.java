package trb1914.rogue;

import java.io.File;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.input.Key;
import trb1914.rogue.input.MouseDistributor;
import trb1914.rogue.io.Registry;

/**
 * Main class of the application, entry point for the PApplet
 * @author trb1914
 */
public final class Rogue extends PApplet{
	
	/** The scaling factor of the whole game*/
	public static final int SCL = 2;
	/** Do we need to overlay the graphics with a fps counter*/
	public static boolean DRAW_FPS = false;
	/** Self reference, used to statically access me*/
	public static Rogue app;
	/** The scaled stage we're doing all the drawing on*/
	public static PGraphics stage;
	
	/**
	 * Called before setup to do some intializing stuff
	 */
	public void settings() {
		//Set ref to self
		Rogue.app = this;
		
		//First load the game ini file
		Registry.load("game.ini", "game");
		
		//Determine if we need to draw FPS
		DRAW_FPS = Registry.getBoolean("game.show_fps");
		
		//Determine screen size and if we should go fullscreen
		if(Registry.getBoolean("game.fullscreen")) {
			fullScreen();
		}else {
			size(Registry.getInteger("game.window_width"), 
					Registry.getInteger("game.window_height"));
		}
		
		//Turn off AA for that pixelated look
		noSmooth();
	}
	
	/**
	 * Initialization that is done after the initial screen is setup
	 */
	public void setup() {
		//Black background while waiting to load
		background(0);
		//Calculate the amount of cols and rows that fits
		Grid.COLS_VISIBLE = floor(width / SCL * Grid.GRID_SIZE);
		Grid.ROWS_VISIBLE = floor(height / SCL * Grid.GRID_SIZE);
		
		//Now load the ini files for the texture indexing
		Registry.load("textures.ini", "tex");
		//Then load all of the lighting templates
		Registry.load("lights.ini", "light");
		//Set the colorMode to HSB
		colorMode(PConstants.HSB);
		
		//Create the stage to draw on
		stage = createGraphics(floor(1280 / SCL), floor(720 / SCL));
		
		//Load all the textures
		Textures.load();
	}


	/**
	 * Called by PApplet whenever the mouse is pressed anywhere
	 */
	public void mousePressed() {
		//Hand the event to the mouseDistributor to take care of
		MouseDistributor.mouseDown(mouseX, mouseY);
	}
	
	/**
	 * Called by PApplet whenever the mouse is released anywhere
	 */
	public void mouseReleased() {
		//Hand the event to the mouseDistributor to take care of
		MouseDistributor.mouseUp(mouseX, mouseY);
	}
	
	/**
	 * Called by PApplet whenever a key is pressed
	 */
	public void keyPressed() {
		//Hand the event to the Key class to register it
		Key.pressKey(key, keyCode);
	}
	
	/**
	 * Called by PApplet whenever a key is released
	 */
	public void keyReleased() {
		//Hand the event to the Key class to register it
		Key.releaseKey(key, keyCode);
	}
	
	/**
	 * Main entry point of the application. Currently I don't allow
	 * forwarding of arguments to the processing application. 
	 * @param args the command line parameters
	 */
	public static void main(String[] args) {
		PApplet.main(Rogue.class.getName());
	}
}
