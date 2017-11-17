
package syntaxtree;

import lexer.Tag;
import java.util.Map;
import java.util.HashMap;

public class CustomArray extends Attribute {

	public final int length;
	public Map<Integer, Object> map;

	public CustomArray(Tag tag, int length){
		super(tag);
		this.length = length;
		map = new HashMap<>();
	}

	public Object get(int index){
		if(index < 0 || index >= length){
			System.out.println("Array bound out");
			System.exit(1);
		}

		if(map.containsKey(index)){
			return map.get(index);
		}

		return null;
	}

	public void put(int index, Object value){
		if(index < 0 || index >= length){
			System.out.println("Array bound out");
			System.exit(1);
		}

		map.put(index, value);
	}

	@Override
	public String toString(){

		String text = "";
		for(Integer key : this.map.keySet()){
			text += key + " -> " + map.get(key).toString() + ",";
		}

		return text;
	}
}