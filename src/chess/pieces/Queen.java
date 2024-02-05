package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "Q";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		//cria uma matriz de booleanos da mesma dimensão do tabuleiro
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		// above
		p.setValues(position.getRow() - 1, position.getColumn());
		//repetirá isso enquanto tiverem casas vazias
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}
		//verificar se existe a peça do oponente e marca como true
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// left
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
		
		// NW
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		//repetirá isso enquanto tiverem casas vazias
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		//verificar se existe a peça do oponente e marca como true
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// NE
		p.setValues(position.getRow() - 1, position.getColumn() + 1);
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// SE
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// SW
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		// repetirá isso enquanto tiverem casas vazias
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		// verificar se existe a peça do oponente e marca como true
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//retorna toda a matriz como falso, por padrão
		return mat;
	}

}
