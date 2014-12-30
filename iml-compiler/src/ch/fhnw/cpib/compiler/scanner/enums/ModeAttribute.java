package ch.fhnw.cpib.compiler.scanner.enums;

public enum ModeAttribute {

	COPY("COPY"), REF("REF"),
	IN("IN"), OUT("OUT"), INOUT("INOUT"),
	CONST("CONST"), VAR("VAR");
	
	ModeAttribute(String toString){
		this.toString = toString;
	}
	
	private String toString;
	
	@Override
	public String toString(){
		return toString;
	}
}
