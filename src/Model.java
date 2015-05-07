

public class Model {
	
	/**
	 * The game board for this model. It is indexed by x, then y, from the top left.
	 */
	private Tile[][] board;
	
	private int redScore;
	
	private int blueScore;
	
	public enum TurnType { RED, BLUE };
	
	private TurnType turn;
	
	public Tile getTile(int x, int y) {
		try {
			return board[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ArrayIndexOutOfBoundsException("(" + x + "," + y + "): outside the game board");
		}
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
