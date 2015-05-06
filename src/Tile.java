
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

	public void setQuadrants(Quadrant[] quadrants) {
		this.quadrants = quadrants;
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
		roadsComplete = false;
		citiesComplete = false;
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

}
