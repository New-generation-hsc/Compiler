package syntaxtree;

import lexer.Token;
import java.util.List;

public abstract class Stmt {


 	interface Visitor<R> {
        public R visitExpressionStmt(Expression expr);
        public R visitPrintfStmt(Printf print);
        public R visitVarStmt(Var stmt);
        public R visitBlockStmt(Block stmt);
        public R visitIfStmt(Stmt.If stmt);
        public R visitWhileStmt(Stmt.While stmt);
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

    public static class Block extends Stmt {

        public Block(List<Stmt> statements){
            this.statements = statements;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitBlockStmt(this);
        }

        final List<Stmt> statements;
    }

    public static class If extends Stmt {

        public If(Expr condition, Stmt thenBranch, Stmt elseBranch){
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitIfStmt(this);
        }

        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;
    }

    public static class While extends Stmt {

        public While(Expr condition, Stmt body){
            this.condition = condition;
            this.body = body;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitWhileStmt(this);
        }

        final Expr condition;
        final Stmt body;
    }

    public abstract <R> R accept(Visitor<R> visitor);
}