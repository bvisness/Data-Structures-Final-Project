
/**
 * This class defines a single side of a {@linkplain Tile}.
 * @author Ben Visness
 *
 */
public class Quadrant {
	
	/**
	 * The type of this quadrant.
	 */
	private QuadrantType type;
	
	/**
	 * The owner of this quadrant.
	 */
	private Owner owner;

	/**
	 * Gets the type of this quadrant.
	 * @return The type of this quadrant.
	 */
	public QuadrantType getType() {
		return type;
	}

	/**
	 * Sets the type of this quadrant.
	 * @param type The new type of quadrant.
	 */
	public void setType(QuadrantType type) {
		this.type = type;
	}

	/**
	 * Gets the owner of this quadrant.
	 * @return The owner of this quadrant
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of this quadrant.
	 * @param owner The new owner of this quadrant.
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
	}
	
	/**
	 * Constructs a new Quadrant with default settings: all
	 * {@link QuadrantType.GRASS}, owner {@link Owner.NONE}.
	 */
	public Quadrant() {
		type = QuadrantType.GRASS;
		owner = Owner.NONE;
	}
	
	/**
	 * Returns a string representation of this quadrant, in the format
	 * TYPE_OWNER. (For example, C_B is a city owned by Blue.)
	 */
	public String toString() {
		String typeString = "UNKNOWN", ownerString = "UNKNOWN";
		
		switch (type) {
		case GRASS:
			typeString = "G";
			break;
		case ROAD:
			typeString = "R";
			break;
		case CITY:
			typeString = "C";
			break;
		}
		
		switch (owner) {
		case NONE:
			ownerString = "N";
			break;
		case RED:
			ownerString = "R";
			break;
		case BLUE:
			ownerString = "B";
			break;
		}
		
		return typeString + "_" + ownerString;
	}

}
