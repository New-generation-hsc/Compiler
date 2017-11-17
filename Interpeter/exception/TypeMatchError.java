/**
 * this is the class when assign a different class to another
 * the type cann't match
 */

package exception;

import lexer.Tag;

public class TypeMatchError extends Exception{

	public final Tag left;
	public final Tag right;
	public final int line;

	public TypeMatchError(Tag left, Tag right, int line, String message){
		super(message);

		this.line = line;

		this.left = left;
		this.right = right;
	}
}