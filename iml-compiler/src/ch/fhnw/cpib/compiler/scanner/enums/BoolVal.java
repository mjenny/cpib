package ch.fhnw.cpib.compiler.scanner.enums;

public enum BoolVal {

	TRUE(1), FALSE(0);
	
	BoolVal(int value){
		this.value = value;
	}
	
	private int value;
	
	public int getIntVal(){
		return value;
	}
	
	@Override
	public String toString(){
		if(value == 0){
			return "BoolVal false";
		} else {
			return "BoolVal true";
		}
	}
}
