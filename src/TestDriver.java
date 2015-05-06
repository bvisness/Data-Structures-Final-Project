

public class TestDriver {

	public static void main(String[] args) {
		Model model = new Model(3);
		System.out.println(model);
		
		Tile newTile = new Tile();
		newTile.getQuadrants()[Tile.SOUTH].setType(QuadrantType.CITY);
		System.out.println(newTile);
		System.out.println(model.isMoveValid(1, 0, newTile));
	}

}
