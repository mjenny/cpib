package ch.fhnw.cpib.compiler.scanner.state;

import java.util.Arrays;

import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.scanner.IScannerState;
import ch.fhnw.cpib.compiler.scanner.IScannerContext;
import ch.fhnw.cpib.compiler.scanner.IToken;
import ch.fhnw.cpib.compiler.scanner.token.Literal;
import ch.fhnw.cpib.compiler.scanner.enums.*;

public class LiteralState implements IScannerState{

	@Override
	public char[] handleChar(char[] c, IScannerContext context)
			throws LexicalError {
		assert (c.length > 1);
        int lastChar = c.length - 1;
        if (('0' <= c[lastChar] && c[lastChar] <= '9')) {
            // literal continues
            context.setState(this, false);
        } else {
            // literal ends
            if (ScannerSymbol.contains(c[lastChar]) 
                    || (' ' == c[lastChar]) 
                    || ('\t' == c[lastChar])
                    || ('\n' == c[lastChar])) {
                String literal = new String(Arrays.copyOfRange(c, 0, c.length - 1));
                Literal token = new Literal(Integer.parseInt(literal));
                token.setLine(context.getLineNumber());
                context.addToken((IToken) token);
                c = Arrays.copyOfRange(c, lastChar, lastChar + 1);
                context.setState(new InitialState(), true);
            } else {
                throw new LexicalError("Illegal character after literal", context.getLineNumber());
            }
        }
        return c;
	}
	 @Override
	    public boolean equals(Object o) {
		 return (o.getClass() == this.getClass());
	    }
}
