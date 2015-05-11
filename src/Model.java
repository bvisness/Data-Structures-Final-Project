import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;



public class Model {
	
	/**
	 * The game board for this model. It is indexed by x, then y, from the top left.
	 */
	private Tile[][] board;
	
	private int redScore;
	
	private int blueScore;
	
	private int tilesPlaced;
	
	public enum Turn { RED, BLUE };
	
	private Turn turn;
	
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
		if (!isInBounds(x, y)) {
			return null;
		}
		return board[x][y];
	}
	
	public Tile getTile(Coordinate c) {
		return getTile(c.getX(), c.getY());
	}

	public int getRedScore() {
		return redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public Turn getTurn() {
		return turn;
	}
	
	private void nextTurn() {
		if (getTurn() == Turn.RED) {
			turn = Turn.BLUE;
		} else {
			turn = Turn.RED;
		}
	}
	
	public boolean isGameOver() {
		return tilesPlaced >= board.length * board.length;
	}
	
	public Model(int size) {
		if (size % 2 == 0) {
			throw new IllegalArgumentException(size + ": The game board size must be odd so it has a middle space.");
		}
		board = new Tile[size][size];
		board[size / 2][size / 2] = Tile.randomTile();
		turn = Turn.RED;
		tilesPlaced = 1;
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
	
	private Quadrant getNeighborQuadrant(int x, int y, int side) {
		Coordinate neighborC = getNextTileCoordinates(x, y, side);
		if (isInBounds(neighborC) && getTile(neighborC) != null) {
			return getTile(neighborC).getQuadrant(Tile.oppositeSide(side));
		} else {
			return null;
		}
	}
	
	private Quadrant getNeighborQuadrant(Coordinate c, int side) {
		return getNeighborQuadrant(c.getX(), c.getY(), side);
	}
	
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
	
	private Owner newOwner(LinkedList<Owner> owners) {
		boolean allNone = true;
		Owner newOwner = Owner.NONE;
		
		Iterator<Owner> itr = owners.iterator();
		while (itr.hasNext()) {
			Owner owner = itr.next();
			if (owner != Owner.NONE) {
				if (allNone) {
					newOwner = owner;
				} else if (owner != newOwner) {
					newOwner = Owner.NONE;
				}
				allNone = false;
			}
		}
		if (allNone) {
			newOwner = turnToOwner(getTurn());
		}
		
		return newOwner;
	}
	
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
	
	public boolean isMoveValid(Coordinate c, Tile tile) {
		return isMoveValid(c.getX(), c.getY(), tile);
	}
	
	public void placeTile(int x, int y, Tile tile) {
		if (!isMoveValid(x, y, tile)) {
			throw new InvalidMoveException("Tile " + tile + " at (" + x + "," + y + ")");
		}
		board[x][y] = tile;
		
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
		System.out.println("roadOwner: " + roadOwner);
		System.out.println("cityOwner: " + cityOwner);
		
		updateTiles(x, y, QuadrantType.ROAD, roadOwner);
		updateTiles(x, y, QuadrantType.CITY, cityOwner);
		
		tilesPlaced++;
		nextTurn();
	}
	
	private void updateTiles(int x, int y, QuadrantType type, Owner owner) {
		HashSet<Tile> set = new HashSet<Tile>();
		
		boolean tilesComplete = updateTilesRecursive(x, y, type, owner, set);
		
		System.out.println(type + " complete: " + tilesComplete);
		
		if (tilesComplete) {
			int scoredTiles = set.size(); // The number of tiles we visited is the number of tiles that scored
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
	
	private boolean updateTilesRecursive(int x, int y, QuadrantType type, Owner owner, HashSet<Tile> set) {
		if (!isInBounds(x, y)) {
			return true; // Nothing to do but return that we're complete from here.
		}
		
		Tile tile = getTile(x, y);
		if (tile == null) {
			return false; // Nothing to do but return that we've hit an incomplete spot.
		}
		if (!tile.hasQuadrantType(type)) {
			return false; // The feature we want is nowhere on this tile.
						  // This can only happen if this is the first tile we check.
		}
		
		if (set.contains(tile)) {
			return true; // We've already seen this tile, so we assume completeness.
		}
		
		set.add(tile);
		
		boolean[] completes = new boolean[4];
		for (int side = 0; side < 4; side++) {
			completes[side] = true; // Assume true, even if we don't have a feature to follow
			Quadrant thisQuadrant = tile.getQuadrant(side);
			if (thisQuadrant.getType() == type) {
				thisQuadrant.setOwner(owner);
				Coordinate nextC = getNextTileCoordinates(x, y, side);
				completes[side] = updateRoadsRecursive(nextC, type, owner, set);
			}
		}
		
		return completes[0] && completes[1] && completes[2] && completes[3];
	}
	
	private boolean updateRoadsRecursive(Coordinate c, QuadrantType type, Owner owner, HashSet<Tile> set) {
		return updateTilesRecursive(c.getX(), c.getY(), type, owner, set);
	}
	
	public void placeTile(Coordinate c, Tile tile) {
		placeTile(c.getX(), c.getY(), tile);
	}
	
	public Tile randomLegalTile() {
		if (isGameOver()) {
			return null;
		}
		
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
