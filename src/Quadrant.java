
public class Quadrant {
	
	public enum QuadrantType { GRASS, ROAD, CITY };
	
	public enum OwnerType { NONE, RED, BLUE };
	
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

}
