import java.util.Random;

public enum QuadrantType {
	GRASS, ROAD, CITY;
	
	private static final QuadrantType[] VALUES = values();
	private static final int SIZE = VALUES.length;
	private static final Random RANDOM = new Random();
	
	public static QuadrantType randomType()  {
		return VALUES[RANDOM.nextInt(SIZE)];
	}
};
