package ch.fhnw.cpib.compiler.scanner;

import java.util.Iterator;
import java.util.LinkedList;

public class TokenList implements ITokenList {

	private LinkedList<IToken> tokenList;
	
	private Iterator<IToken> listIterator;
	
	public TokenList(){
		tokenList = new LinkedList<IToken>();
	}

	@Override
	public void add(IToken token) {
		if(token != null){
			tokenList.add(token);
		}
		
	}

	@Override
	public void reset() {
		listIterator = null;
		
	}

	@Override
	public IToken nextToken() {
		if (listIterator == null){
			listIterator = tokenList.iterator();
		}
		return listIterator.next();
	}
	
	@Override
    public String toString() {
    	return tokenList.toString();
    }
	@Override
    public boolean equals(Object o) {
        if (o != null && this.getClass().equals(o.getClass())) {
            TokenList cmp = (TokenList) o;
            Iterator<IToken> thisIt = tokenList.iterator();
            Iterator<IToken> cmpIt = cmp.tokenList.iterator();

            while (thisIt.hasNext() && cmpIt.hasNext()) {
                IToken thisToken = thisIt.next();
                IToken cmpToken = cmpIt.next();
                if (!thisToken.equals(cmpToken))
                    return false;
            }
            return !(thisIt.hasNext() || cmpIt.hasNext());
        }
        return false;
    }
}
