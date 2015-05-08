

public class Model {
	
	/**
	 * The game board for this model. It is indexed by x, then y, from the top left.
	 */
	private Tile[][] board;
	
	private int redScore;
	
	private int blueScore;
	
	public enum TurnType { RED, BLUE };
	
	private TurnType turn;
	
	private class Coordinate {
		private final int x;
		
		private final int y;

		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
	
	public Tile getTile(int x, int y) {
		try {
			return board[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("(" + x + "," + y + "): outside the game board");
		}
	}
	
	public Tile getTile(Coordinate c) {
		return getTile(c.getX(), c.getY());
	}

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	private void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public TurnType getTurn() {
		return turn;
	}

	private void setTurn(TurnType turn) {
		this.turn = turn;
	}
	
	public Model(int size) {
		if (size % 2 == 0) {
			throw new IllegalArgumentException(size + ": The game board size must be odd so it has a middle space.");
		}
		board = new Tile[size][size];
		board[size / 2][size / 2] = Tile.randomTile();
	}
	
	private Coordinate getNextTileCoordinates(int x, int y, int side) {
		if (side < 0 || side > 3)
			throw new IllegalArgumentException(side + ": quadrant index must be from 0 to 3. (Use Tile.NORTH, etc. to avoid this error.)");
		
		switch (side) {
		case Tile.NORTH:
			return new Coordinate(x, y - 1);
		case Tile.EAST:
			return new Coordinate(x + 1, y);
		case Tile.SOUTH:
			return new Coordinate(x, y + 1);
		case Tile.WEST:
			return new Coordinate(x - 1, y);
		default:
			return null;
		}
	}
	
	private Coordinate getNextTileCoordinates(Coordinate c, int side) {
		return getNextTileCoordinates(c.getX(), c.getY(), side);
	}
	
	private boolean isInBounds(int x, int y) {
		return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
	}
	
	private boolean isInBounds(Coordinate c) {
		return isInBounds(c.getX(), c.getY());
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
