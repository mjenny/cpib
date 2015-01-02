package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;
import ch.fhnw.cpib.compiler.scanner.enums.OperatorAttribute;

public abstract class Operator extends AbstractToken {

	/**
	 * Serial id which is used for deep copy
	 */
	private static final long serialVersionUID = -3558372892185656506L;
	
	private final OperatorAttribute attribute;
	
	/**
	 * 
	 * @param terminal
	 */
	public Operator(Terminals terminal, OperatorAttribute attribute) {
		super(terminal);
		this.attribute = attribute;
	}
	
	public OperatorAttribute getOperatorAttribute() {
		return attribute;
	}
	@Override
	public String toString() {
		return "(" + getTerminal().toString() + ", " + attribute.toString() + ")";
	}
	    
	public String toString(final String indent) {
		return indent
				+ "<Operator name=\""
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
				return (attribute == ((Operator) o).attribute);
	        }
	    }
	    return false;
	}
	
	/**
	 * Class for AddOpr
	 */
	public static class AddOpr extends Operator {
		
		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -6397828496299817656L;
		
		public AddOpr(OperatorAttribute attribute) {
			super(Terminals.ADDOPR, attribute);
			assert(OperatorAttribute.PLUS == attribute || OperatorAttribute.MINUS == attribute);
		}
	}
	
	/**
	 * Class for BoolOpr
	 */
	public static class BoolOpr extends Operator {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -4552285047758807110L;

		/**
		 * Constructor for BoolOpr
		 * @param attribute Specifies which BoolOpr to use
		 */
		public BoolOpr(OperatorAttribute attribute) {
			super(Terminals.BOOLOPR, attribute);
			assert(OperatorAttribute.AND == attribute || OperatorAttribute.OR == attribute || 
					OperatorAttribute.CAND == attribute || OperatorAttribute.COR == attribute);
		}
	}
	
	/**
	 * Class for MultOpr
	 */
	public static class MultOpr extends Operator {
		
		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = 4065064966640082383L;

		/**
		 * Constructor for MultOpr
		 * @param attribute Specifies which MultOpr to use
		 */
		public MultOpr(OperatorAttribute attribute) {
			super(Terminals.MULTOPR, attribute);
			assert(OperatorAttribute.TIMES == attribute || OperatorAttribute.DIV == attribute || 
					OperatorAttribute.MOD == attribute);
		}
	}
	
	/**
	 * Class for RelOpr
	 */
	public static class RelOpr extends Operator {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = 3366616738652185830L;

		public RelOpr(OperatorAttribute attribute) {
			super(Terminals.RELOPR, attribute);
			assert(OperatorAttribute.EQ == attribute || OperatorAttribute.NE == attribute ||
					OperatorAttribute.LT == attribute || OperatorAttribute.GT == attribute || 
					OperatorAttribute.LE == attribute || OperatorAttribute.GE == attribute);
		}
		
	}
	
	/**
	 * Class for DotOpr
	 */
	public static class DotOpr extends Operator {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -6111066864207707211L;

		public DotOpr() {
			super(Terminals.RELOPR, OperatorAttribute.DOT);
		}
		
	}
}
