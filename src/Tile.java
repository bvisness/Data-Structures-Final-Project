/**
 * Defines a tile for Carcassonne.
 * 
 * @author Ben Visness
 * 
 */
public class Tile {

	/**
	 * Constant index for the north side of a tile.
	 */
	public static final int NORTH = 0;

	/**
	 * Constant index for the east side of a tile.
	 */
	public static final int EAST = 1;

	/**
	 * Constant index for the south side of a tile.
	 */
	public static final int SOUTH = 2;

	/**
	 * Constant index for the west side of a tile.
	 */
	public static final int WEST = 3;

	/**
	 * The quadrants of this tile, in an array for easy iteration.
	 */
	private Quadrant[] quadrants;

	/**
	 * Gets the quadrants of this tile.
	 * 
	 * @return An array of {@linkplain Quadrant} objects.
	 */
	public Quadrant[] getQuadrants() {
		return quadrants;
	}

	/**
	 * Gets a specific quadrant of this tile. To avoid index errors, using the
	 * constants defined in this class is recommended.
	 * 
	 * @param side
	 *            The side of the tile to get the quadrant from.
	 * @return The {@linkplain Quadrant} object for the given side of the tile.
	 */
	public Quadrant getQuadrant(int side) {
		if (side < 0 || side > 3)
			throw new IllegalArgumentException(side + ": quadrant index must be from 0 to 3. (Use Tile.NORTH, etc. to avoid this error.)");
		return quadrants[side];
	}

	/**
	 * Checks whether this tile contains a quadrant of a given type.
	 * 
	 * @param type
	 *            The type of quadrant to check for.
	 * @return Whether the tile has a quadrant of the given type.
	 */
	public boolean hasQuadrantType(QuadrantType type) {
		boolean hasType = false;
		for (int side = 0; side < 4; side++) {
			if (getQuadrant(side).getType() == type) {
				hasType = true;
				break;
			}
		}
		return hasType;
	}

	/**
	 * Gets the owner of the given type of quadrant on the tile. (For example,
	 * the owner of the roads or cities on the tile.)
	 * 
	 * @param type
	 *            The type of quadrant to check.
	 * @return The owner of the given type of quadrant.
	 */
	public Owner getQuadrantTypeOwner(QuadrantType type) {
		for (int side = 0; side < 4; side++) {
			if (getQuadrant(side).getType() == type) {
				return getQuadrant(side).getOwner(); // We can return the first owner we see
													 // because all roads or cities will have
													 // the same owner
			}
		}
		return null;
	}

	/**
	 * Constructs a new tile with the default Quadrant options.
	 * 
	 * @see Quadrant#Quadrant()
	 */
	public Tile() {
		quadrants = new Quadrant[4];
		for (int i = 0; i < 4; i++) {
			quadrants[i] = new Quadrant();
		}
	}

	/**
	 * Constructs a new tile with all random quadrants.
	 * 
	 * @return A randomized Tile object.
	 */
	public static Tile randomTile() {
		Tile newTile = new Tile();
		for (int i = 0; i < 4; i++) {
			newTile.quadrants[i].setType(QuadrantType.randomType());
		}
		return newTile;
	}

	/**
	 * Rotates the tile 90 degrees counterclockwise.
	 */
	public void rotateLeft() {
		Quadrant tmp = quadrants[NORTH];
		quadrants[NORTH] = quadrants[EAST];
		quadrants[EAST] = quadrants[SOUTH];
		quadrants[SOUTH] = quadrants[WEST];
		quadrants[WEST] = tmp;
	}

	/**
	 * Rotates the tile 90 degrees clockwise.
	 */
	public void rotateRight() {
		Quadrant tmp = quadrants[NORTH];
		quadrants[NORTH] = quadrants[WEST];
		quadrants[WEST] = quadrants[SOUTH];
		quadrants[SOUTH] = quadrants[EAST];
		quadrants[EAST] = tmp;
	}

	/**
	 * Given a side index of this tile, gets the index of the opposite side.
	 * 
	 * @param side
	 *            The original side index.
	 * @return The index of the opposite side.
	 */
	public static int oppositeSide(int side) {
		return (side + 2) % 4;
	}

	/**
	 * Returns a String representation of the tile.
	 */
	public String toString() {
		return "N: " + quadrants[NORTH]
				+ ". E: "+ quadrants[EAST]
				+ ". S: "+ quadrants[SOUTH]
				+ ". W: " + quadrants[WEST]
				+ ".";
	}

}
