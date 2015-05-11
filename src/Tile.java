
public class Tile {
	
	public static final int NORTH = 0;
	
	public static final int EAST = 1;
	
	public static final int SOUTH = 2;
	
	public static final int WEST = 3;
	
	/**
	 * The quadrants of this tile, in an array for easy iteration.
	 */
	private Quadrant[] quadrants;
	
	private boolean roadsComplete;
	
	private boolean citiesComplete;

	public Quadrant[] getQuadrants() {
		return quadrants;
	}
	
	public Quadrant getQuadrant(int side) {
		if (side < 0 || side > 3)
			throw new IllegalArgumentException(side + ": quadrant index must be from 0 to 3. (Use Tile.NORTH, etc. to avoid this error.)");
		return quadrants[side];
	}
	
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

	public boolean areRoadsComplete() {
		return roadsComplete;
	}

	public void setRoadsComplete(boolean roadsComplete) {
		this.roadsComplete = roadsComplete;
	}

	public boolean areCitiesComplete() {
		return citiesComplete;
	}

	public void setCitiesComplete(boolean citiesComplete) {
		this.citiesComplete = citiesComplete;
	}
	
	public Tile() {
		quadrants = new Quadrant[4];
		for (int i = 0; i < 4; i++) {
			quadrants[i] = new Quadrant();
		}
		roadsComplete = false;
		citiesComplete = false;
	}
	
	public static Tile randomTile() {
		Tile newTile = new Tile();
		for (int i = 0; i < 4; i++) {
			newTile.quadrants[i].setType(QuadrantType.randomType());
		}
		return newTile;
	}
	
	public void rotateLeft() {
		Quadrant tmp = quadrants[NORTH];
		quadrants[NORTH] = quadrants[EAST];
		quadrants[EAST] = quadrants[SOUTH];
		quadrants[SOUTH] = quadrants[WEST];
		quadrants[WEST] = tmp;
	}
	
	public void rotateRight() {
		Quadrant tmp = quadrants[NORTH];
		quadrants[NORTH] = quadrants[WEST];
		quadrants[WEST] = quadrants[SOUTH];
		quadrants[SOUTH] = quadrants[EAST];
		quadrants[EAST] = tmp;
	}
	
	public static int oppositeSide(int side) {
		return (side + 2) % 4;
	}
	
	public String toString() {
		return "N: " + quadrants[NORTH]
				+ ". E: "+ quadrants[EAST]
				+ ". S: "+ quadrants[SOUTH]
				+ ". W: " + quadrants[WEST]
				+ ".";
	}

}
