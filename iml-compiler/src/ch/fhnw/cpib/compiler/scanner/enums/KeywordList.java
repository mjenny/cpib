package ch.fhnw.cpib.compiler.scanner.enums;

import ch.fhnw.cpib.compiler.scanner.IToken;
import ch.fhnw.cpib.compiler.scanner.token.Keyword;
import ch.fhnw.cpib.compiler.scanner.token.Type;
import ch.fhnw.cpib.compiler.scanner.token.Literal;
import ch.fhnw.cpib.compiler.scanner.token.Operator;
import ch.fhnw.cpib.compiler.scanner.token.Mode;

public enum KeywordList {
	BOOL("bool", new Type(TypeAttribute.BOOL)),
	CALL("call", new Keyword.Call()),
	CAND("cand", new Operator.BoolOpr(OperatorAttribute.CAND)), 
	CONST("const", new Mode.ChangeMode(ModeAttribute.CONST)), 
	COPY("copy", new Mode.MechMode(ModeAttribute.COPY)), 
	COR("cor", new Operator.RelOpr(OperatorAttribute.COR)),
	DIV("div", new Operator.MultOpr(OperatorAttribute.DIV)),
	DEBUGIN("debugin", new Keyword.DebugIn()),
	DEBUGOUT("debugout", new Keyword.DebugOut()),
	ELSE("else", new Keyword.Else()), 
	ENDIF("endif", new Keyword.EndIf()),
	ENDWHILE("endwhile", new Keyword.EndWhile()),
	ENDPROGRAM("endprogram", new Keyword.EndProgram()),
	ENDFUN("endfun", new Keyword.EndFun()),
	ENDPROC("endproc", new Keyword.EndProc()),
	FALSE("false", new Literal(BoolVal.FALSE)), 
	FUN("fun", new Keyword.Fun()), 
	GLOBAL("global", new Keyword.Global()), 
	IF("if", new Keyword.If()),
	THEN("then", new Keyword.Then()),
	IN("in", new Mode.FlowMode(ModeAttribute.IN)), 
	INIT("init", new Keyword.Init()), 
	INOUT("inout", new Mode.FlowMode(ModeAttribute.INOUT)), 
	INT32("int32", new Type(TypeAttribute.INT32)), 
	LOCAL("local", new Keyword.Global()), 
	MOD("mod", new Operator.MultOpr(OperatorAttribute.MOD)),
	NOT("not", new Keyword.Not()), 
	OUT("out", new Mode.FlowMode(ModeAttribute.OUT)), 
	PROC("proc", new Keyword.Proc()), 
	PROGRAM("program", new Keyword.Program()),
	RECORD("record", new Keyword.Record()),
	REF("ref", new Mode.MechMode(ModeAttribute.REF)), 
	RETURNS("returns", new Keyword.Returns()),
	SKIP("skip", new Keyword.Skip()), 
	TRUE("true", new Literal(BoolVal.TRUE)), 
	VAR("var", new Mode.ChangeMode(ModeAttribute.VAR)), 
	WHILE("while", new Keyword.While());
	
	/**
	 * Creates a KeywordList
	 * @param s
	 * @param t
	 */
	KeywordList(String s, IToken t) {
		this.pattern = s;
		this.token = t;
	}
	
	private String pattern;
	private IToken token;
	
	/**
	 * Match and return a KeywordList
	 * @param toMatch Pattern to match
	 * @return Matching KeywordList or null
	 */
	public static KeywordList match(String toMatch) {
		for (KeywordList k : values()) {
			if (k.pattern.equals(toMatch))
				return k;
		}
		return null;
	}
	
	/**
	 * Set the line number of the token
	 * @param number the token's line number
	 */
	public void setLine(int number) {
		token.setLine(number);
	}
	
	/**
	 * Returns a clone of the token
	 * @return Returns the token
	 */
	public IToken getToken() {
		return token.clone();
	}
}
