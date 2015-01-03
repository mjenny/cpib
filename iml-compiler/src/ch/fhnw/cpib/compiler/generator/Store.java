package ch.fhnw.cpib.compiler.generator;

import ch.fhnw.cpib.compiler.parser.AbsTree.TypedIdent;

public final class Store {
	private boolean initialized;
	private boolean isConst;
	private boolean writeable;
	private int address;
	private final String ident;
	private final TypedIdent typedIdent;
	
	public Store(final String ident, final TypedIdent typedIdent, final boolean isConst) {
		this.ident = ident;
		this.typedIdent = typedIdent;
		this.writeable = true;
		this.initialized = false;
		this.isConst = isConst;
	}
	
	public boolean isConst() {
		return isConst;
	}
	
	public boolean isWriteable() {
		return writeable;
	}
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void inizialize() {
		initialized = true;
		if (isConst)
			writeable = false;
	}
	
	public int getAddress() {
		return address;
	}
	
	public void setAddress(final int address) {
		this.address = address;
	}
	
	public String getIdent() {
		return this.ident;
	}
	
	public TypedIdent getType() {
		return this.typedIdent;
	}
}
