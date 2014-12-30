package ch.fhnw.cpib.compiler.scanner.state;

import java.util.Arrays;

import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.scanner.IScannerContext;
import ch.fhnw.cpib.compiler.scanner.IScannerState;
import ch.fhnw.cpib.compiler.scanner.IToken;
import ch.fhnw.cpib.compiler.scanner.enums.KeywordList;
import ch.fhnw.cpib.compiler.scanner.enums.ScannerSymbol;
import ch.fhnw.cpib.compiler.scanner.token.Ident;

public class LetterState implements IScannerState{

	@Override
	public char[] handleChar(char[] c, IScannerContext context)
			throws LexicalError {
		assert (c.length > 1);
        int lastChar = c.length - 1;
        int line = context.getLineNumber();

        if (('A' <= c[lastChar] && c[lastChar] <= 'Z') || ('a' <= c[lastChar] && c[lastChar] <= 'z')
                || ('0' <= c[lastChar] && c[lastChar] <= '9')) {
            // keyword or identifier continues
            context.setState(this, false);
        } else {
            // keyword or identifier ends
            if (ScannerSymbol.contains(c[lastChar]) || (' ' == c[lastChar]) || ('\t' == c[lastChar])
                    || ('\n' == c[lastChar])) {
                String letters = new String(Arrays.copyOfRange(c, 0, c.length - 1));
                KeywordList k = KeywordList.match(letters);
                if (k == null) {
                    // is an identifier
                    IToken t = new Ident(letters);
                    t.setLine(line);
                    context.addToken(t);
                    c = Arrays.copyOfRange(c, lastChar, lastChar + 1);
                    context.setState(new InitialState(), true);
                } else {
                    // is a keyword
                    k.setLine(line);
                    context.addToken(k.getToken());
                    c = Arrays.copyOfRange(c, lastChar, lastChar + 1);
                    context.setState(new InitialState(), true);
                }

            } else {
                // illegal character found
                throw new LexicalError("Syntax error, illegal character '" + c[lastChar] + "' found", line);
            }
        }
        return c;
	}
	 @Override
	 public boolean equals(Object o) {
		 return (o.getClass() == this.getClass());
	}

}
