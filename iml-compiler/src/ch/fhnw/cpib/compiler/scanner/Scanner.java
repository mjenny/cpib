package ch.fhnw.cpib.compiler.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;


import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.scanner.state.InitialState;
import ch.fhnw.cpib.compiler.utils.ArrayUtils;

public class Scanner implements IScannerContext {

	private ITokenList tokenList = null;
	
	private IScannerState currentState = null;
	
	private int lineNumber = 0;
	
	private boolean keepCurrent = false;
	
	@Override
	public void addToken(IToken token) {
		if(tokenList != null){
			tokenList.add(token);
		}
		
	}
	@Override
	public void setState(IScannerState state, boolean keepCurrent) {
		if (state != null){
			currentState = state;
		}
		this.keepCurrent = keepCurrent;
		
	}

	@Override
	public IScannerState getState() {
		return currentState;
	}

	

	@Override
	public int getLineNumber() {
		return lineNumber;
	}
	
	public ITokenList scan(BufferedReader input) throws IOException, LexicalError {
		tokenList = new TokenList();
		currentState = new InitialState();
		String currentLine = "";
		char[] currentChar = {};
		lineNumber = 0;
		while ((currentLine = input.readLine()) != null){
			lineNumber++;
			currentLine = currentLine + "\n";
			Iterator<Character> chars = stringIterator(currentLine);
			
			while (chars.hasNext() || currentChar.length > 0){
				if(!keepCurrent)
					currentChar = ArrayUtils.expandCharArray(currentChar, (char) chars.next());
				currentChar = currentState.handleChar(currentChar, this);
			}
		}
		//process end of file
		if (currentState.equals(new InitialState())){
			char[] end_of_text = { '\u0003' };
			currentState.handleChar(end_of_text, this);
		} else {
			throw new LexicalError("Unexpected file end", lineNumber);
		}
		return tokenList;
		
	}
    private static Iterator<Character> stringIterator(final String string) {
        if (string == null)
            throw new NullPointerException();

        return new Iterator<Character>() {
            private int index = 0;

            public boolean hasNext() {
                return index < string.length();
            }

            public Character next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return string.charAt(index++);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
	
	
}
