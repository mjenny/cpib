package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;
import ch.fhnw.cpib.compiler.scanner.enums.BoolVal;

public class Literal extends AbstractToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4985256907228710024L;

	private final int value;
	
	private BoolVal bool;

	public Literal(int value) {
		super(Terminals.LITERAL);
		this.value = value;
	}
	
	public Literal(BoolVal bool){
		super(Terminals.LITERAL);
		this.value = bool.getIntVal();
		this.bool = bool;
	}
	@Override
    public String toString() {
        if (bool != null) {
            return "(LITERAL, " + bool.toString() + ")";
        } else {
            return "(LITERAL, IntVal " + value + ")";
        }
    }
    
	public String toString(final String indent) {

		return indent
				+ "<Literal mode=\""
				+ getTerminal().toString()
				+ "\" value=\""
				+ (bool != null?bool.toString():value)
				+ "\" line=\""
				+ super.getLine()
				+ "\"/>\n";
	}

	public boolean getBoolVal(){
		return (value != 0);
	}

	public int getIntVal(){
		return value;
	}
	
	public boolean isInteger(){
		return (bool == null);
	}
	
	public boolean isBoolean(){
		return (bool != null);
	}
	
	@Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            if (super.equals((AbstractToken) o)) {
                return (this.value == ((Literal) o).value);
            }
        }
        return false;
    }
}
