package syntaxtree;

import java.util.HashMap;
import java.util.Map;
import exception.RuntimeError;
import exception.TypeMatchError;
import logger.ErrorHandler;

import lexer.Token;
import lexer.Tag;

import utils.Transform;

public class Environment {

	  public final Environment enclosing;
  	private final Map<String, Attribute> values = new HashMap<>();

  	public void define(String name, Attribute value) {

      if(values.containsKey(name)){
          System.out.println("Duplicated define");
          System.exit(1);
      }

<<<<<<< HEAD
=======
  	public void define(String name, Object value) {

      if(values.containsKey(name)){
        System.out.println("Duplicate define");
        System.exit(1);
      }

>>>>>>> b1b9c30301ed2e62e4f88dbd1cb4cacdabae4a45
    	values.put(name, value);
  	}

  	public Attribute get(Token name) {
    	if (values.containsKey(name.lexeme)) {
      		return values.get(name.lexeme);
    	}

    	if (enclosing != null) return enclosing.get(name);

    	throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
  	}

  	public void assign(Token name, Attribute value) {

    	if (values.containsKey(name.lexeme)) {
          values.put(name.lexeme, value);
      		return;
    	}

    	if (enclosing != null) {
    		  enclosing.assign(name, value);
      		return;
    	}
    	
    	throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
  	}

  	public Environment() {
    	enclosing = null;
  	}

  	public Environment(Environment enclosing) {
    	this.enclosing = enclosing;
  	}
}