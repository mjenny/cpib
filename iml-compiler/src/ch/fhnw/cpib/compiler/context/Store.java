package ch.fhnw.cpib.compiler.context;

import ch.fhnw.cpib.compiler.parser.AbsTree.TypedIdent;
import ch.fhnw.cpib.compiler.parser.AbsTree.TypedIdentIdent;
import ch.fhnw.cpib.compiler.Compiler;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.CodeTooSmallError;
@SuppressWarnings("rawtypes")
public final class Store extends Symbol {
	private boolean initialized;
	private boolean isConst;
	private boolean writeable;
	private int address;
	@SuppressWarnings("unused")
	private final String ident;
	@SuppressWarnings("unused")
	private final TypedIdent typedIdent;
	private boolean relative = false;
	private boolean reference = false;
	private boolean isRecord = false;
	
	public Store(final String ident, final TypedIdent typedIdent, final boolean isConst) {
		super(ident, typedIdent);
		this.ident = ident;
		this.typedIdent = typedIdent;
		this.writeable = true;
		this.initialized = false;
		this.isConst = isConst;
		if (typedIdent instanceof TypedIdentIdent)
			isRecord = true;
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
	
	public boolean isRecord() {
		return isRecord;
	}
	
	public void initialize() {
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
	
	public void setRelative(final boolean relative) {
		this.relative = relative;
	}
	
	public void setReference(final boolean reference) {
		this.reference = reference;
	}
	
	public int codeLoad(final int loc) throws CodeTooSmallError {
		int loc1 = codeRef(loc);
		Compiler.getVM().Deref(loc1++);
		return loc1;
	}
	
	public int codeRef(final int loc) throws CodeTooSmallError {
		int loc1 = loc;
		
		if (relative) {
			Compiler.getVM().LoadRel(loc1++, address);
		} else {
			Compiler.getVM().IntLoad(loc1++, address);
		}
		
		if (reference) {
			Compiler.getVM().Deref(loc1++);
		}
		
		return loc1;
	}
	
	public Store clone() {
		Store store = new Store(
		this.getIdent(),
		this.getType(),
		this.isConst);
		store.address = this.address;
		store.initialized = this.initialized;
		store.writeable = this.writeable;
		return store;
	}
	
}
