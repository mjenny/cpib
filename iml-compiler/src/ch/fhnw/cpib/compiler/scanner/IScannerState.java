package ch.fhnw.cpib.compiler.scanner;


import ch.fhnw.cpib.compiler.error.LexicalError;

public interface IScannerState {

	public char[] handleChar(char[] c, IScannerContext context) throws LexicalError;
}
