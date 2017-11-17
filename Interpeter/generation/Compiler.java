/**
 * This is the class that generation the assemble language
 * according to the parsing tree
 */

package generation;

import syntaxtree.Expr;
import syntaxtree.Stmt;
import lexer.Tag;
import lexer.Token;

import exception.RuntimeError;
import logger.ErrorHandler;

import java.util.List;

import java.util.Map;
import java.util.HashMap;

public class Compiler implements Expr.Visitor<Void>, Stmt.Visitor<Void> {

    private VMWriter writer;
    private int level = 0;

    private int labelCount = 0;

    final SymbolTable globals = new SymbolTable();
    private SymbolTable table = globals;

    private Map<String, Stmt.Function> map = new HashMap<>();

    public Compiler(){
        writer = new VMWriter();
    }

	public void compile(List<Stmt> statements){
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
	public Void visitExpressionStmt(Stmt.Expression expr){
		evaluate(expr.expression);
		return null;
	}

	@Override
    public Void visitPrintfStmt(Stmt.Printf stmt){
        
        if(stmt.expression instanceof Expr.Literal){
            Expr.Literal expr = (Expr.Literal) stmt.expression;
            if(expr.value instanceof Integer){
                writer.writePush("constant", (int)expr.value);
                writer.writePrintInt();
            }
        }

    	return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt){

        Token name = stmt.name;
        String type = transform(name.type);
        String kind = getKind(this.level);

        table.define(name.lexeme, type, kind);

        if(stmt.initializer != null) {
            evaluate(stmt.initializer);
            writer.writePop(table.kindOf(name.lexeme), table.indexOf(name.lexeme));
        }

    	return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt){
    	SymbolTable previous = table;

        SymbolTable scope = new SymbolTable(previous);
        this.level = this.level + 1;
        this.table = scope;

        for (Stmt statement : stmt.statements) {
            execute(statement);
        }

        this.table = previous;
        this.level = this.level - 1;

        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt){
        evaluate(stmt.condition);
        writer.writeArithmetic("not");
        writer.writeIf("L", labelCount);
        execute(stmt.thenBranch);
        writer.writeGoto("L", labelCount+1);
        writer.writeLabel("L", labelCount);
        labelCount++;
        if(stmt.elseBranch != null){
            execute(stmt.elseBranch);
        }
        writer.writeLabel("L", labelCount);
        labelCount++;
    	return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt){
        writer.writeLabel("L", labelCount);
        evaluate(stmt.condition);
        writer.writeArithmetic("not");
        writer.writeIf("L", labelCount + 1);
        execute(stmt.body);
        writer.writeGoto("L", labelCount);
        labelCount++;
        writer.writeLabel("L", labelCount);
        labelCount++;
    	return null;
    }

    @Override
    public Void visitArrStmt(Stmt.ArrStmt stmt){
    	return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt){
        
        map.put(stmt.name.lexeme, stmt);

        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt){
        if(stmt.value instanceof Expr.Literal){
            Expr.Literal expr = (Expr.Literal) stmt.value;
            if(expr.value instanceof Integer){
                writer.writePush("constant", (int)expr.value);
            }
        }

        if(stmt.value instanceof Expr.Variable){
            Expr.Variable variable = (Expr.Variable)stmt.value;
            writer.writePush(table.kindOf(variable.name.lexeme), table.indexOf(variable.name.lexeme));
        }

    	return null;
    }

	@Override
	public Void visitBinaryExpr(Expr.Binary expr){

		evaluate(expr.left);
		evaluate(expr.right);

		switch(expr.operator.type){
            case PLUS:
                writer.writeArithmetic("add"); break;
            case MINUS:
                writer.writeArithmetic("sub"); break;
            case MUL:
                writer.writeArithmetic("call Math.multiply 2"); break;
            case SLASH:
                writer.writeArithmetic("call Math.divide 2"); break;
            case GT:
                writer.writeArithmetic("gt"); break;
            case LT:
                writer.writeArithmetic("lt"); break;
            case EQ:
                writer.writeArithmetic("eq"); break;
        }

		return null;

	}

	@Override
	public Void visitUnaryExpr(Expr.Unary expr){
		evaluate(expr.right);
		switch(expr.operator.type){
            case MINUS:
                writer.writeArithmetic("neg"); break;
            case NOT:
                writer.writeArithmetic("not"); break;
        }
		return null;
	}

	@Override
    public Void visitGroupingExpr(Expr.Grouping expr){
    	evaluate(expr.expression);
    	return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.Literal expr){
    	if(expr.value instanceof Integer){
            writer.writePush("constant", (int)expr.value);
        }
    	return null;
    }
    
    @Override
    public Void visitVariableExpr(Expr.Variable expr){
    	writer.writePush(table.kindOf(expr.name.lexeme), table.indexOf(expr.name.lexeme));
    	return null;
    }

    @Override
    public Void visitAssignExpr(Expr.Assign expr){
    	evaluate(expr.value);
    	writer.writePop(table.kindOf(expr.name.lexeme), table.indexOf(expr.name.lexeme));
    	return null;
    }

    @Override
    public Void visitArrayExpr(Expr.Array expr){
    	return null;
    }

    @Override
    public Void visitAssignArrayExpr(Expr.AssignArray expr){
    	return null;
    }

    @Override
    public Void visitCallExpr(Expr.Call expr){

        for(Expr arg : expr.arguments){
            evaluate(arg);
        }

        Expr.Variable name = (Expr.Variable)expr.callee;
        executeFunction(map.get(name.name.lexeme));
    	return null;
    }

    private void generate(String instruction){
    	System.out.println(instruction);
    }

    private void evaluate(Expr expr){
    	expr.accept(this);
    }

    private void execute(Stmt stmt){
    	stmt.accept(this);
    }

    private String transform(Tag tag){
        if(tag.equals(Tag.INT)) return "int";
        else if(tag.equals(Tag.DOUBLE)) return "double";
        else if(tag.equals(Tag.CHAR)) return "char";
        else if(tag.equals(Tag.FLOAT)) return "float";

        return null;
    }

    private String getKind(int level){
        if(level > 0) return "temp";
        else return "static";
    }

    private void executeFunction(Stmt.Function stmt){

        SymbolTable previous = table;

        SymbolTable scope = new SymbolTable(previous);
        this.level = this.level + 1;
        this.table = scope;

        for(Token param : stmt.parameters){
            this.table.define(param.lexeme, transform(param.type), "temp");
        }

        int size = stmt.parameters.size();
        for(int i = size - 1; i >= 0; i--){
            String name = stmt.parameters.get(i).lexeme;
            writer.writePop(table.kindOf(name), table.indexOf(name));
        }

        for (Stmt statement : stmt.body) {
            execute(statement);
        }

        this.table = previous;
        this.level = this.level - 1;
    }
}