package trb1914.rogue;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.grid.Grid;
import trb1914.rogue.input.Key;
import trb1914.rogue.input.MouseDistributor;
import trb1914.rogue.io.Registry;
import trb1914.rogue.state.GameState;
import trb1914.rogue.state.MainMenu;

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
	/** Intermediate, eased framerate value*/
	private float FRAME_RATE = 60.0f;
	
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
		
		//Load the font to use for the stage
		stage.beginDraw();
		stage.textFont(createFont("Dawnlike/GUI/SDS_8x8.ttf", 8));
		stage.endDraw();
		
		//Set the size for the frameRate font
		textSize(16);
		
		//Finally create the first game state
		GameState.current = new MainMenu();
	}
	
	/**
	 * Called every time we need to render stuff.
	 */
	public void draw() {
		//First black background
		background(0);
		//Then start drawing on the stage
		stage.beginDraw();
		stage.background(0);
		GameState.current.render(stage);
		stage.endDraw();
		
		//update the current game state
		GameState.current.update();
		
		//Update the mouseDistributor
		MouseDistributor.update(mouseX, mouseY);
		
		//Now that the buffer (stage) has been rendered, render it in turn to the main window
		image(stage, 0, 0, width, height);
		
		//Check if we should draw FPS during devlopment
		if(DRAW_FPS) {
			fill(0);
			text(floor(FRAME_RATE) + " fps", 2, 16);
			fill(40, 255, 255);
			text(floor(FRAME_RATE) + " fps", 3, 17);
			FRAME_RATE -= (FRAME_RATE - frameRate) * 0.1f;
		}
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
	 * Random element from any fixed size array
	 * @param arr
	 * @return
	 */
	public <E> E random(E[] arr) {
		return arr[floor(Rogue.app.random(arr.length))];
	}
	
	/**
	 * Random element from an arraylist
	 * @param list
	 * @return
	 */
	public <E> E random(ArrayList<E> list) {
		return (list.get(floor(random(list.size()))));
	}
	
	/**
	 * Checks wheterh one in (number) was met. For example,
	 * one in 50 has a 2 percent chance of succeeding
	 * @param chance
	 * @return
	 */
	public static boolean oneIn(float chance) {
		  return Rogue.app.random(1) < (1 / chance);
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
