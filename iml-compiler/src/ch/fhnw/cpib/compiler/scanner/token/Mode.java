package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;
import ch.fhnw.cpib.compiler.scanner.enums.ModeAttribute;


public class Mode extends AbstractToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4591329230515185762L;
	
	private final ModeAttribute attribute;
	
	public Mode(Terminals terminal, ModeAttribute attribute) {
		super(terminal);
		this.attribute = attribute;
	}

	public ModeAttribute getAttribute(){
		return attribute;
	}
	@Override
    public String toString() {
        return "(" + getTerminal().toString() + ", " + attribute.toString() + ")";
    }
    
	public String toString(final String indent) {
		return indent
				+ "<Mode name=\""
				+ getTerminal().toString()
				+ "\" attribute=\""
				+ attribute.toString()
				+ "\" line=\""
				+ super.getLine()
				+ "\"/>\n";
	}

    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == this.getClass()) {
            if (super.equals((AbstractToken) o)) {
                return (attribute == ((Mode) o).attribute);
            }
        }
        return false;
    }
    public static class ChangeMode extends Mode {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2702117323796554867L;
    	
		public ChangeMode(ModeAttribute attribute){
			super(Terminals.CHANGEMODE, attribute);
			assert (attribute == ModeAttribute.CONST || attribute == ModeAttribute.VAR);
		}
    }
    
    public static class FlowMode extends Mode {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7391883613108497808L;
    	
		public FlowMode(ModeAttribute attribute){
			super(Terminals.FLOWMODE, attribute);
			assert (attribute == ModeAttribute.IN || attribute == ModeAttribute.INOUT || attribute == ModeAttribute.OUT);
		}
    }
    public static class MechMode extends Mode{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6226108510444906672L;
		
		public MechMode(ModeAttribute attribute){
			super(Terminals.MECHMODE, attribute);
			assert (attribute == ModeAttribute.COPY|| attribute == ModeAttribute.REF);
		}
    }
    
}
