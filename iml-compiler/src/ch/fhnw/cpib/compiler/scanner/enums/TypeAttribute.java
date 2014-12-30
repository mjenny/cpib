package ch.fhnw.cpib.compiler.scanner.enums;

public enum TypeAttribute {
	INT32("INT32"), BOOL("BOOL");
	
	TypeAttribute(String toString) {
		this.toString = toString;
	}
	
	private String toString;
	
	@Override
	public String toString() {
		return toString;
	}
}
