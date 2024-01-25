package boardgame;

public class Piece {
	
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		//por padrão o Java já coloca nulo
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	
}
