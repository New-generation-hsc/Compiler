
package syntaxtree;

import lexer.Tag;

public class CustomVar extends Attribute {
	public final Object value;

	public CustomVar(Tag tag, Object value){
		super(tag);
		this.value = value;
	}

	@Override
	public String toString(){
		return "<" + this.tag.toString() + ", " + this.value.toString() + ">";
	}
}