/**
 * An exception to be thrown if an illegal move is performed in a board game.
 * 
 * @author Ben Visness
 * 
 */
public class InvalidMoveException extends RuntimeException {

	/**
	 * Constructs a new InvalidMoveException with a message.
	 * 
	 * @param message
	 *            The message for this exception.
	 */
	public InvalidMoveException(String message) {
		super(message);
	}

	/**
	 * Constructs a new InvalidMoveException with another Throwable object.
	 * (StackOverflow told me to do it!)
	 * 
	 * @param throwable
	 *            The other Throwable thing
	 */
	public InvalidMoveException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs a new InvalidMoveException with a message and another
	 * Throwable object. (Again, don't ask me why.)
	 * 
	 * @param message
	 *            The message for this exception.
	 * @param throwable
	 *            The other Throwable thing.
	 */
	public InvalidMoveException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
