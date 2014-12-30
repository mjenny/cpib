package ch.fhnw.cpib.compiler.error;

public class LexicalError extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4897540190746836685L;

	private final int lineNumber;
	
	private final String message;
	
	public LexicalError(String message, int lineNumber){
		super();
		this.lineNumber = lineNumber;
		this.message = message;
	}
	
	public String getMessage(){
		return message + "at line" + lineNumber + ".";
	}
	
	
}
