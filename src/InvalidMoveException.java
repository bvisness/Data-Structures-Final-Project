

public class InvalidMoveException extends RuntimeException {
    
	public InvalidMoveException(String message) {
        super(message);
    }
	
	public InvalidMoveException(Throwable throwable) {
        super(throwable);
    }
	
	public InvalidMoveException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
