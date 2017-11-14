package syntaxtree;

import lexer.Token;

public abstract class Stmt {


 	interface Visitor<R> {
        public R visitExpressionStmt(Expression expr);
        public R visitPrintfStmt(Printf print);
        public R visitVarStmt(Var stmt);
    }

	public static class Expression extends Stmt {


        public Expression(Expr expression) {
            this.expression = expression;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitExpressionStmt(this);
	    }

	    public final Expr expression;
    }

    public static class Printf extends Stmt {

    	public Printf(Expr expression) {
            this.expression = expression;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitPrintfStmt(this);
	    }

	    public final Expr expression;

    }

    public static class Var extends Stmt {

        public Var(Token name, Expr initializer){
            this.name = name;
            this.initializer = initializer;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitVarStmt(this);
        }

        public final Token name;
        public final Expr initializer;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}