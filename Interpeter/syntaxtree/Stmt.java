package syntaxtree;

import lexer.Token;
import java.util.List;

public abstract class Stmt {


 	public interface Visitor<R> {
        public R visitExpressionStmt(Expression expr);
        public R visitPrintfStmt(Printf print);
        public R visitVarStmt(Var stmt);
        public R visitBlockStmt(Block stmt);
        public R visitIfStmt(Stmt.If stmt);
        public R visitWhileStmt(Stmt.While stmt);
        public R visitArrStmt(Stmt.ArrStmt stmt);
        public R visitFunctionStmt(Stmt.Function stmt);
        public R visitReturnStmt(Stmt.Return stmt);
    }

     public static class Function extends Stmt{
        public Function(Token name, List<Token> parameters, List<Stmt> body){
            this.name=name;
            this.parameters=parameters;
            this.body=body;
        }
        public <R> R accept(Visitor<R> visitor){
            return visitor.visitFunctionStmt(this);
        }
        final Token name;
        final List<Token> parameters;
        final List<Stmt> body;
    }

    public static class Return extends Stmt{
        public Return(Token keyword,Expr value){
            this.keyword=keyword;
            this.value=value;
        }
        public <R> R accept(Visitor<R> visitor){
            return visitor.visitReturnStmt(this);
        }
        final Token keyword;
        final Expr value;
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

    public static class ArrStmt extends Stmt {

        public ArrStmt(Token name, int length){
            this.name = name;
            this.length = length;
        }

        public <R> R accept(Visitor<R> visitor){
            return visitor.visitArrStmt(this);
        }

        public final Token name;
        public final int length;
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