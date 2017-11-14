package syntaxtree;

import exception.RuntimeError;
import lexer.Token;
import java.util.List;

import logger.ErrorHandler;
import exception.TypeCastException;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

	private Environment environment = new Environment();

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
	public Void visitVarStmt(Stmt.Var stmt){
		Object value = null;

		if(stmt.initializer != null){
			value = evaluate(stmt.initializer);
		}

		environment.define(stmt.name.lexeme, value);
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
	public Object visitBinaryExpr(Expr.Binary expr){
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);

		left = transform(left);
		right = transform(right);

		if(!(left instanceof Double) || !(right instanceof Double)){
			ErrorHandler.patternMatchError(new TypeCastException(expr.operator, "the operand must be a number"));
			System.exit(1);
		}

		switch(expr.operator.type){
			case MINUS:
				return (double)left - (double)right;
			case PLUS:
				return (double)left + (double)right;
			case SLASH:
				return (double)left / (double)right;
			case MUL:
				return (double)left * (double)right;
			case GT:
				return (double)left > (double)right;
			case GE:
				return (double)left >= (double)right;
			case LT:
				return (double)left < (double)right;
			case LE:
				return (double)left <= (double)right;
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
  	public Object visitVariableExpr(Expr.Variable expr) {
    	return environment.get(expr.name);
  	}
  
	// @Override
	// public void visitWhileStmt(Stmt.While stmt){
	// 	while(isTruthy(evaluate(stmt.condition))){
	// 		execute(stmt.body);
	// 	}
	// 	return null;
	// } 


	private Object evaluate(Expr expr){
		return expr.accept(this);
	}


	// void executeBlock(List<Stmt> statements, Environment environment) {
 //    	Environment previous = this.environment;
 //    	try {
 //      		this.environment = environment;

 //      		for (Stmt statement : statements) {
 //        		execute(statement);
 //      		}
 //    	}
 //    	finally {
 //      		this.environment = previous;
 //    	}
 //  	}

 //  	@Override
 //  	public Void visitBlockStmt(Stmt.Block stmt) {
 //    	executeBlock(stmt.statements, new Environment(environment));
 //    	return null;
 //  	}

	// @Override
 //  	public Void visitVarStmt(Stmt.Var stmt) {
 //    	Object value = null;
 //    	if (stmt.initializer != null) {
 //      		value = evaluate(stmt.initializer);
 //    	}

 //    	environment.define(stmt.name.lexeme, value);
 //    	return null;
 //  	}

 //  	@Override
 //  	public Object visitAssignExpr(Expr.Assign expr) {
 //    	Object value = evaluate(expr.value);

 //    	environment.assign(expr.name, value);
 //    	return value;
 //  	}


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