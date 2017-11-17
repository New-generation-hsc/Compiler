/**
 * This is the class that generation the assemble language
 * according to the parsing tree
 */

package generation;

import syntaxtree.Expr;
import syntaxtree.Stmt;
import lexer.Tag;

import exception.RuntimeError;
import logger.ErrorHandler;

import java.util.List;

public class Compiler implements Expr.Visitor<Void>, Stmt.Visitor<Void> {

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
    	// Object value = evaluate(stmt.expression);

    	// if(value instanceof String){
    	// 	System.out.println("DATAS SEGMENT");
    	// 	String instruction = "\nSTRING" + count + " DB '" + value + "';";
    	// 	System.out.println(instruction);
    	// 	System.out.println("\nDATAS ENDS");
    	// }

    	// generate("MOV DX, OFFSET STRING" + count);
    	// generate("MOV AH, 09H");
    	// generate("INT 21H");

    	return null;
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt){
    	return null;
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt){
    	return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt){
    	return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt){
    	return null;
    }

    @Override
    public Void visitArrStmt(Stmt.ArrStmt stmt){
    	return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt){
    	return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt){
    	return null;
    }

	@Override
	public Void visitBinaryExpr(Expr.Binary expr){

		evaluate(expr.left);
		evaluate(expr.right);

		generate(expr.operator.lexeme);

		// switch(expr.operator.type){
		// 	case PLUS:
		// 		generate("MOV BX, " + right);
		// 		generate("POP AX");
		// 		generate("ADD AX, BX");
		// 		generate("PUSH AX");
		// 		return left;
		// 	case MINUS:
		// 		generate("MOV BX, " + right);
		// 		generate("POP AX");
		// 		generate("ADD AX, BX");
		// 		generate("PUSH AX");
		// 		return left;
		// 	default:
		// 		return left;
		// }
		return null;

	}

	@Override
	public Void visitUnaryExpr(Expr.Unary expr){
		evaluate(expr.right);
		generate(expr.operator.lexeme);
		return null;
	}

	@Override
    public Void visitGroupingExpr(Expr.Grouping expr){
    	evaluate(expr.expression);
    	return null;
    }

    @Override
    public Void visitLiteralExpr(Expr.Literal expr){
    	generate("push " + expr.value.toString());
    	return null;
    }
    
    @Override
    public Void visitVariableExpr(Expr.Variable expr){
    	generate("push " + expr.name.lexeme);
    	return null;
    }

    @Override
    public Void visitAssignExpr(Expr.Assign expr){
    	evaluate(expr.value);
    	generate("pop " + expr.name.lexeme);
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
    	for(Expr argument : expr.arguments){
    		evaluate(argument);
    	}

    	if(expr.callee instanceof Expr.Variable){
    		Expr.Variable var = (Expr.Variable)expr.callee;
    		generate("call " + var.name.lexeme);
    	}
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

    // private String move(String left, String right){
    // 	String instruction = "MOV " + right + ", " + left + ";";
    // 	generate(instruction);
    // 	return right;
    // }

  //   private void generateHeader(){
  //   	String header = "SSEG    SEGMENT" + 
		// 				"\nSTACK   DW      50 DUP (0)" + 
		// 				"\nSSEG    ENDS" + 
		// 				"\nCSEG    SEGMENT" + 
		// 				        "\n\t\tASSUME  CS:CSEG, ASSUME  SS:SSEG" + 
		// 				"\nSTART:  MOVE AX, DATAS;" +
		// 				"\n\tMOV DS, AX;";
		// System.out.println(header);
  //   }

  //   private void generateFooter(){
  //   	String footer = "\tMOV     AH, 4CH" + 
  //       				"\n\tINT     21H" + 
  //   					"\nCSEG    ENDS" + 
  //       						"\n\t\tEND     START";
  //       System.out.println(footer);
  //   }
}