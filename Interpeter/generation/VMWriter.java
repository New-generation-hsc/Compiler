
package generation;

public class VMWriter {

	public void writePush(String segment, int index){
		System.out.printf("push %s %d\n", segment, index);
	}

	public void writePop(String segment, int index){
		System.out.printf("pop %s %d\n", segment, index);
	}

	public void writeArithmetic(String command){
		System.out.println(command);
	}

	public void writeLabel(String label, int counter){
		System.out.printf("label %s\n", label + Integer.toString(counter));
	}

	public void writeGoto(String label, int counter){
		System.out.printf("goto %s\n", label + Integer.toString(counter));
	}

	public void writeIf(String label, int counter){
		System.out.printf("if-goto %s\n", label + Integer.toString(counter));
	}

	public void writeCall(String name, int nArgs){
		System.out.printf("call %s %d\n", name, nArgs);
	}

	public void writeFunction(String name, int nLocals){
		System.out.printf("function %s %d\n", name, nLocals);
	}

	public void writePrintInt(){
		System.out.println("call output.printInt 1");
	}

	public void writeReturn(){
		System.out.println("return");
	}
}