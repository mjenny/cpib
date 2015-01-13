package ch.fhnw.cpib.compiler.scanner.token;

import java.io.Serializable;

import ch.fhnw.cpib.compiler.scanner.IToken;
import ch.fhnw.cpib.compiler.scanner.enums.Terminals;

public abstract class AbstractToken implements IToken, Serializable {

	/**
	 * Serial number of the class
	 */
	private static final long serialVersionUID = -2519188943124717822L;

	/**
	 * Terminal of this token
	 */
	private final Terminals terminal;
	
	/**
	 * Line number in the source code
	 */
	private int line;
	
	/**
	 * Constructor for AbstractToken
	 * @param terminal Specifies the terminal of the token
	 */
	public AbstractToken(Terminals terminal) {
		this.terminal = terminal;
	}
	
	/**
	 * Returns the token's terminal
	 * @return Returns the token's terminal
	 */
	public Terminals getTerminal() {
		return this.terminal;
	}
	
	/**
	 * Returns the token's line number
	 */
	public int getLine(){
		return line;
	}
	
	/**
	 * Set the token's line number
	 */
	public void setLine(int number){
		this.line = number;
	}
	
	@Override
	public String toString() {
		return terminal.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			AbstractToken cmp = (AbstractToken) o;
	        if (this.terminal == cmp.terminal) {
	        	if (this.line == cmp.line) {
	        		return true;
	            }
	        }
		} catch (Exception e) {
	            System.out.println("Exception occured: " + e.getMessage());
	    }
	    	return false;
	}
	    
	@Override
	public AbstractToken clone(){
		try {
			return (AbstractToken) super.clone();
	    } catch (CloneNotSupportedException e) {
	    	throw new InternalError();
	    }
	}
}
