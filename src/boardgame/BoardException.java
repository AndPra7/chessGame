package boardgame;

public class BoardException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	//repassa a mensagem para o construtor da superClasse RunTimeException
	public BoardException(String msg) {
		super(msg);
	}
	
}
