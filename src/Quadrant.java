import java.util.Random;


public class Quadrant {
	
	private QuadrantType type;
	
	private OwnerType owner;

	public QuadrantType getType() {
		return type;
	}

	public void setType(QuadrantType type) {
		this.type = type;
	}

	public OwnerType getOwner() {
		return owner;
	}

	public void setOwner(OwnerType owner) {
		this.owner = owner;
	}
	
	public Quadrant() {
		type = QuadrantType.GRASS;
		owner = OwnerType.NONE;
	}
	
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
