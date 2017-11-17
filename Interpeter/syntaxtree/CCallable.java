/**
 * the call object
 */
package syntaxtree;

import java.util.List;

public interface CCallable {
	Object call(Interpreter interpreter, List<Object> arguments);
}