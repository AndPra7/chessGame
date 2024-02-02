package chess;

import java.util.ArrayList;
import java.util.List;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		// quando a partida for iniciada, cria um tabuleiro 8 por 8 e chama initialSetup
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				// necessita de um downcasting
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		// retorna a matriz de peças da partida de xadrez
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		//converte a posição de xadrez para uma posição de matriz
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		//retorna os movimentos possíveis desta posição
		return 	board.piece(position).possibleMoves();
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		//converte as posições para as posições da matriz
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		//validando se existe uma peça na posição informada
		validateSourcePosition(source);
		//
		validateTargetPosition(source, target);
		//operação responsável por realizar a movimentação da peça
		Piece capturePiece = makeMove(source, target);
		
		//troca o turno
		nextTurn();
		
		//faz um downcasting porque a peça era do tipo Piece
		return (ChessPiece)capturePiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		// retira a peça que está na posição de origem
		Piece p = board.removePiece(source);
		//remove a possível peça que esteja na posição de destino
		Piece capturedPiece = board.removePiece(target);
		//coloca da posição de origem na posição de destino
		board.placePiece(p, target);
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		return capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		
		//verifica se a peça que esta tentando mover é do adversário
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours");
		}
		
		//testa se existe movimentos possíveis para a peça
		if(!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn() {
		turn++;
		//Se o jogador for Branco Então = ? ele vai ser Preto, caso contrário será Branco
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	// esse método receberá as coordenadas do xadrez
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		// na instanciação está convertendo para posição de matriz
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		//além de colocar as peças no tabuleiro, coloca também na lista de peças
		piecesOnTheBoard.add(piece);
	}

	// método responsável por iniciar a partida de xadrez colocando as peças no
	// tabuleiro
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}

}
