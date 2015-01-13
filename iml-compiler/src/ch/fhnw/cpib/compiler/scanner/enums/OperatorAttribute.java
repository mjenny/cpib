package ch.fhnw.cpib.compiler.scanner.enums;

public enum OperatorAttribute {
	// Addition operator
	PLUS("PLUS"), MINUS("MINUS"),
	
	// Multiplication operator
	TIMES("TIMES"), MOD("MOD"), DIV("DIV"),
	
	// Relational operators
	EQ("EQ"), NE("NE"), LT("LT"), GT("GT"), LE("LE"), GE("GE"),
	
	// Boolean operators
	AND("AND"), OR("OR"), CAND("CAND"), COR("COR"),
	
	// Member operators
	
	DOT("DOT");
	
	private String toString;
	
	OperatorAttribute(String toString) {
		this.toString = toString;
	}
		
	@Override
	public String toString() {
		return toString;
	}
}
