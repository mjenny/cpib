package ch.fhnw.cpib.compiler.error;

public class ContextError extends Exception {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 4885736750228182905L;
	
	private final int lineNumber;
	
	private final String message;
	
	public ContextError(String message, int lineNumber){
		super();
		this.lineNumber = lineNumber;
		this.message = message;
	}
	@Override
	public String getMessage(){
		return message + " at line " + lineNumber + ".";
	}
}
