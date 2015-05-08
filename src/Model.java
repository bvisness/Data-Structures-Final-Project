import java.util.Iterator;
import java.util.LinkedList;



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
	
	private void nextTurn() {
		if (getTurn() == TurnType.RED) {
			setTurn(TurnType.BLUE);
		} else {
			setTurn(TurnType.RED);
		}
	}
	
	public Model(int size) {
		if (size % 2 == 0) {
			throw new IllegalArgumentException(size + ": The game board size must be odd so it has a middle space.");
		}
		board = new Tile[size][size];
		board[size / 2][size / 2] = Tile.randomTile();
		setTurn(TurnType.RED);
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
	
	private static OwnerType turnToOwner(TurnType player) {
		switch (player) {
		case RED:
			return OwnerType.RED;
		case BLUE:
			return OwnerType.BLUE;
		default:
			return null;
		}
	}
	
	private OwnerType newOwner(LinkedList<OwnerType> owners) {
		boolean allNone = true;
		OwnerType newOwner = OwnerType.NONE;
		
		Iterator<OwnerType> itr = owners.iterator();
		while (itr.hasNext()) {
			OwnerType owner = itr.next();
			if (owner != OwnerType.NONE) {
				if (allNone) {
					newOwner = owner;
				} else if (owner != newOwner) {
					newOwner = OwnerType.NONE;
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
		
		// TODO Do all the other tile placing business!
		LinkedList<OwnerType> neighborRoadOwners = new LinkedList<OwnerType>();
		LinkedList<OwnerType> neighborCityOwners = new LinkedList<OwnerType>();
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
		OwnerType roadOwner = newOwner(neighborRoadOwners);
		OwnerType cityOwner = newOwner(neighborCityOwners);
		System.out.println("roadOwner: " + roadOwner);
		System.out.println("cityOwner: " + cityOwner);
		
		// TODO Propagate out, setting ownership
		
		nextTurn();
	}
	
	public void placeTile(Coordinate c, Tile tile) {
		placeTile(c.getX(), c.getY(), tile);
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
