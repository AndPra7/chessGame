package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	// uma proprieda boolean já começa com falso
	private boolean check;
	private boolean checkMate;
	
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
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
		Piece capturedPiece = makeMove(source, target);
		
		// testa se o próprio jogador se colocou em cheque, caso sim o movimento é desfeito e lançado uma mensagem
		if(testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		//se o oponente ficou em cheque recebe verdadeiro, se não, recebe falso
		// se testCheck do oponente do jogadoratual diz que está em cheque se não dirá que a partida não está em cheque
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		// testa se a jogada deixa o oponente em xeque mate
		if(testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		}
		else {
		//troca o turno
		nextTurn();
		}
		
		//faz um downcasting porque a peça era do tipo Piece
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		// retira a peça que está na posição de origem
		ChessPiece p = (ChessPiece)board.removePiece(source);
		//contagem do movimento das peças
		p.increaseMoveCount();
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
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		// tira a peça que removeu do destino
		ChessPiece p = (ChessPiece)board.removePiece(target);
		//contagem do movimento das peças
		p.decreaseMoveCount();
		// devolve para a posição de origem
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			// devolve a peça dp tabu para a posição de destino
			board.placePiece(capturedPiece, target);
			// tira a peça da lista de peças capturada e coloca novamente na lista de peças no tabuleiro
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
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
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	//filtra o rei de uma determinada cor
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			//testa se é uma instancia de rei
			if(p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	//verifica se o Rei está em cheque
	private boolean testCheck(Color color) {
		// pega a posição do Rei em formato de matriz
		Position kingPosition = king(color).getChessPosition().toPosition();
		// listas do oponente
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			//matriz de movimentos possíveis da peça p
			boolean[][] mat = p.possibleMoves();
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color)) {
			return false;
		}
		//pega todas as peças da color do atributo
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for(int i=0; i<board.getRows(); i++) {
				for(int j=0; j<board.getColumns(); j++) {
					// essa posição da matriz é um movimento possível?
					if(mat[i][j]) {
						//pega a posição da peça p
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						//movimentação da peça p do destino para o destino
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						//se não está em check retorna false dizendo que não está em mate
						if(!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
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
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE));
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
	}

}
