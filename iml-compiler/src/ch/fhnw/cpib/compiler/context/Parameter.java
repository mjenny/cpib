package ch.fhnw.cpib.compiler.context;

import ch.fhnw.cpib.compiler.parser.AbsTree.TypedIdent;
import ch.fhnw.cpib.compiler.scanner.token.Mode;

@SuppressWarnings("rawtypes")
public class Parameter {
	private final Mode flowMode;
	private final Mode mechMode;
	private final Mode changeMode;
	private final TypedIdent type;
	
	public Parameter(
			final Mode flowMode,
			final Mode mechMode,
			final Mode changeMode,
			final TypedIdent type) {
		this.flowMode = flowMode;
		this.mechMode = mechMode;
		this.changeMode = changeMode;
		this.type = type;
	}
	
	public Mode getFlowMode() {
		return flowMode;
	}
	
	public Mode getMechMode() {
		return mechMode;
	}
	
	public Mode getChangeMode() {
		return changeMode;
	}
	
	public TypedIdent getType() {
		return type;
	}
}
