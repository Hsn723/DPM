package master;
import lejos.nxt.Sound;

/* Original code taken from http://pastebin.com/mr70U8MA
 * for the byte code for Benny Hill and the play method.
 * Refactored to work in different situations.
 * Plays some sound samples.
 * Modified to add the ability to select sample
*/
/**
 * Play some music
 * @author
 *
 */
public class SoundSample {
	// Constructors
	private static short[] currentSong;

	private static final String name1 = "Imperial March";
	private static final short[] note1 = { 370, 39, 0, 7, 370, 39, 0, 7, 370, 39, 0, 7, 
		294, 26, 0, 2, 440, 13, 0, 2, 370, 39, 0, 2, 294, 26, 0, 2, 440, 13, 0, 2, 370, 78,
		554, 39, 0, 7, 554, 39, 0, 7, 554, 39, 0, 7, 587, 26, 0, 2, 440, 13, 0, 2,  
		349, 39, 0, 2, 294, 26, 0, 2, 440, 13, 0, 2, 370, 78 };

	private static final String name3 = "Benny Hill";
	static final short[] note3 = { 294, 38, 330, 13, 392, 13, 0, 13, 392, 13,
			0, 13, 330, 4, 0, 8, 294, 4, 0, 8, 196, 4, 0, 8, 247, 4, 0, 8, 294,
			4, 0, 8, 247, 4, 0, 8, 330, 13, 0, 13, 294, 4, 0, 8, 247, 4, 0, 8,
			220, 4, 0, 8, 247, 4, 0, 8, 196, 25, 220, 13, 233, 13, 247, 13,
			294, 13, 330, 13, 294, 13, 392, 13, 0, 50, 294, 4, 0, 8, 330, 4, 0,
			8, 294, 4, 0, 8, 392, 17, 0, 8, 392, 17, 0, 8, 330, 4, 0, 8, 294,
			4, 0, 8, 196, 4, 0, 8, 247, 13, 294, 4, 0, 8, 247, 4, 0, 8, 330,
			13, 0, 13, 294, 4, 0, 8, 247, 4, 0, 8, 196, 4, 0, 8, 247, 6, 0, 7,
			294, 13, 0, 13, 294, 4, 0, 8, 294, 4, 0, 8, 370, };
	/**
	 * Plays the SoundSample
	 */
	public void play() {
		for (int i = 0; i < currentSong.length; i += 2) {
			final int tone = (int) currentSong[i];
			final int b = i + 1;
			final int length = 10 * currentSong[b];
			Sound.playTone(tone, length);
			try {
				Thread.sleep(length);
			} catch (InterruptedException e) {
			}
		}
	}
	
	/**
	 * Initializes a default SoundSample object
	 * @param title	the title of one of the sample sounds
	 */
	public SoundSample(String title) {
		if (title.equals(name1))
			currentSong = note1;
		else currentSong = note3;
	}
	
	/**
	 * Initializes a custom SoundSample object
	 * @param song an array containing the tone-duration pairs of the song
	 */
	public SoundSample(short[] song) {
		this.currentSong = song;
	}
}