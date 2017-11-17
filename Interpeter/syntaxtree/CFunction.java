
package syntaxtree;

import java.util.List;
import utils.Transform;
import lexer.Tag;
import logger.ErrorHandler;

public class CFunction implements CCallable {

	private final Stmt.Function declaration;

	public CFunction(Stmt.Function declaration){
		this.declaration = declaration;
	}

	@Override
  	public Object call(Interpreter interpreter, List<Object> arguments) {
    	Environment environment = new Environment(interpreter.globals);

        if(declaration.parameters.size() != arguments.size()){
            ErrorHandler.error(declaration.parameters.get(0).line, "parameters length don't match");
            System.exit(1);
        }

    	for (int i = 0; i < declaration.parameters.size(); i++) {
            Tag tag = declaration.parameters.get(i).type;
            if(tag.equals(Tag.INT) && !(arguments.get(i) instanceof Integer)){
                ErrorHandler.error(declaration.parameters.get(i).line, "parameters type don't match");
                System.exit(1);
            }
            if(tag.equals(Tag.DOUBLE) && !(arguments.get(i) instanceof Double)){
                ErrorHandler.error(declaration.parameters.get(i).line, "parameters type don't match");
                System.exit(1);
            }
            if(tag.equals(Tag.CHAR) && !(arguments.get(i) instanceof Character)){
                ErrorHandler.error(declaration.parameters.get(i).line, "parameters type don't match");
                System.exit(1);
            }

            CustomVar var = new CustomVar(tag, arguments.get(i));
      	    environment.define(declaration.parameters.get(i).lexeme, var);
    	}

        interpreter.executeBlock(declaration.body, environment);

    	return null;
  	}
}