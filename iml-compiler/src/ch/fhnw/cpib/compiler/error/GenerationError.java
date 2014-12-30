package ch.fhnw.cpib.compiler.error;

public class GenerationError extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7058746659286972841L;
	
	private final String message;
	
	public GenerationError(String message){
		super();
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message + ".";
	}

}
