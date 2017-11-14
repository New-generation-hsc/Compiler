/**
 * this is the class that tranform the tag into string
 */

package utils;

import lexer.Tag;

public class Resolve {
	public static String resolve(Tag tag){
		switch(tag){
			case INT:
				return "int";
			case FLOAT:
				return "float";
			case DOUBLE:
				return "double";
			case CHAR:
				return "char";
			default:
				return null;
		}
	}
} 