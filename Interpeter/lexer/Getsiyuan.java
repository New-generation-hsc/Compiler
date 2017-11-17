package lexer;
import java.util.*;



public class Getsiyuan {
	public Stack<String> stack=new Stack<>();
	public int count=1;
	public List<Siyuan> siyuan = new ArrayList<>();

	public class Siyuan{
		public String operator;
	    public String operand1;
	    public String operand2;
	    public String temporary;

	    
		public void Typesiyuan(){
			System.out.println("<" + operator + "," + operand1 +","+operand2+","+temporary+">" );
    }
}
	public void GEQ(Token operator ){
		Siyuan quanternary = new Siyuan();
		quanternary.operator = operator.lexeme;
		quanternary.operand2 = stack.pop();
		quanternary.operand1 = stack.pop();
		quanternary.temporary = "t"+count;
		quanternary.Typesiyuan();
		stack.push(quanternary.temporary);
		siyuan.add(quanternary);
		count++;
	}
	public void HEQ(Token operator ){
		Siyuan quanternary = new Siyuan();
		quanternary.operator = operator.lexeme;
		quanternary.operand1 = stack.pop();
		quanternary.operand2 = "_";
		quanternary.temporary = "t"+count;
		quanternary.Typesiyuan();
		stack.push(quanternary.temporary);
		siyuan.add(quanternary);
		count++;
	}
	public void ASSI(){
		Siyuan quanternary = new Siyuan();
		quanternary.operator = "="; 
		quanternary.operand1 = stack.pop();
		quanternary.operand2 = "_";
		quanternary.temporary = stack.pop();
		quanternary.Typesiyuan();
		stack.push(quanternary.temporary);
		siyuan.add(quanternary);
		
	}
	public void IF(){
		Siyuan quanternary = new Siyuan();
		quanternary.operator = "if"; 
		quanternary.operand1 = stack.pop();
		quanternary.operand2 = "_";
		quanternary.temporary = "_";
		quanternary.Typesiyuan();
		siyuan.add(quanternary);
		
    }
    public void EL(){
    	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "else"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "_";
		quanternary.Typesiyuan();
		siyuan.add(quanternary);
    }
    public void IE(){
    	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "ie"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "_";
		quanternary.Typesiyuan();
		siyuan.add(quanternary);

    }
     public void WH(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "while"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "_";
		quanternary.Typesiyuan();
		siyuan.add(quanternary);

    }
     public void DO(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "do"; 
		quanternary.operand1 = stack.pop();
		quanternary.operand2 = "-";
		quanternary.temporary = "whileend";
		quanternary.Typesiyuan();
		siyuan.add(quanternary);
		

    }
     public void WE(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "we"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "_";
		quanternary.Typesiyuan();
        siyuan.add(quanternary);

    }
    public void YES(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "yes"; 
		quanternary.operand1 = stack.pop();;
		quanternary.operand2 = "_";
		quanternary.temporary = "forend";
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void JUMPS2(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "jump"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "s2";
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void JUMPS3(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "jump"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "s3";
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void JUMPS4(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "jump"; 
		quanternary.operand1 = "_";
		quanternary.operand2 = "_";
		quanternary.temporary = "s4";
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
   public void ARR(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "[]"; 
		quanternary.operand2 = stack.pop();
		quanternary.operand1 = stack.pop();
		quanternary.temporary = "t"+count;
	    stack.push(quanternary.temporary);
		count++;
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void CALL(Token function){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "call"; 
		quanternary.operand1 = function.lexeme+"()";
		quanternary.operand2 = "_";
		quanternary.temporary ="_"; 
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void RETURN(){
     	Siyuan quanternary = new Siyuan();
    	quanternary.operator = "return"; 
		quanternary.operand2 = "_";
		quanternary.operand1 = "_";
		quanternary.temporary ="old_address"; 
		quanternary.Typesiyuan();
        siyuan.add(quanternary);
    }
    public void Push(Token operand){
        stack.push(operand.lexeme);
    }
    
}