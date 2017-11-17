package test;

import lexer.Scanner;
import lexer.Token;
import lexer.Tag;

import java.util.List;
import java.util.ArrayList;

import syntaxtree.Stmt;
import syntaxtree.Interpreter;
import parser.Parser;

import generation.Compiler;

public class InterpretTest {
	
	public static void main(String[] args){
		
		// Scanner scanner = new Scanner();
		// System.out.println(scanner.read(scanner.PATH));

		// Token token = scanner.scan();

		// while(token != null){
		// 	System.out.println(token);
		// 	token = scanner.scan();
		// }

		Parser parser = new Parser();

		List<Stmt> stmts = parser.parse();
		
		Interpreter interpreter = new Interpreter();

		interpreter.interpret(stmts);
	}
}