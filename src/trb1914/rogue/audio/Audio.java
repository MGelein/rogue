package trb1914.rogue.audio;

import processing.sound.SoundFile;
import trb1914.debug.Debug;
import trb1914.rogue.Rogue;
import trb1914.rogue.io.Registry;

/**
 * All audio clips have this as their parent class
 * @author trb1914
 */
public class Audio {
	
	/** The soundfile once it is loaded*/
	private SoundFile soundFile;

	/**
	 * Creates a new audio clip by loading the file that is assigned in the .ini
	 * files to the provided definition
	 */
	public Audio(String definition) {
		if(definition == null) {
			Debug.println("Audio definition is null, not loading", this);
			return;
		}
		if(definition.length() < 1) {
			Debug.println("Audio definition is of length 0, not loading", this);
			return;
		}
		String url = Registry.get("audio." + definition);
		if(url != null) {
			soundFile = new SoundFile(Rogue.app, "../audio/" + url);
		}else {
			Debug.println("Audio definition '" + definition + "' could not be found in the registry. Not loading.", this);
		}
	}
	
	/**
	 * Starts playing the soundFile
	 */
	public void play() {
		soundFile.play();
	}
	
	/**
	 * Stops playing the soundFile
	 */
	public void stop() {
		soundFile.stop();
	}
}
