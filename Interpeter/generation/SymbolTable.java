package generation;

import java.util.Hashtable;

public class SymbolTable {

	public final SymbolTable parent;
	private final Hashtable<String, Info> table = new Hashtable<>();

	private int staticIndex = 0, tempIndex = 0;

	public class Info {
		public String type;
		public String kind;
		public int index;

		public Info(String t, String k, int i){
				type = t;
				kind = k;
				index = i;
		}

		@Override
		public String toString(){
			return "< " + type + ", " + kind + ", " + index + ">";
		}
	}

	public void define(String name, String type, String kind){

		Info info = null;

		if(kind.equals("static")) info = new Info(type, kind, staticIndex++);
		else if(kind.equals("temp")) info = new Info(type, kind, tempIndex++);
		else{
			System.out.println("unknown kind");
			System.exit(1);
		}

		//System.out.println("localIndex -> " + localIndex);

		table.put(name, info);

		//System.out.println(info);
	}


	public int varCount(String kind){
		switch(kind){
			case "static": return staticIndex;
			case "temp": return tempIndex;
			default: return -1;
		}
	}

	public String kindOf(String name){
		if(table.containsKey(name)){
			return table.get(name).kind;
		}

		if(parent != null) return parent.kindOf(name);

		return "KIND_ERROR";
	}

	public String typeOf(String name){
		if(table.containsKey(name)){
			return table.get(name).type;
		}

		if(parent != null) return parent.typeOf(name);

		return "TYPE_ERROR";
	}

	public int indexOf(String name){
		if(table.containsKey(name)){
			return table.get(name).index;
		}

		if(parent != null) return parent.indexOf(name);

		System.out.println("error");

		return 0;
	}

	public SymbolTable() {
		 parent = null;
	}

	public SymbolTable(SymbolTable enclosing) {
		 this.parent = enclosing;
	}
}