
public class Model {
	
	private Tile[][] board;
	
	private int redScore;
	
	private int blueScore;
	
	public enum TurnType { RED, BLUE };
	
	private TurnType turn;

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public void setBlueScore(int blueScore) {
		this.blueScore = blueScore;
	}

	public TurnType getTurn() {
		return turn;
	}

	public void setTurn(TurnType turn) {
		this.turn = turn;
	}

}
