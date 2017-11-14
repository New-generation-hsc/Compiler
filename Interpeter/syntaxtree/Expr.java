package syntaxtree;

import java.util.List;
import lexer.Token;

public abstract class Expr {
	
    interface Visitor<R> {
        public R visitBinaryExpr(Binary expr);
        public R visitGroupingExpr(Grouping expr);
        public R visitLiteralExpr(Literal expr);
        public R visitUnaryExpr(Unary expr);
        public R visitVariableExpr(Variable expr);
        public R visitAssignExpr(Assign expr);
    }

    public static class Assign extends Expr {
        public Assign(Token name, Expr value){
            this.name = name;
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitAssignExpr(this);
        }

        final Token name;
        final Expr value;
    }

    public static class Binary extends Expr {
        public Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitBinaryExpr(this);
	    }

	    final Expr left;
	    final Token operator;
	    final Expr right;
    }

    public static class Grouping extends Expr {
        public Grouping(Expr expression) {
            this.expression = expression;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitGroupingExpr(this);
	    }

    	final Expr expression;
    }

    public static class Literal extends Expr {
        public Literal(Object value) {
            this.value = value;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitLiteralExpr(this);
	    }

    	final Object value;
    }

    public static class Unary extends Expr {
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

    	public <R> R accept(Visitor<R> visitor) {
        	return visitor.visitUnaryExpr(this);
    	}

	    final Token operator;
	    final Expr right;
    }

    public static class Variable extends Expr {
        public Variable(Token name){
            this.name = name;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitVariableExpr(this);
        }

        public final Token name;

    }

    public abstract <R> R accept(Visitor<R> visitor);
}
