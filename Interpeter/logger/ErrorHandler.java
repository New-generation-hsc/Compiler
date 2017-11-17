/**
 * the file is the main project error handle
 * if it cann't deal with the error, than throw it
 */
package logger;

import java.util.logging.Logger;
import exception.TypeCastException;
import exception.RuntimeError;
import exception.TypeMatchError;
import exception.UnAssignmentError;

import utils.Resolve;

public class ErrorHandler {

	public static void error(int line, String message){
		report(line, "", message);
	}

	public static void report(int line, String where, String message){
		Logger.getLogger(ErrorHandler.class.getName()).severe("[line " + line + "] Error" + where + ": " + message);
	}

	public static void runtimeError(RuntimeError error){
		Logger.getLogger(ErrorHandler.class.getName()).severe(error.getMessage() + "\n[line " + error.token.line + "]");
	}

	public static void patternMatchError(TypeCastException error){
		Logger.getLogger(ErrorHandler.class.getName()).severe("<line " + error.token.line + "> Error: " + error.getMessage());
	}

	public static void typeMatchError(TypeMatchError error){
		String left = Resolve.resolve(error.left);
		String right = Resolve.resolve(error.right);

		Logger.getLogger(ErrorHandler.class.getName()).severe("<line " + error.line + "> Error: " + error.getMessage() + " '" + right + "' -> '" + left + "'");
	}

	public static void unAssignmentError(UnAssignmentError error){
		Logger.getLogger(ErrorHandler.class.getName()).severe("<line " + error.name.line + "> Error: " + "variable must be assigned before reference");
	}
}