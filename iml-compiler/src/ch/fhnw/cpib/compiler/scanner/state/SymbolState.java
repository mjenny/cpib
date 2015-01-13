package ch.fhnw.cpib.compiler.scanner.state;

import java.util.Arrays;

import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.scanner.IScannerContext;
import ch.fhnw.cpib.compiler.scanner.IScannerState;
import ch.fhnw.cpib.compiler.scanner.enums.ScannerSymbol;
import ch.fhnw.cpib.compiler.scanner.enums.SymbolList;

public class SymbolState implements IScannerState {
	
    @Override
    public char[] handleChar(char[] c, IScannerContext context) throws LexicalError {
        assert (c.length > 1);
        int lastChar = c.length - 1;
        if (c[lastChar] == '=') { // = is only possible follow-up
            // symbol continues
            context.setState(this, false);
 
        } else {
            if (('A' <= c[lastChar] && c[lastChar] <= 'Z') 
                    || ('a' <= c[lastChar] && c[lastChar] <= 'z')
                    || ('0' <= c[lastChar] && c[lastChar] <= '9') 
                    || (' ' == c[lastChar]) 
                    || ('\t' == c[lastChar])
                    || ('\n' == c[lastChar]) 
                    || (ScannerSymbol.contains(c[lastChar]))) {
                // keyword
                String letters = new String(Arrays.copyOfRange(c, 0, c.length - 1));
                SymbolList s = SymbolList.match(letters);
                if (s != null) {
                    s.setLine(context.getLineNumber());
                    context.addToken(s.getToken());
                    c = Arrays.copyOfRange(c, lastChar, lastChar + 1);
                    context.setState(new InitialState(), true);
                } else {
                    throw new LexicalError("Illegal symbol '" + letters + "'", context.getLineNumber());
                }
            } else {
                throw new LexicalError("Illegal character " + c[lastChar] + " after symbol", context.getLineNumber());
            }
        }
        return c;
    }

    @Override
    public boolean equals(Object o) {
    	return (o.getClass() == this.getClass());
    }


}
