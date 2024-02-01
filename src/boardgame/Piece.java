package boardgame;

public abstract class Piece {
	
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
	
	public abstract boolean[][] possibleMoves();
	
	//testa / retorna um verdadeiro ou falso se é possível mover uma determinada peça na posição informada
	//trata-se de um método concreto que faz uso de método abstrato, chama-se Rook methods
	public boolean possibleMove(Position position) {
		return possibleMoves()[position.getRow()][position.getColumn()];
	}
	
	//contase existe pelo menos um movimento possível para a peça
	//implementação padrão que depende de um método abstrato
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		//percorre a matriz para achar uma posição que seja verdadeira
		for(int i=0; i<mat.length; i++) {
			for(int j=0; j<mat.length; j++) {
				if(mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
	
	
}
