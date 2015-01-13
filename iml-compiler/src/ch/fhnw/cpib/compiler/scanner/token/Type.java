package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;
import ch.fhnw.cpib.compiler.scanner.enums.TypeAttribute;

public class Type extends AbstractToken {

	/**
	 * Serial id which is used for deep copy
	 */
	private static final long serialVersionUID = -5817373929157375018L;

	private final TypeAttribute attribute; 
	/**
	 * 
	 * @param terminal
	 */
	public Type(TypeAttribute attribute) {
		super(Terminals.TYPE);
		this.attribute = attribute;
	}

	/**
	 * Returns the token's type
	 * @return
	 */
	public TypeAttribute getAttribute() {
		return attribute;
	}
	
	@Override
	public String toString() {
		return "(TYPE, " + attribute.toString()  + ")";
	}
	public String toString(final String indent) {
		return indent
				+ "<Type type=\""
				+ attribute.toString()
				+ "\" line=\""
				+ super.getLine()
				+ "\"/>\n";
	}
}
