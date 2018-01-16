package trb1914.rogue.audio;

import java.util.HashMap;

import trb1914.debug.Debug;

/**
 * Library of oneshot files that can be played. After playing once 
 * they are cached to be re-used
 * @author trb1914
 */
public abstract class Sound {

	/** Contains the all loaded files ordered by name*/
	private static HashMap<String, Audio> clips = new HashMap<String, Audio>();
	
	/**
	 * Plays the file that is signified by the definition, depending on the parameter
	 * it will cache the result in the clips. If you set forceLoad to true, it will
	 * circumvent the use of the cahce and force reload
	 * @param definition	the definition of this sound file
	 * @param cache			if we should cache this file
	 * @param forceLoad		if this file should skip a cache search and just force reload from disk
	 */
	public static void play(String definition, boolean cache, boolean forceLoad) {
		if(clips.containsKey(definition) && !forceLoad) {
			//Play the cahced version if it exists
			clips.get(definition).play();
		}else {
			//Just play a new audio file and check if we need to save its ref
			Audio audio = new Audio(definition);
			audio.play();
			//Now cache if we want to cache
			if(cache) clips.put(definition, audio);
		}
	}
	
	/**
	 * Plays the file that is signified by the definition, depending on the parameter
	 * it will cache the result in the clips. 
	 * @param definition	the definition of this sound file
	 * @param cache			if we should cache this file
	 */
	public static void play(String definition, boolean cache) {
		play(definition, cache, false);
	}
	
	/**
	 * Plays the file that is signified by the definition. This will by default
	 * cahce the file or look for it in the cache.
	 * @param definition
	 */
	public static void play(String definition) {
		play(definition, true, false);
	}
	
	/**
	 * Stops the music sound file that was previously playing. Should not be necessary for 
	 * short one stops, but still
	 * @param definition
	 */
	public static void stop(String definition) {
		if(clips.containsKey(definition)) {
			clips.get(definition).stop();
		}else {
			Debug.println("Soundclip '" + definition +  "' was not found as a cached clip. Could not stop playback." );
		}
	}
}
