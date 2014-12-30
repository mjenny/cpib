package ch.fhnw.cpib.compiler.scanner;

import ch.fhnw.cpib.compiler.scanner.IToken;

public interface ITokenList {
	
	public void add(IToken token);
	
	public void reset();
	
	public IToken nextToken();
	
	public String toString();
}
