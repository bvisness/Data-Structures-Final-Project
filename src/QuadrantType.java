import java.util.Random;

/**
 * This enum defines a type of quadrant, either GRASS, ROAD, or CITY.
 * 
 * @author Ben Visness
 * 
 */
public enum QuadrantType {
	GRASS, ROAD, CITY;

	private static final QuadrantType[] VALUES = values();
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = new Random();

	/**
	 * Gets a random value of QuadrantType.
	 * @return A random QuadrantType value.
	 */
	public static QuadrantType randomType() {
		return VALUES[RANDOM.nextInt(SIZE)];
	}
};
