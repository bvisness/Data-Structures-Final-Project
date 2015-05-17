import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Defines a model for the board game Carcassonne.
 * 
 * @author Ben Visness
 * 
 */
public class Model {

	/**
	 * The game board for this model. It is indexed by x, then y, from the top
	 * left.
	 */
	private Tile[][] board;

	/**
	 * The red player's score.
	 */
	private int redScore;

	/**
	 * The blue player's score.
	 */
	private int blueScore;

	/**
	 * The number of tiles placed on the board.
	 */
	private int tilesPlaced;

	/**
	 * An enum to describe whose turn it is.
	 */
	public enum Turn {
		RED, BLUE
	};

	/**
	 * Which player's turn it is.
	 */
	private Turn turn;

	/**
	 * A simple class to hold an xy-coordinate of a tile on the board.
	 * 
	 * @author Ben Visness
	 * 
	 */
	private class Coordinate {
		/**
		 * The x-value of this coordinate.
		 */
		private final int x;

		/**
		 * The y-value of this coordinate.
		 */
		private final int y;

		/**
		 * Constructs a new Coordinate with a given x and y.
		 * 
		 * @param x
		 *            The x-value of this coordinate.
		 * @param y
		 *            The y-value of this coordinate.
		 */
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * Gets the x-value of this coordinate.
		 * 
		 * @return The x-value of this coordinate.
		 */
		public int getX() {
			return x;
		}

		/**
		 * Gets the y-value of this coordinate.
		 * 
		 * @return The y-value of this coordinate.
		 */
		public int getY() {
			return y;
		}
	}

	/**
	 * Gets the {@linkplain Tile} at a given x and y on the game board.
	 * 
	 * @param x
	 *            The x-value of the tile to get.
	 * @param y
	 *            The y-value of the tile to get.
	 * @return The {@linkplain Tile} at the given x and y.
	 */
	public Tile getTile(int x, int y) {
		if (!isInBounds(x, y)) {
			return null;
		}
		return board[x][y];
	}

	/**
	 * Gets the {@linkplain Tile} at a given Coordinate on the game board.
	 * 
	 * @param c
	 *            The Coordinate of the tile to get.
	 * @return The {@linkplain Tile} at the given coordinate.
	 * @see Coordinate
	 */
	private Tile getTile(Coordinate c) {
		return getTile(c.getX(), c.getY());
	}

	/**
	 * Gets the red player's score.
	 * 
	 * @return The red player's score.
	 */
	public int getRedScore() {
		return redScore;
	}

	/**
	 * Gets the blue player's score.
	 * 
	 * @return The blue player's score.
	 */
	public int getBlueScore() {
		return blueScore;
	}

	/**
	 * Gets which player's turn it is.
	 * 
	 * @return Which player's turn it is.
	 * @see Turn
	 */
	public Turn getTurn() {
		return turn;
	}

	/**
	 * Switches to the next player's turn.
	 */
	private void nextTurn() {
		if (getTurn() == Turn.RED) {
			turn = Turn.BLUE;
		} else {
			turn = Turn.RED;
		}
	}

	/**
	 * Gets whether the game is over.
	 * 
	 * @return Whether the game is over.
	 */
	public boolean isGameOver() {
		return tilesPlaced >= board.length * board.length;
	}

	/**
	 * Gets the winner of the game.
	 * 
	 * @return The winner of the game, or null if the game is a draw.
	 * @throws Exception
	 *             if the game is not yet over.
	 * @see Turn
	 */
	public Turn getWinner() throws Exception {
		if (!isGameOver()) {
			throw new Exception("The game is not over.");
		}

		if (redScore > blueScore) {
			return Turn.RED;
		} else if (blueScore > redScore) {
			return Turn.BLUE;
		} else {
			return null;
		}
	}

	/**
	 * Constructs a new Model with a given size.
	 * 
	 * @param size
	 *            The width and height of the game board. This number must be
	 *            odd so the board has a middle space.
	 */
	public Model(int size) {
		if (size % 2 == 0) {
			throw new IllegalArgumentException(size + ": The game board size must be odd so it has a middle space.");
		}
		board = new Tile[size][size];
		board[size / 2][size / 2] = Tile.randomTile();
		turn = Turn.RED;
		tilesPlaced = 1;
	}

	/**
	 * Gets the coordinates of the next tile on the game board, given the x and
	 * y of a tile and a direction in which to travel.
	 * 
	 * @param x
	 *            The x-value of the start tile.
	 * @param y
	 *            The y-value of the start tile.
	 * @param side
	 *            The side of the tile to move from. Using the constants defined
	 *            in {@linkplain Tile} is recommended.
	 * @return A {@linkplain Coordinate} for the next tile.
	 * @see Coordinate
	 */
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

	/**
	 * Gets the coordinates of the next tile on the game board, given the
	 * Coordinate of a tile and a direction in which to travel.
	 * 
	 * @param c
	 *            The Coordinate of the start tile.
	 * @param side
	 *            The side of the tile to move from. Using the constants defined
	 *            in {@linkplain Tile} is recommended.
	 * @return A {@linkplain Coordinate} for the next tile.
	 * @see Coordinate
	 */
	private Coordinate getNextTileCoordinates(Coordinate c, int side) {
		return getNextTileCoordinates(c.getX(), c.getY(), side);
	}

	/**
	 * Gets whether a given x and y coordinates are in bounds.
	 * 
	 * @param x
	 *            The x coordinate to check.
	 * @param y
	 *            The y coordinate to check
	 * @return Whether the given x and y are in bounds.
	 */
	private boolean isInBounds(int x, int y) {
		return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
	}

	/**
	 * Gets whether a given Coordinate is in bounds.
	 * 
	 * @param c
	 *            The Coordinate to check.
	 * @return Whether the given Coordinate is in bounds.
	 * @see Coordinate
	 */
	private boolean isInBounds(Coordinate c) {
		return isInBounds(c.getX(), c.getY());
	}

	/**
	 * Gets the quadrant immediately neighboring the side of a given tile.
	 * 
	 * @param x
	 *            The x-coordinate of the start tile.
	 * @param y
	 *            The y-coordinate of the start tile.
	 * @param side
	 *            The side of the start tile to check for neighbors on.
	 * @return The {@linkplain Quadrant} immediately neighboring the side of a
	 *         given tile, or null if there is none.
	 */
	private Quadrant getNeighborQuadrant(int x, int y, int side) {
		Coordinate neighborC = getNextTileCoordinates(x, y, side);
		if (isInBounds(neighborC) && getTile(neighborC) != null) {
			return getTile(neighborC).getQuadrant(Tile.oppositeSide(side));
		} else {
			return null;
		}
	}

	/**
	 * Gets the quadrant immediately neighboring the side of a given tile.
	 * 
	 * @param c
	 *            The Coordinate of the start tile.
	 * @param side
	 *            The side of the start tile to check for neighbors on.
	 * @return The {@linkplain Quadrant} immediately neighboring the side of a
	 *         given tile, or null if there is none.
	 * @see Coordinate
	 */
	private Quadrant getNeighborQuadrant(Coordinate c, int side) {
		return getNeighborQuadrant(c.getX(), c.getY(), side);
	}

	/**
	 * A method to convert whose turn it is into an Owner for a tile.
	 * 
	 * @param player
	 *            The player whose turn it is.
	 * @return An Owner value matching the current Turn.
	 * @see Owner
	 * @see Turn
	 */
	private static Owner turnToOwner(Turn player) {
		switch (player) {
		case RED:
			return Owner.RED;
		case BLUE:
			return Owner.BLUE;
		default:
			return null;
		}
	}

	/**
	 * Given a list of Owners encountered during a crawl of the game board, gets
	 * the Owner who should own the entire feature.
	 * 
	 * @param owners
	 *            The list of Owners generated from a crawl of the board.
	 * @return The Owner who should own the entire feature.
	 */
	private Owner newOwner(LinkedList<Owner> owners) {
		// Assume we encountered no owned features.
		boolean allNone = true;
		Owner newOwner = Owner.NONE;

		// Iterate through the owners we encountered
		Iterator<Owner> itr = owners.iterator();
		while (itr.hasNext()) {
			Owner owner = itr.next();
			if (owner != Owner.NONE) { // Unowned features do not affect the
										// outcome.
				if (allNone) {
					// This is the first player owner we have seen
					newOwner = owner;
				} else if (owner != newOwner) {
					// We have an ownership conflict, and we should nullify all
					// ownership for this feature.
					newOwner = Owner.NONE;
				}
				allNone = false;
			}
		}

		// If we never saw a player owner, the current player should own the
		// whole feature.
		if (allNone) {
			newOwner = turnToOwner(getTurn());
		}

		return newOwner;
	}

	/**
	 * Checks whether a given Tile can be legally placed at a given x and y on
	 * the board.
	 * 
	 * @param x
	 *            The x-coordinate at which to place the tile.
	 * @param y
	 *            The y-coordinate at which to place the tile.
	 * @param tile
	 *            The tile to place at the given x and y.
	 * @return Whether the Tile can be legally placed at the given x and y.
	 */
	public boolean isMoveValid(int x, int y, Tile tile) {
		// Check if the indices are in bounds. If this fails then something
		// is badly wrong and we should return an exception instead of
		// returning false.
		if (!isInBounds(x, y)) {
			throw new ArrayIndexOutOfBoundsException("(" + x + "," + y + "): outside the game board");
		}

		// Check if there is already a tile where we are trying to place.
		if (getTile(x, y) != null) {
			return false;
		}

		// Check all neighboring tiles to see if the Quadrants match up.
		boolean hasNeighbor = false;
		for (int side = 0; side < 4; side++) {
			Quadrant thisQuadrant = tile.getQuadrant(side);
			Quadrant neighborQuadrant = getNeighborQuadrant(x, y, side);
			if (neighborQuadrant != null) {
				hasNeighbor = true;
				if (thisQuadrant.getType() != neighborQuadrant.getType()) {
					return false;
				}
			}
		}

		// Check if there were any adjacent tiles at all.
		if (!hasNeighbor) {
			return false;
		}

		// We passed all the tests!
		return true;
	}

	/**
	 * Checks whether a given Tile can be legally placed at a given x and y on
	 * the board.
	 * 
	 * @param c
	 *            The Coordinate at which to place the tile.
	 * @param tile
	 *            The tile to place at the given x and y.
	 * @return Whether the Tile can be legally placed at the given x and y.
	 * @see Coordinate
	 */
	private boolean isMoveValid(Coordinate c, Tile tile) {
		return isMoveValid(c.getX(), c.getY(), tile);
	}

	/**
	 * Places the given tile at the given x and y on the board.
	 * 
	 * @param x
	 *            The x-coordinate at which to place the tile.
	 * @param y
	 *            The y-coordinate at which to place the tile.
	 * @param tile
	 *            The tile to place at the given x and y.
	 */
	public void placeTile(int x, int y, Tile tile) {
		// Check if the move is valid. If not, we must throw an exception.
		if (!isMoveValid(x, y, tile)) {
			throw new InvalidMoveException("Tile " + tile + " at (" + x + "," + y + ")");
		}

		// Put the tile in the board.
		board[x][y] = tile;

		// Determine who should own the roads and cities on the new tile.
		LinkedList<Owner> neighborRoadOwners = new LinkedList<Owner>();
		LinkedList<Owner> neighborCityOwners = new LinkedList<Owner>();
		for (int side = 0; side < 4; side++) {
			Quadrant neighborQ = getNeighborQuadrant(x, y, side);
			if (neighborQ != null) {
				if (neighborQ.getType() == QuadrantType.ROAD) {
					neighborRoadOwners.add(neighborQ.getOwner());
				} else if (neighborQ.getType() == QuadrantType.CITY) {
					neighborCityOwners.add(neighborQ.getOwner());
				}
			}
		}
		Owner roadOwner = newOwner(neighborRoadOwners);
		Owner cityOwner = newOwner(neighborCityOwners);

		// Crawl through the board, updating the game state accordingly
		updateTiles(x, y, QuadrantType.ROAD, roadOwner);
		updateTiles(x, y, QuadrantType.CITY, cityOwner);

		tilesPlaced++;
		nextTurn();
	}

	/**
	 * Places the given tile at the given x and y on the board.
	 * 
	 * @param c
	 *            The Coordinate at which to place the tile.
	 * @param tile
	 *            The tile to place at the given Coordinate.
	 * @see Coordinate
	 */
	private void placeTile(Coordinate c, Tile tile) {
		placeTile(c.getX(), c.getY(), tile);
	}

	/**
	 * Starting at a given tile, crawls through the board, updating ownership
	 * and adding to the score if features are completed.
	 * 
	 * @param x
	 *            The x-coordinate of the start tile.
	 * @param y
	 *            The y-coordinate of the start tile.
	 * @param type
	 *            The type of quadrant to follow.
	 * @param owner
	 *            The new owner of the feature.
	 */
	private void updateTiles(int x, int y, QuadrantType type, Owner owner) {
		// Maintain a set of already-visited tiles.
		HashSet<Tile> set = new HashSet<Tile>();

		// The recursive method will return whether the given type of feature is
		// now complete.
		boolean tilesComplete = updateTilesRecursive(x, y, type, owner, set);

		if (tilesComplete) {
			int scoredTiles = set.size(); // The number of tiles we visited is
											// the number of tiles that scored
			switch (owner) {
			case RED:
				redScore += scoredTiles;
				break;
			case BLUE:
				blueScore += scoredTiles;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * A method that recursively walks through the board, updating ownership and
	 * checking completeness for a given type of quadrant.
	 * 
	 * @param x
	 *            The x-coordinate of the tile to examine.
	 * @param y
	 *            The y-coordinate of the tile to examine.
	 * @param type
	 *            The type of quadrant to follow.
	 * @param owner
	 *            The new owner of the feature.
	 * @param set
	 *            A set of already-visited tiles.
	 * @return Whether the tile's features are complete.
	 */
	private boolean updateTilesRecursive(int x, int y, QuadrantType type, Owner owner, HashSet<Tile> set) {
		if (!isInBounds(x, y)) {
			return true; // Nothing to do but return that we're complete from
							// here.
		}

		Tile tile = getTile(x, y);
		if (tile == null) {
			return false; // Nothing to do but return that we've hit an
							// incomplete spot.
		}
		if (!tile.hasQuadrantType(type)) {
			return false; // The feature we want is nowhere on this tile.
							// This can only happen if this is the first tile we
							// check.
		}

		if (set.contains(tile)) {
			return true; // We've already seen this tile, so we assume
							// completeness.
		}

		set.add(tile);

		// Check whether the features on each side of the tile are complete
		boolean[] completes = new boolean[4];
		for (int side = 0; side < 4; side++) {
			completes[side] = true; // Assume true, even if we don't have a
									// feature to follow
			Quadrant thisQuadrant = tile.getQuadrant(side);
			if (thisQuadrant.getType() == type) {
				thisQuadrant.setOwner(owner);
				Coordinate nextC = getNextTileCoordinates(x, y, side);
				completes[side] = updateRoadsRecursive(nextC, type, owner, set);
			}
		}

		return completes[0] && completes[1] && completes[2] && completes[3];
	}

	/**
	 * A method that recursively walks through the board, updating ownership and
	 * checking completeness for a given type of quadrant.
	 * 
	 * @param c
	 *            The Coordinate of the tile to examine.
	 * @param type
	 *            The type of quadrant to follow.
	 * @param owner
	 *            The new owner of the feature.
	 * @param set
	 *            A set of already-visited tiles.
	 * @return Whether the tile's features are complete.
	 * @see Coordinate
	 */
	private boolean updateRoadsRecursive(Coordinate c, QuadrantType type, Owner owner, HashSet<Tile> set) {
		return updateTilesRecursive(c.getX(), c.getY(), type, owner, set);
	}

	/**
	 * Gets a random tile that can be legally placed on the board.
	 * @return A random legal tile.
	 */
	public Tile randomLegalTile() {
		// Prevent infinite-looping conditions when there are no open spaces on
		// the board
		if (isGameOver()) {
			return null;
		}

		// Brute-force over the whole board, rotating the tile and checking
		// whether it can be legally placed somewhere
		Tile newTile = null;
		boolean isLegal = false;
		while (!isLegal) {
			newTile = Tile.randomTile();
			for (int x = 0; x < board.length; x++) {
				for (int y = 0; y < board[0].length; y++) {
					if (getTile(x, y) == null) {
						for (int i = 0; i < 4; i++) {
							if (isMoveValid(x, y, newTile)) {
								isLegal = true;
							}
							newTile.rotateRight();
						}
						if (isLegal) {
							return newTile;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * Returns a string representation of the model. Very limited, only for
	 * emergency debugging purposes.
	 */
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
