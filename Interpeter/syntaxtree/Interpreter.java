package syntaxtree;

import exception.RuntimeError;
import lexer.Token;
import lexer.Tag;
import java.util.List;

import logger.ErrorHandler;
import exception.TypeCastException;
import exception.TypeMatchError;
import exception.UnAssignmentError;
import syntaxtree.Attribute;
import syntaxtree.CustomVar;
import syntaxtree.CustomArray;

import utils.Transform;
import java.util.List;
import java.util.ArrayList;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

	final Environment globals = new Environment();

	private Environment environment = globals;

	public void interpret(List<Stmt> statements){

		try{
			for(Stmt statement : statements){
				execute(statement);
			}
		}catch(RuntimeError error){
			ErrorHandler.runtimeError(error);
			System.exit(1);
		}
	}

	@Override
	public Void visitFunctionStmt(Stmt.Function stmt){
		CFunction function = new CFunction(stmt);

		CustomVar func = new CustomVar(Tag.FUNC, function);
		environment.define(stmt.name.lexeme, func);
		return null;
	}

	@Override
	public Void visitReturnStmt(Stmt.Return stmt){
		return null;
	}

	@Override
	public Void visitVarStmt(Stmt.Var stmt){
		Object value = null;

		if(stmt.initializer != null){
			value = evaluate(stmt.initializer);
		}

		CustomVar var = new CustomVar(stmt.name.type, value);

		environment.define(stmt.name.lexeme, var);
		return null;
	}

	@Override
	public Void visitArrStmt(Stmt.ArrStmt stmt){

		CustomArray arr = new CustomArray(stmt.name.type, stmt.length);

		environment.define(stmt.name.lexeme, arr);
		return null;
	}

	@Override
	public Void visitWhileStmt(Stmt.While stmt){
		while(isTruthy(evaluate(stmt.condition))){
			execute(stmt.body);
		}
		return null;
	}

	@Override
	public Void visitIfStmt(Stmt.If stmt){
		if(isTruthy(evaluate(stmt.condition))){
			execute(stmt.thenBranch);
		}else if(stmt.elseBranch != null){
			execute(stmt.elseBranch);
		}

		return null;
	}

	@Override
	public Void visitExpressionStmt(Stmt.Expression stmt){
		evaluate(stmt.expression);
		return null;
	}

	@Override
	public Void visitPrintfStmt(Stmt.Printf stmt){
		Object value = evaluate(stmt.expression);

		System.out.println(stringify(value));
		return null;
	}

	@Override
  	public Void visitBlockStmt(Stmt.Block stmt) {
    	executeBlock(stmt.statements, new Environment(environment));
    	return null;
  	}

	@Override
  	public Object visitAssignExpr(Expr.Assign expr) {
    	Object value = evaluate(expr.value);

    	Tag type = Tag.INT;

    	if(value instanceof Integer)
    		type = Tag.INT;
    	else if(value instanceof Double)
    		type = Tag.DOUBLE;
    	else if(value instanceof Character)
    		type = Tag.CHAR;

    	CustomVar var = new CustomVar(type, value);
    	environment.assign(expr.name, var);

    	return value;
  	}

  	@Override
  	public Object visitAssignArrayExpr(Expr.AssignArray expr){

  		Object value = evaluate(expr.value);

  		System.out.println("the array assignment value is : -> " + stringify(value));

  		Tag type = Tag.INT;

    	if(value instanceof Integer)
    		type = Tag.INT;
    	else if(value instanceof Double)
    		type = Tag.DOUBLE;
    	else if(value instanceof Character)
    		type = Tag.CHAR;

    	Attribute attr = environment.get(expr.name);
    	if(attr instanceof CustomArray && type.equals(attr.tag)){
    		CustomArray arr = (CustomArray)attr;
    		arr.put(expr.index, value);
    	}else {
    		ErrorHandler.error(expr.name.line, "invalid assignment expression");
    		System.exit(1);
    	}

    	return value;
  	}

	@Override
	public Object visitBinaryExpr(Expr.Binary expr){
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);

		if(left instanceof Character){
			ErrorHandler.error(expr.operator.line, "Invalid expression operand");
		}

		if(right instanceof Character ){
			ErrorHandler.error(expr.operator.line, "Invalid expression operand");
		}

		switch(expr.operator.type){
			case MINUS:
				if((left instanceof Integer) && (right instanceof Integer))
					return (int)left - (int)right;
				left = transform(left); right = transform(right);
				return (double)left - (double)right;
			case PLUS:
				if((left instanceof Integer) && (right instanceof Integer))
					return (int)left + (int)right;
				left = transform(left); right = transform(right);
				return (double)left + (double)right;
			case SLASH:
				if((left instanceof Integer) && (right instanceof Integer))
					return (int)left / (int)right;
				left = transform(left); right = transform(right);
				return (double)left / (double)right;
			case MUL:
				if((left instanceof Integer) && (right instanceof Integer))
					return (int)left * (int)right;
				left = transform(left); right = transform(right);
				return (double)left * (double)right;
			case GT:
				left = transform(left); right = transform(right);
				return (double)left > (double)right;
			case GE:
				left = transform(left); right = transform(right);
				return (double)left >= (double)right;
			case LT:
				left = transform(left); right = transform(right);
				return (double)left < (double)right;
			case LE:
				left = transform(left); right = transform(right);
				return (double)left <= (double)right;
			default:
				break;
		}

		if(!(left instanceof Boolean) || !(right instanceof Boolean)){
			ErrorHandler.error(expr.operator.line, "The operand must be boolean expression");
			System.exit(1);
		}

		if(expr.operator.type.equals(Tag.AND)){
			return (boolean)left && (boolean)right;
		}else if(expr.operator.type.equals(Tag.OR)){
			return (boolean)left || (boolean)right;
		}

		// unreachable
		return null;
	}

	@Override
	public Object visitGroupingExpr(Expr.Grouping expr){
		return evaluate(expr.expression);
	}

	@Override
	public Object visitLiteralExpr(Expr.Literal expr){
		return expr.value;
 	}

 	@Override
 	public Object visitArrayExpr(Expr.Array expr){
 		Attribute attr = environment.get(expr.name);

 		CustomArray arr = (CustomArray)attr;
 		if(arr.get(expr.index) == null){
 			ErrorHandler.unAssignmentError(new UnAssignmentError(expr.name, "variable must be assigned before reference"));
 			System.exit(1);
 		}

 		return arr.get(expr.index);
 	}

	@Override
	public Object visitUnaryExpr(Expr.Unary expr){
		Object right = evaluate(expr.right);

		switch(expr.operator.type){
			case MINUS:
				if (right instanceof Integer)
					return -(int)right;
			    return -(double)right;
			case NOT:
				return !isTruthy(right);
		}

		// Unreachable
		return null;
	}

	@Override
	public Object visitCallExpr(Expr.Call expr){
		Object callee = evaluate(expr.callee);

		List<Object> arguments = new ArrayList<>();
		for(Expr argument : expr.arguments){
			arguments.add(evaluate(argument));
		}

		if(!(callee instanceof CCallable)){
			System.out.println("Can only call functions");
			System.exit(1);
		}

		CCallable function = (CCallable) callee;
		return function.call(this, arguments);
	}

	@Override
  	public Object visitVariableExpr(Expr.Variable expr) {
    	
    	Attribute attr = environment.get(expr.name);

    	CustomVar var = (CustomVar)attr;
    	return var.value;
  	} 


	private Object evaluate(Expr expr){
		return expr.accept(this);
	}


	void executeBlock(List<Stmt> statements, Environment environment) {
    	Environment previous = this.environment;
    	try {
      		this.environment = environment;

      		for (Stmt statement : statements) {
        		execute(statement);
      		}
    	}
    	finally {
      		this.environment = previous;
    	}
  	}

 


	private boolean isTruthy(Object right){
		if(right == null) return false;
		if(right instanceof Boolean) return (boolean) right;
		return true;
	}

	private boolean isEqual(Object a, Object b){
		if(a == null && b == null) return true;
		if(a == null) return false;

		return a.equals(b);
	}

	private void checkNumberOperand(Token operator, Object operand){
		if(operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}

	private void checkNumberOperands(Token operator, Object left, Object right){
		if(left instanceof Double && right instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}

	private String stringify(Object object){
		if(object == null) return "";

		if(object instanceof Double){
			String text = object.toString();
			if(text.endsWith(".0")){
				text = text.substring(0, text.length() - 2);
			}

			return text;
		}

		return object.toString();

	}

	public Object transform(Object object){

		if(object instanceof Integer){
			Integer o = (Integer)object;
			return new Double(o.intValue());
		}

		return object;
	}

	private void execute(Stmt stmt){
		stmt.accept(this);
	}
}