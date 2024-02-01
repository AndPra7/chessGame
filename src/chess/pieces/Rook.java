package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		//cria uma matriz de booleanos da mesma dimensão do tabuleiro
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		// above
		//pega a posição da peça menos 1 na linha dela
		p.setValues(position.getRow() - 1, position.getColumn());
		//repetirá isso enquanto tiverem casas vazias
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			//faz a linha continuar subindo
			p.setRow(p.getRow() - 1);
		}
		//verificar se existe a peça do oponente e marca como true
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// left
		// pega a posição da peça menos 1 na linha dela
		p.setValues(position.getRow(), position.getColumn() - 1);
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			// faz a coluna continuar andando
			p.setColumn(p.getColumn() - 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// right
		// pega a posição da peça menos 1 na linha dela
		p.setValues(position.getRow(), position.getColumn() + 1);
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			// faz a coluna continuar andando
			p.setColumn(p.getColumn() + 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// below
		// pega a posição da peça menos 1 na linha dela
		p.setValues(position.getRow() + 1, position.getColumn());
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			// faz a linha continuar descendo
			p.setRow(p.getRow() + 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//retorna toda a matriz como falso, por padrão
		return mat;
	}

}
