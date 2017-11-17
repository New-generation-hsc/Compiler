package exception;

import lexer.Token;

public class UnAssignmentError extends RuntimeException {

	public final Token name;
	public UnAssignmentError(Token name, String message){
		super(message);
		this.name = name;
	}
}