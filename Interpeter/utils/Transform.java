/**
 * This file is part of the utils class
 *
 * when a token is recongized,
 * if it it a keyword, then we should convert it's coorspanding tag
 * else it is a identifier, we just reverse the same like it.
 */

package utils;
import lexer.Tag;
import lexer.Token;

import logger.ErrorHandler;
import exception.TypeMatchError;

public class Transform {

	public static Tag transform(String lexeme){
		switch(lexeme){
			case "int":
				return Tag.INT;
			case "char":
				return Tag.CHAR;
			case "float":
				return Tag.FLOAT;
			case "double":
				return Tag.DOUBLE;
			case "long":
				return Tag.VOID;
			case "do":
				return Tag.DO;
			case "while":
				return Tag.WHILE;
			case "if":
				return Tag.IF;
			case "else":
				return Tag.ELSE;
			case "switch":
				return Tag.SWITCH;
			case "case":
				return Tag.CASE;
			case "default":
				return Tag.DEFAULT;
			case "break":
				return Tag.BREAK;
			case "continue":
				return Tag.CONTINUE;
			case "static":
				return Tag.STATIC;
			case "struct":
				return Tag.STRUCT;
			case "return":
				return Tag.RETURN;
			case "for":
				return Tag.FOR;
			case "printf":
				return Tag.PRINTF;
			case "func":
				return Tag.FUNC;
			default:
				return null;
		}
	}

	// public static Object compatiable(Attribute attr, Attribute value){

	// 	// try{
	// 	// 	if(name.tag.equals(value.tag))
 //  // 				return true;

	//  //  		if(name.tag.equals(Tag.CHAR) ^ (value.tag.equals(Tag.CHAR))
	//  //  			throw new TypeMatchError(name.type, value.tag, name.line, "type cast failed");

	//  //  		if(name.type.equals(Tag.DOUBLE) && (value.tag.equals(Tag.Integer))){
	//  //  			Integer object = (Integer) value;
	//  //  			return new Double(object.intValue());
	//  //  		}

	//  //  		if(name.type.equals(Tag.INT) && (value instanceof Double)){
	//  //  			String text = value.toString();
	//  //  			Double object = (Double) value;
	//  //  			if(text.endsWith(".0"))
	//  //  				return new Integer((int)object.doubleValue());
	//  //  			throw new TypeMatchError(name.type, solve(value), name.line, "type cast failed");
	//  //  		}
	// 	// }catch(TypeMatchError error){
	// 	// 	ErrorHandler.typeMatchError(error);
	// 	// 	System.exit(1);
	// 	// }

 //  		// unreachable
 //  		return value;
 //  	}

  	public static Tag solve(Object value){
  		if(value instanceof Integer)
  			return Tag.INT;
  		else if(value instanceof Character)
  			return Tag.CHAR;
  		else if(value instanceof Double)
  			return Tag.DOUBLE;
  		return null;
  	}
}