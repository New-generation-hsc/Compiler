/**
 * This file is part of the syntax parse
 *
 * In the syntax parse, we choose the recurisve decesent analysis
 * we will construct the whole program from the bottom to the top
 * that is to say contructing from the basic arithmetic expression to the whole program
 */

package parser;

import lexer.Tag;
import lexer.Token;
import lexer.Scanner;
import lexer.Getsiyuan;

import syntaxtree.Expr;
import syntaxtree.Stmt;
import logger.ErrorHandler;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import exception.TypeCastException;

public class Parser {

	private Token token; // the current token that need to parse
	private Token next;  // the next token
	private Scanner scanner; // the scanner scan the c program file and generate token
    public Getsiyuan quanternary = new Getsiyuan();

	public Parser(){
		scanner = new Scanner();
		token = null;
		next = scanner.scan(); // get the first token
	}

	public List<Stmt> parse(){

		List<Stmt> statements = new ArrayList<>();

		while(next != null){
			statements.add(declaration());
		}

		return statements;
	}

	private Stmt declaration(){

		if(accept(Tag.INT) || accept(Tag.CHAR) || accept(Tag.DOUBLE) || accept(Tag.FLOAT)){
			this.update();
			if(accept(Tag.LBRACKET))
				return arrDeclaration();
			return varDeclaration();
		}

		return statement();
	}

	public Stmt arrDeclaration(){
		Tag type = token.type; // record the type of current array

		expect(Tag.LBRACKET);
		this.update();
		expect(Tag.NUM);
		this.update();
		Integer length = (Integer)token.literal;
		expect(Tag.RBRACKET);
		this.update();

		expect(Tag.ID);
		this.update();
		Token name = new Token(type, token.lexeme, token.literal, token.line);

		expect(Tag.SEMICOLON);
		this.update();

		return new Stmt.ArrStmt(name, length);
	}

	private Stmt varDeclaration(){

		expect(Tag.ID);
		Token name = new Token(token.type, next.lexeme, next.literal, next.line);
		this.update();
		quanternary.Push(token);

		Expr initializer = null;
		if(accept(Tag.ASSIGN)){
			this.update();
			initializer = expression();
			quanternary.ASSI();
		}

		expect(Tag.SEMICOLON);
		this.update();

		return new Stmt.Var(name, initializer);
	}

	private Stmt statement(){
		if(accept(Tag.PRINTF)){
			this.update();
			expect(Tag.LPAREN);
			this.update();
			Stmt stmt = printfStatement();
			expect(Tag.RPAREN);
			this.update();
			expect(Tag.SEMICOLON);
			this.update();

			return stmt;
		}

		if(accept(Tag.FUNC)){
			return function();
		}

		if(accept(Tag.LBRACE)){
			this.update();
			return new Stmt.Block(block());
		}

		if(accept(Tag.WHILE)){
			quanternary.WH();
			this.update();
			return whileStatement();
		}

		if(accept(Tag.IF)){
			this.update();
			return ifStatement();
		}

		if(accept(Tag.FOR)){
			this.update();
			return forStatement();
		}

		return expressionStatement();
	}

	private Stmt function(){
		this.update();
		if(!(accept(Tag.INT) || accept(Tag.CHAR) || accept(Tag.DOUBLE) || accept(Tag.FLOAT))){
			ErrorHandler.error(next.line, "Missing the return data type" );
			
		}
		this.update();
        expect(Tag.ID);
		this.update();

		Token identifier = token;

		expect(Tag.LPAREN);
		this.update();
		List<Token> parameters = new ArrayList<>();
		    if(!accept(Tag.RPAREN)){
			  do {
				if(accept(Tag.COMMA))
					this.update();
				if(accept(Tag.INT) || accept(Tag.DOUBLE) || accept(Tag.CHAR)){
					this.update();
				}else {
					System.out.println("Missing the keyword for function");
					System.exit(1);
				}

				Tag tag = token.type;
				expect(Tag.ID);
				this.update();
				Token param = new Token(tag, token.lexeme, token.literal, token.line);
				parameters.add(param);
			}while(accept(Tag.COMMA));
		}

		expect(Tag.RPAREN);
		this.update();

		expect(Tag.LBRACE);
		this.update();

		List<Stmt> body = block();
		return new Stmt.Function(identifier, parameters, body);
        
		
	}

	// the while loop
	private Stmt whileStatement(){
		expect(Tag.LPAREN);
		this.update();
		//System.out.println("accept the token -> " + token);
		Expr condition = expression();
		expect(Tag.RPAREN);
		quanternary.DO();
		this.update();
		//System.out.println("accept the token -> " + token);
		Stmt body = statement();
		quanternary.WE();

		return new Stmt.While(condition, body);
	}

	// the for loop
	private Stmt forStatement(){
		expect(Tag.LPAREN);
		this.update();

		Stmt initializer = null;

		if(accept(Tag.SEMICOLON)){
			this.update();
			initializer = null;
		}else if(accept(Tag.INT) || accept(Tag.CHAR) || accept(Tag.DOUBLE) || accept(Tag.FLOAT)){
			this.update();
			initializer =  varDeclaration();
		}else {
			initializer = expressionStatement();
		}

		Expr condition = null;
		if(!accept(Tag.SEMICOLON)){
			condition = expression();
		}

		expect(Tag.SEMICOLON);
		this.update();
		quanternary.YES();
		quanternary.JUMPS4();

		Expr increment = null;
		if(!accept(Tag.RPAREN)){
			increment = expression();
			quanternary.JUMPS2();
		}

		expect(Tag.RPAREN);
		this.update();

		Stmt body = statement();
		quanternary.JUMPS3();

		if(increment != null){
			body = new Stmt.Block(Arrays.asList(
				body, new Stmt.Expression(increment)));
		}

		if(condition == null) condition = new Expr.Literal(1);
		body = new Stmt.While(condition, body);

		if(initializer != null){
			body = new Stmt.Block(Arrays.asList(initializer, body));
		}

		return body;
	}

	//if
	private Stmt ifStatement(){

		this.expect(Tag.LPAREN);
		this.update();
		Expr condition =expression();
		this.expect(Tag.RPAREN);
		quanternary.IF();
		this.update();
		Stmt thenBranch = statement();

		Stmt elseBranch = null;

		if(accept(Tag.ELSE)){
			quanternary.EL();
			this.update();

			elseBranch = statement();
			quanternary.IE();
		}

		return new Stmt.If(condition, thenBranch, elseBranch);
	}

	private List<Stmt> block() {
    	List<Stmt> statements = new ArrayList<>();

    	while (!accept(Tag.RBRACE)) {
      		statements.add(declaration());
    	}

    	expect(Tag.RBRACE);
    	this.update();
    	return statements;
  	}

	private Stmt printfStatement(){
		Expr value = expression();

		return new Stmt.Printf(value);
	}

	private Stmt expressionStatement(){
		Expr expr = expression();
		expect(Tag.SEMICOLON);
		this.update();
		return new Stmt.Expression(expr);
	}

	private Expr expression(){
		return assignment();
	}

	private Expr assignment() {

    	Expr expr = logical();

    	if (accept(Tag.ASSIGN)) {
      		Token equals = token;
      		this.update();

      		Expr value = assignment();
      		quanternary.ASSI();

      		if (expr instanceof Expr.Variable) {
        		Token name = ((Expr.Variable)expr).name;
        		return new Expr.Assign(name, value);
      		}else if(expr instanceof Expr.Array){
      			Expr.Array arr = (Expr.Array)expr;
      			return new Expr.AssignArray(arr.name, value, arr.index);
      		}

      		ErrorHandler.error(equals.line, "Invalid assignment target");
    	}

    	return expr;
	}

	// && || operation
	private Expr logical(){

		Expr expr = equality();
		while(accept(Tag.AND) || accept(Tag.OR)){
			this.update();
			System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = equality();
			quanternary.GEQ(operator);
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}


	// != , ==
	private Expr equality(){

		Expr expr = comparison();
		while(accept(Tag.NE) || accept(Tag.EQ)){
			this.update();
			//System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = comparison();
			quanternary.GEQ(operator);
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// <, >, <=, >=
	private Expr comparison(){

		Expr expr = addition();
		while(accept(Tag.LT) || accept(Tag.LE) || accept(Tag.GT) || accept(Tag.GE)){
			this.update();
			//System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = addition();
			quanternary.GEQ(operator);
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// +, -
	private Expr addition(){

		Expr expr = multiplication();
		while(accept(Tag.PLUS) || accept(Tag.MINUS)){
			this.update();
			//System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = multiplication();
			quanternary.GEQ(operator);
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}

	// *, /
	private Expr multiplication(){

		Expr expr = unary();
		while(accept(Tag.MUL) || accept(Tag.SLASH)){
			this.update();
			//System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = unary();
			quanternary.GEQ(operator);
			expr = new Expr.Binary(expr, operator, right);
		}

		return expr;
	}
	
	// !, -
	private Expr unary(){

		if(accept(Tag.NOT) || accept(Tag.MINUS)){
			this.update();
			//System.out.println("accept the token -> " + token);
			Token operator = token;
			Expr right = primary();
			quanternary.HEQ(operator);
			return new Expr.Unary(operator, right);
		}

		return call();
	}

	// call function
	private Expr call(){
		Expr expr = primary();

		while(true){
			if(accept(Tag.LPAREN)){
				quanternary.CALL(token);
				quanternary.RETURN();
				this.update();
				//System.out.println("the next token -> " + next);
				expr = finishCall(expr);
			}else {
				break;
			}
		}

		return expr;
	}

	private Expr finishCall(Expr callee){
		List<Expr> arguments = new ArrayList<>();

		if(!accept(Tag.RPAREN)){
			do {
				if(accept(Tag.COMMA))
					this.update();
				//System.out.println("current token -> " + token);
				arguments.add(expression());
			}while(accept(Tag.COMMA));
		}

		expect(Tag.RPAREN);
		this.update();

		return new Expr.Call(callee, arguments);
	}

	// the basic element
	private Expr primary(){

		if(accept(Tag.NUM) || accept(Tag.REAL) || accept(Tag.CHARACTER) || accept(Tag.STRING)){
			this.update();
			quanternary.Push(token);
			//System.out.println("accept the token -> " + token);
			return new Expr.Literal(token.literal);
		}

		if(accept(Tag.LPAREN)){
			this.update();
			Expr expr = expression();
			expect(Tag.RPAREN);
			this.update();
			return new Expr.Grouping(expr);
		}

		if(accept(Tag.ID)){
			Token name = next;
			this.update();
			quanternary.Push(name);
			if(accept(Tag.LBRACKET)){
				this.update();
				expect(Tag.NUM);
				this.update();
				quanternary.Push(token);


				Integer length = (Integer)token.literal;
				expect(Tag.RBRACKET);
				quanternary.ARR();
				this.update();

				//System.out.println("return the array expression");
				return new Expr.Array(name, length);
			}
			
			return new Expr.Variable(name);
		}

		//System.out.println(next);
		ErrorHandler.error(next.line, "Invalid expression");
		System.exit(1);

		// unreachable
		return null;
	}


	public void update(){
		token = next;
		next = scanner.scan();
	}

	/**
	 * the recursive decesent method must look forward a token, it can determine the next step
	 * so this function is that judge whether the next token match the expected token
	 * if match then return true, else return false
	 * @param  symbol the expected Tag
	 * @return        boolean
	 */
	public boolean accept(Tag symbol){
		if(next != null && next.type.equals(symbol)){ // if it accept the current token
			return true;
		}
		return false;
	}

	/**
	 * just for expecting some match symbol like ), ], }
	 * if it expect failed, then throw an error
	 * @param symbol the expected Tag
	 */
	public void expect(Tag symbol){
		if(!accept(symbol)) ErrorHandler.error(next.line, "Missing the symbol <" + symbol.toString() + ">.");
	}
}