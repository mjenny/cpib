package ch.fhnw.cpib.compiler.scanner;

public interface IScannerContext {
	
	public void setState(IScannerState state, boolean keepCurrent);
	
	public IScannerState getState();
	
	public void addToken(IToken token);
	
	public int getLineNumber();

}
