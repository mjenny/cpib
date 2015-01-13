package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;

public class Ident extends AbstractToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 832628527569980445L;

	private final String name;
	
	public Ident(String name){
		super(Terminals.IDENT);
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	@Override
    public String toString() {
        return "(IDENT, \"" + name + "\")";
    }
    
	public String toString(final String indent) {
		return indent
				+ "<Ident name=\""
				+ name
				+ "\" line=\""
				+ super.getLine()
				+ "\"/>\n";
	}

    @Override
    public boolean equals(Object o) {
        if (o != null) {
            if (o.getClass() == this.getClass()) {
                if (super.equals((AbstractToken) o)) {
                    if (name.equals(((Ident) o).name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
