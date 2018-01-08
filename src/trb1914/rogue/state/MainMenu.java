package trb1914.rogue.state;

import processing.core.PGraphics;
import trb1914.rogue.Rogue;
import trb1914.rogue.gfx.Textures;
import trb1914.rogue.input.MouseHandler;
import trb1914.rogue.ui.BigTextButton;
import trb1914.rogue.ui.ButtonType;
import trb1914.rogue.ui.FancyButton;

/**
 * Holds the main menu and its functionality
 * @author trb1914
 */
public class MainMenu extends GameState{

	/**
	 * Creates the mainMenu
	 */
	public MainMenu() {
		//New game button
	    BigTextButton newGameButton = new BigTextButton(0, 112, ButtonType.GREEN, "New Game");
	    newGameButton.addMouseHandler(new MouseHandler(){
	      public void mouseDown(int x, int y){
	        GameState.current.change(new Game());
	      }
	    });
	    newGameButton.centerX();
	    
	    //Close button
	    BigTextButton closeButton = new BigTextButton(0, 176, ButtonType.RED, "Exit");
	    closeButton.addMouseHandler(new MouseHandler(){
	      public void mouseDown(int x, int y){
	        Rogue.app.exit();
	      }
	    });
	    closeButton.centerX();
	    
	    int bottomY = Rogue.stage.height - 40;
	    final FancyButton musicButton = new FancyButton(8, bottomY, ButtonType.YELLOW, "Music: ON", Textures.get("music.trumpet"));
	    musicButton.addMouseHandler(new MouseHandler(){
	      public void mouseDown(int x, int y){
	        musicButton.setText((musicButton.getText() == "Music: ON") ? "Music: OFF" : "Music: ON");
	      }
	    });

	    addRender(newGameButton, closeButton, musicButton);
	    addMouse(newGameButton, closeButton, musicButton);
	}

	/**
	 * Renders the main menu
	 */
	public void render(PGraphics g) {
		//First draw the bg-image
		g.tint(255, 100);//Slightly opaque
		g.image(Textures.mainMenuBG, 0, 0, g.width, g.height);
		g.tint(255);//reset tint

		//Paint the title text
		drawTitle(g);

		//Finally paint all other elements
		super.render(g);
	}

	/**
	 * Draws the title text
	 * @param g
	 */
	private void drawTitle(PGraphics g){
		g.textSize(36);//Set size to title size
		String title = "Rogue 2D";
		float titleWidth = g.textWidth(title);
		float posX = (g.width - titleWidth) * 0.5f;
		float posY = 100;
		int shadowOffset = 3;
		g.fill(0);//Black bg for the text, sort of dropshadow
		g.text(title, posX + shadowOffset, posY + shadowOffset);
		g.fill(255, 0, 0);
		g.text(title, posX, posY);
		g.fill(255);
		g.text(title.substring(0, 5), posX, posY);
		g.fill(255);
		g.textSize(8);//Reset to normal size
	}
}
