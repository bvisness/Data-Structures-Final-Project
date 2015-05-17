/**
 * This interface defines a simple observer pattern for listening for game tile
 * updates.
 * 
 * @author Ben Visness
 * 
 */
public interface TileUpdateListener {

	/**
	 * Performs actions when a game tile is updated.
	 */
	public void tileUpdated();

}
