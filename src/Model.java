

public class Model {
	
	/**
	 * The game board for this model. It is indexed by x, then y, from the top left.
	 */
	private Tile[][] board;
	
	private int redScore;
	
	private int blueScore;
	
	public enum TurnType { RED, BLUE };
	
	private TurnType turn;

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public TurnType getTurn() {
		return turn;
	}

	public void setTurn(TurnType turn) {
		this.turn = turn;
	}
	
	public Model(int size) {
		if (size % 2 == 0) {
			throw new IllegalArgumentException(size + ": The game board size must be odd so it has a middle space.");
		}
		board = new Tile[size][size];
		board[size / 2][size / 2] = Tile.randomTile();
	}
	
	public boolean isMoveValid(int x, int y, Tile tile) throws ArrayIndexOutOfBoundsException {
		// Error if checking outside the game board - this is enough of
		// a problem to warrant an exception.
		if (x < 0 || y < 0 || x >= board.length || y >= board[0].length) {
			throw new ArrayIndexOutOfBoundsException("(" + x + "," + y + "): outside the game board");
		}
		
		// False if there is already a tile where we want to place.
		if (board[x][y] != null) {
			return false;
		}
		
		boolean isNeighbor = false;
		
		// Check north
		if (y > 0 && board[x][y-1] != null) {
			isNeighbor = true;
			QuadrantType thisType = tile.getQuadrants()[Tile.NORTH].getType();
			QuadrantType neighborType = board[x][y-1].getQuadrants()[Tile.SOUTH].getType();
			if (thisType != neighborType) {
				return false;
			}
		}
		
		// Check east
		if (x < board.length - 1 && board[x+1][y] != null) {
			isNeighbor = true;
			QuadrantType thisType = tile.getQuadrants()[Tile.EAST].getType();
			QuadrantType neighborType = board[x+1][y].getQuadrants()[Tile.WEST].getType();
			if (thisType != neighborType) {
				return false;
			}
		}
		
		// Check south
		if (y < board[0].length - 1 && board[x][y+1] != null) {
			isNeighbor = true;
			QuadrantType thisType = tile.getQuadrants()[Tile.SOUTH].getType();
			QuadrantType neighborType = board[x][y+1].getQuadrants()[Tile.NORTH].getType();
			if (thisType != neighborType) {
				return false;
			}
		}
		
		// Check west
		if (x > 0 && board[x-1][y] != null) {
			isNeighbor = true;
			QuadrantType thisType = tile.getQuadrants()[Tile.WEST].getType();
			QuadrantType neighborType = board[x-1][y].getQuadrants()[Tile.EAST].getType();
			if (thisType != neighborType) {
				return false;
			}
		}
		
		// See if there were any neighbors at all
		if (!isNeighbor)
			return false;
		
		// It passed all the tests!
		return true;
	}
	
	public String toString() {
		String result = "";
		for (int y = 0; y < board[0].length; y++) {
			for (int x = 0; x < board.length; x++) {
				result += board[x][y] + "\t";
			}
			result += "\n";
		}
		return result;
	}

}
