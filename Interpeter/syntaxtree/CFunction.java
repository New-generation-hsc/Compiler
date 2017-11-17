
package syntaxtree;

import java.util.List;
import utils.Transform;
import lexer.Tag;

public class CFunction implements CCallable {

	private final Stmt.Function declaration;

	public CFunction(Stmt.Function declaration){
		this.declaration = declaration;
	}

	@Override
	public Object call(Interpreter interpreter, List<Object> arguments){
		Environment environment = new Environment(interpreter.globals);


		for(int i = 0; i < declaration.parameters.size(); i++){
			Tag tag = Transform.solve(arguments.get(i));
			CustomVar var = new CustomVar(tag, arguments.get(i));

			environment.define(declaration.parameters.get(i).lexeme, var);
		}

		interpreter.executeBlock(declaration.body, environment);
		return null;
	}
}