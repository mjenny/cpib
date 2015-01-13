package ch.fhnw.cpib.compiler.scanner;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;

public interface IToken extends Cloneable{
	
	
	public int getLine();
	
	public void setLine(int number);
	
	public IToken clone();
	
	public Terminals getTerminal();
	
	public String toString();
}
