package syntaxtree;

import java.util.List;
import lexer.Token;

public abstract class Expr {
	
    public interface Visitor<R> {
        public R visitBinaryExpr(Binary expr);
        public R visitGroupingExpr(Grouping expr);
        public R visitLiteralExpr(Literal expr);
        public R visitUnaryExpr(Unary expr);
        public R visitVariableExpr(Variable expr);
        public R visitAssignExpr(Assign expr);
        public R visitArrayExpr(Array expr);
        public R visitAssignArrayExpr(AssignArray expr);
        public R visitCallExpr(Call expr);
    }

    public static class Assign extends Expr {
        public Assign(Token name, Expr value){
            this.name = name;
            this.value = value;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitAssignExpr(this);
        }

        public final Token name;
        public final Expr value;
    }

    public static class AssignArray extends Expr {

        public AssignArray(Token name, Expr value, int index){
            this.name = name;
            this.value = value;
            this.index = index;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitAssignArrayExpr(this);
        }

        public final Token name;
        public final Expr value;
        public final int index;
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

	    public final Expr left;
	    public final Token operator;
	    public final Expr right;
    }

    public static class Grouping extends Expr {
        public Grouping(Expr expression) {
            this.expression = expression;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitGroupingExpr(this);
	    }

    	public final Expr expression;
    }

    public static class Literal extends Expr {
        public Literal(Object value) {
            this.value = value;
        }

	    public <R> R accept(Visitor<R> visitor) {
	        return visitor.visitLiteralExpr(this);
	    }

    	public final Object value;
    }

    public static class Unary extends Expr {
        public Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

    	public <R> R accept(Visitor<R> visitor) {
        	return visitor.visitUnaryExpr(this);
    	}

	    public final Token operator;
	    public final Expr right;
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

    public static class Array extends Expr {

        public Array(Token name, int index){
            this.name = name;
            this.index = index;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitArrayExpr(this);
        }

        public final Token name;
        public final int index;
    }

    public static class Call extends Expr {
        public Call(Expr callee, List<Expr> arguments){
            this.callee = callee;
            this.arguments = arguments;
        }
        public <R> R accept(Visitor<R> visitor){
            return visitor.visitCallExpr(this);
        }

        public final Expr callee;
        public final List<Expr> arguments;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}
