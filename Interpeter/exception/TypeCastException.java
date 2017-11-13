
package exception;

import lexer.Token;

public class TypeCastException extends Exception{

	public Token token;
	public TypeCastException(Token token, String message){
		super(message);
		this.token = token;
	}
}