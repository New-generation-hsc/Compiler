package syntaxtree;

import lexer.Tag;

public class Attribute {
	public final Tag tag;

	public Attribute(Tag tag){
		this.tag = tag;
	}

	@Override
	public String toString(){
		return this.tag.toString();
	}
}