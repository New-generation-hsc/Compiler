package generation;

import java.util.Hashtable;

public class SymbolTable {

	public final SymbolTable enclosing;
  	private final Map<String, Attribute> values = new HashMap<>();

  	private class Info {
  		private String type;
  		private String kind;
  		private int index;
  	}

  	public void define(String name, Attribute value) {

      if(values.containsKey(name)){
          System.out.println("Duplicated define");
          System.exit(1);
      }

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