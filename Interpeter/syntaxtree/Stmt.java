package syntaxtree;

public abstract class Stmt {


 	interface Visitor<R> {
        public R visitExpressionStmt(Expression expr);
        public R visitPrintfStmt(Printf print);
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

    public abstract <R> R accept(Visitor<R> visitor);
}