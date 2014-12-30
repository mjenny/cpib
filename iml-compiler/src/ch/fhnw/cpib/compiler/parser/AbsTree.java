package ch.fhnw.cpib.compiler.parser;


import ch.fhnw.cpib.compiler.scanner.token.*;
import ch.fhnw.cpib.compiler.scanner.token.Mode.*;

public interface AbsTree {
	
	//gemacht 18.12.
	public class StorageParameter {
		private final DeclarationStore declarationStore;
		private final DeclarationStore nextDeclarationStore;
		
		public StorageParameter(DeclarationStore delcarationStore, DeclarationStore next){
			this.declarationStore = delcarationStore;
			this.nextDeclarationStore = next;
		}
		public String toString(String indent){
			return indent
					+ "<StorageParameter>\n"
					+ declarationStore.toString(indent + '\t')
					+ nextDeclarationStore.toString(indent + '\t')
					+ indent
					+ "</StorageParameter>\n";
		}
		public DeclarationStore getDeclarationStore(){
			return declarationStore;
		}
		public DeclarationStore getNextDeclarationStore(){
			return nextDeclarationStore;
		}
	}
	//gefixt
	public class ProgramParameter {
		private final FlowMode flowMode;
		private final ChangeMode changeMode;
		private final TypedIdent typedIdent;
		private final ProgramParameter nextProgramParameter;
		
		public ProgramParameter(FlowMode flowMode, ChangeMode changeMode, TypedIdent typedIdent, ProgramParameter next){
			this.flowMode = flowMode;
			this.changeMode = changeMode;
			this.typedIdent = typedIdent;
			this.nextProgramParameter = next;
		}
		public String toString(String indent) {
			return indent
					+ "<ProgramParameter>\n"
					+ flowMode.toString(indent + '\t')
					+ changeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ nextProgramParameter.toString(indent + '\t')
					+ indent
					+ "</ProgramParameter>\n";
		}
		public FlowMode getFlowMode() { return flowMode; }
		public ChangeMode getChangeMode() { return changeMode; }
		public TypedIdent getTypedIdent() { return typedIdent; }
		public ProgramParameter getNextProgramParameter() { return nextProgramParameter; }
	}
	public class Program {
		private final Ident ident;
		private final Declaration declaration;
		private final Cmd cmd;
		private final ProgramParameter programParameter;
		
		public Program(Ident ident,ProgramParameter programParameter, Declaration declaration, Cmd cmd){
			this.ident = ident;
			this.programParameter = programParameter;
			this.declaration = declaration;
			this.cmd = cmd;
		}
		public String toString(String indent){
			return indent
							+ "<Program>\n"
							+ ident.toString(indent + '\t')
							+ programParameter.toString(indent + '\t')
							+ declaration.toString(indent + '\t')
							+ cmd.toString(indent + '\t')
							+ indent
							+ "</Program>\n";
		}
		public String toString(){
			return toString("");
		}
		public Declaration getDeclarations(){return declaration;}
		public Cmd getCommands(){return cmd;}
		public Ident getIdent(){return ident;}
		public ProgramParameter getProgramParameter(){return programParameter;}
		
	}
	public class Declaration {
		protected final Declaration nextDecl;
		
		public Declaration(Declaration next){
			this.nextDecl = next;
		}
		public String toString(String indent){
			return indent
					+ "<Decl>\n"
					+ (nextDecl != null?nextDecl.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</Decl>\n";
		}
		public Declaration getNextDecl(){ return nextDecl; }
	}
	public class DeclarationFunction extends Declaration{
		private final Ident ident;
		private final Parameter param;
		private final DeclarationStore returnDecl;
		private final GlobalImport globImp;
		private final Declaration dcl;
		private final Cmd cmd;
		
		public DeclarationFunction(Ident ident, Parameter param, DeclarationStore returnDecl, GlobalImport globImp, Declaration dcl, Cmd cmd, Declaration nextDecl){
			super(nextDecl);
			this.ident = ident;
			this.param = param;
			this.returnDecl = returnDecl;
			this.globImp = globImp;
			this.dcl = dcl;
			this.cmd = cmd;
		}
		public String toString(String indent){
			return indent
					+ "<DeclFun>\n"
					+ ident.toString(indent + '\t')
					+ param.toString(indent + '\t')
					+ returnDecl.toString(indent + '\t')
					+ globImp.toString(indent + '\t')
					+ dcl.toString(indent + '\t')
					+ cmd.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</DeclFun>\n";
		}
		public Cmd getCmd() { return cmd;}
		public Ident getIdent() {return ident;}
		public Declaration getDecl() { return dcl;}
		public Parameter getParam() { return param;}
		public GlobalImport getGlobImp() { return globImp;}
		public DeclarationStore getReturnDecl() { return returnDecl;}
	}
	public class DeclarationProcedure extends Declaration {
		private final Ident ident;
		private final Parameter param;
		private final GlobalImport globalImport;
		private final Declaration decl;
		private final Cmd cmd;

		public DeclarationProcedure(Ident ident, Parameter parameter, GlobalImport globalImport, Declaration decl, Cmd cmd, Declaration nextDecl) {
			super(nextDecl);
			this.ident = ident;
			this.param = parameter;
			this.globalImport = globalImport;
			this.decl = decl;
			this.cmd = cmd;
		}

		public String toString(final String indent) {
			return indent
					+ "<DeclarationProcedure>\n"
					+ ident.toString(indent + '\t')
					+ param.toString(indent + '\t')
					+ (globalImport!=null?globalImport.toString(indent + '\t'):"")
					+ (decl!=null?decl.toString(indent + '\t'):"")
					+ cmd.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</DeclarationProcedure>\n";
		}
		
		public Ident getIdent() { return ident;}
		public Parameter getParam() { return param;}
		public GlobalImport getGlobImp() { return globalImport;}
		public Declaration getDecl() { return decl;}
		public Cmd getCmd() { return cmd;}
	}
	public class DeclarationStore extends Declaration {
		private final ChangeMode changeMode;
		private final TypedIdent typedIdent;

		public DeclarationStore(ChangeMode changeMode, TypedIdent typedIdent, Declaration nextDeclaration) {
			super(nextDeclaration);
			this.changeMode = changeMode;
			this.typedIdent = typedIdent;
		}

		public String toString(final String indent) {
			return indent
					+ "<DeclStore>\n"
					+ changeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</DeclStore>\n";
		}
		public ChangeMode getChangeMode() {
            return changeMode;
        }

        public TypedIdent getTypedIdent() {
            return typedIdent;
        }

	}
	public class DeclarationRecord extends Declaration {
		private final Ident ident;
		private final StorageParameter storageParameter;
		
		public DeclarationRecord(Ident ident, StorageParameter storageParameter, Declaration nextDeclaration) {
			super(nextDeclaration);
			this.ident = ident;
			this.storageParameter = storageParameter;
		}
		public String toString(final String indent) {
			return indent
					+ "<DeclarationRecord>\n"
					+ ident.toString(indent + '\t')
					+ storageParameter.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</DeclarationRecord>\n";
		}
		
		public Ident getIdent() { return ident; }
		public StorageParameter getStorageParameter(){ return storageParameter; }
	}
	//18.12.
	public class Parameter {
		private final FlowMode flowMode;
		private final MechMode mechMode;
		private final ChangeMode changeMode;
		private final TypedIdent typedIdent;
		private final Parameter nextParam;

		public Parameter(FlowMode flowMode, MechMode mechMode, ChangeMode changeMode, TypedIdent typedIdent, Parameter nextParam) {
			this.flowMode = flowMode;
			this.mechMode = mechMode;
			this.changeMode = changeMode;
			this.typedIdent = typedIdent;
			this.nextParam = nextParam;
		}

		public String toString(String indent) {
			return indent
					+ "<Parameter>\n"
					+ flowMode.toString(indent + '\t')
					+ mechMode.toString(indent + '\t')
					+ changeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ (nextParam!=null?nextParam.toString(indent + '\t'):"")
					+ indent
					+ "</Parameter>\n";
		}

        public FlowMode getFlowMode() {
            return flowMode;
        }

        public MechMode getMechMode() {
            return mechMode;
        }

        public ChangeMode getChangeMode() {
            return changeMode;
        }

        public Parameter getNextParam() {
            return nextParam;
        }
	}
	public class ParameterList {
		private final Parameter parameter;
		private final ParameterList parameterList;

		public ParameterList(Parameter parameter, ParameterList parameterList) {
			this.parameter = parameter;
			this.parameterList = parameterList;
		}

		public String toString(String indent) {
			return indent
					+ "<ParameterList>\n"
					+ parameter.toString(indent + '\t')
					+ (parameterList!=null?parameterList.toString(indent + '\t'):"")
					+ indent
					+ "</ParameterList>\n";
		}

        public Parameter getParameter() {
            return parameter;
        }

        public ParameterList getParameterList() {
            return parameterList;
        }
	}
	
	
	public class Cmd{
		private final Cmd nextCmd;
		
		public Cmd(Cmd nextCmd) {
			this.nextCmd = nextCmd;
		}
		public String toString(final String indent) {
			return indent
					+ "<Cmd>\n"
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</Cmd>\n";
		}
		public Cmd getNextCmd() { return nextCmd; }
	}
	public class CmdSkip extends Cmd {

	    public CmdSkip(Cmd nextCmd) {
			super(nextCmd);
		}

		public String toString(final String indent) {
			return indent
					+ "<CmdSkip>\n"
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdSkip>\n";
		}
	}
	public class CmdAssi extends Cmd {
		private final Expression targetExpression;
		private final Expression sourceExpression;

		public CmdAssi(Expression targetExpression, Expression sourceExpression, Cmd nextCmd) {
			super(nextCmd);
			this.targetExpression = targetExpression;
			this.sourceExpression = sourceExpression;
		}

	    public String toString(final String indent) {
			return indent
					+ "<CmdAssi>\n"
					+ targetExpression.toString(indent + '\t')
					+ sourceExpression.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdAssi>\n";
		}

        public Expression getTargetExpression() {
            return targetExpression;
        }

        public Expression getSourceExpression() {
            return sourceExpression;
        }
	}
	public class CmdCond extends Cmd {
		private final Expression expression;
		private final Cmd ifCmd;
		private final Cmd elseCmd;

		public CmdCond(Expression expression, Cmd ifCmd, Cmd elseCmd, Cmd nextCmd) {
			super(nextCmd);
			this.expression = expression;
			this.ifCmd = ifCmd;
			this.elseCmd = elseCmd;
		}

	    public String toString(String indent) {
			return indent
					+ "<CmdCond>\n"
					+ expression.toString(indent + '\t')
					+ ifCmd.toString(indent + '\t')
					+ elseCmd.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdCond>\n";
		}

        public Expression getExpression() {
            return expression;
        }

        public Cmd getIfCmd() {
            return ifCmd;
        }

        public Cmd getElseCmd() {
            return elseCmd;
        }
	}
	public class CmdWhile extends Cmd {
		private final Expression expression;
		private final Cmd cmd;

		public CmdWhile(Expression expression, Cmd cmd, Cmd nextCmd) {
			super(nextCmd);
			this.expression = expression;
			this.cmd = cmd;
		}

	    public String toString(final String indent) {
			return indent
					+ "<CmdWhile>\n"
					+ expression.toString(indent + '\t')
					+ cmd.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdWhile>\n";
		}

        public Expression getExpression() {
            return expression;
        }

        public Cmd getCmd() {
            return cmd;
        }
	}
	public class CmdProcCall extends Cmd {
		
		private final RoutineCall routineCall;
		private final GlobalInits globalInit;

		public CmdProcCall(RoutineCall routineCall, GlobalInits globalInit, Cmd nextCmd) {
			super(nextCmd);
			this.routineCall = routineCall;
			this.globalInit = globalInit;
		}

		public String toString(final String indent) {
			return indent
					+ "<ExprCall>\n"
					+ routineCall.toString(indent + '\t')
					+ (globalInit!=null?globalInit.toString(indent + '\t'):"")
					+ super.toString(indent + '\t')
					+ indent
					+ "</ExprCall>\n";
		}

        public RoutineCall getRoutineCall() {
            return routineCall;
        }

        public GlobalInits getGlobalInit() {
            return globalInit;
        }
	}
	public class CmdInput extends Cmd {
		private final Expression expr;

		public CmdInput(Expression expr, Cmd nextCmd) {
			super(nextCmd);
			this.expr = expr;
		}

	    public String toString(String indent) {
			return indent
					+ "<CmdInput>\n"
					+ expr.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdInput>\n";
		}

        public Expression getExpr() {
            return expr;
        }
	}
	public class CmdOutput extends Cmd {
		private final Expression expr;

		public CmdOutput(Expression expr, Cmd nextCmd) {
			super(nextCmd);
			this.expr = expr;
		}

	    public String toString(String indent) {
			return indent
					+ "<CmdOutput>\n"
					+ expr.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</CmdOutput>\n";
		}

        public Expression getExpr() {
            return expr;
        }
	}
	public abstract class TypedIdent {
		
		public String toString(String indent) { return ""; }
		
	}
	public class TypedIdentIdent extends TypedIdent {
		
		private final Ident firstIdent;
		private final Ident ident;
		
		public TypedIdentIdent(Ident firstIdent, Ident ident){
			this.firstIdent = firstIdent;
			this.ident = ident;
		}
		
		public String toString(String indent) {
			return indent
					+ "<TypedIdentIdent>\n"
					+ firstIdent.toString(indent + '\t')
					+ ident.toString(indent + '\t')
					+ indent
					+ "</TypedIdentIdent>\n";
		}
		public Ident getIdent() {
			return firstIdent;
		}
		
		public Ident getTypeIdent() {
			return ident;
		}
	}
	public class TypedIdentType extends TypedIdent {
		private final Ident ident;
		private final Type type;
		
		public TypedIdentType(Ident ident, Type type){
			this.ident = ident;
			this.type = type;
		}
		public String toString(String indent){
			return indent
					+ "<TypedIdentType>\n"
					+ ident.toString(indent + '\t')
					+ type.toString(indent + '\t')
					+ indent
					+ "</TypedIdentType>\n";
		}
		public Ident getIdent() {
			return ident;
		}
		public Type getType() {
			return type;
		}
	}
	public abstract class Expression {
		public String toString(String indent) { return ""; }
	}
	
	public class ExprLiteral extends Expression {
		private final Literal literal;

		public ExprLiteral(Literal literal) {
			this.literal = literal;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprLiteral>\n"
					+ literal.toString(indent + '\t')
					+ indent
					+ "</ExprLiteral>\n";
		}

        public Literal getLiteral() {
            return literal;
        }
	}
	
	public class ExprStore extends Expression {
		private final Ident ident;
		private final boolean isInit;

		public ExprStore(Ident ident, boolean isInit) {
			this.ident = ident;
			this.isInit = isInit;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprStore>\n"
					+ ident.toString(indent + '\t')
					+ indent
					+ "\t<IsInit>" + isInit + "</IsInit>\n"
					+ indent
					+ "</ExprStore>\n";
		}

        public Ident getIdent() {
            return ident;
        }

        public boolean isInit() {
            return isInit;
        }
	}
	
	public class ExprFunCall extends Expression {
		private final RoutineCall routineCall;

		public ExprFunCall(RoutineCall routineCall) {
			this.routineCall = routineCall;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprCall>\n"
					+ routineCall.toString(indent + '\t')
					+ super.toString(indent + '\t')
					+ indent
					+ "</ExprCall>\n";
		}

        public RoutineCall getRoutineCall() {
            return routineCall;
        }
	}
	
	public class ExprMonadic extends Expression {
		private final Operator operator;
		private final Expression expr;

		public ExprMonadic(Operator operator,Expression expr) {
			this.operator = operator;
			this.expr = expr;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprMonadic>\n"
					+ operator.toString(indent + '\t')
					+ expr.toString(indent + '\t')
					+ indent
					+ "</ExprMonadic>\n";
		}

        public Operator getOperator() {
            return operator;
        }

        public Expression getExpr() {
            return expr;
        }
	}
	
	public final class ExprDyadic extends Expression {
		private final Operator operator;
		private final Expression expr1;
		private final Expression expr2;

		public ExprDyadic(Operator operator, Expression expr1, Expression expr2) {
			this.operator = operator;
			this.expr1 = expr1;
			this.expr2 = expr2;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprDyadic>\n"
					+ operator.toString(indent + '\t')
					+ expr1.toString(indent + '\t')
					+ expr2.toString(indent + '\t')
					+ indent
					+ "</ExprDyadic>\n";
		}

        public Operator getOperator() {
            return operator;
        }

        public Expression getExpr1() {
            return expr1;
        }

        public Expression getExpr2() {
            return expr2;
        }
	}
	
	public class RoutineCall {
		private final Ident ident;
		private final ExpressionList expressionList;
		
		public RoutineCall(Ident ident, ExpressionList expressionList) {
			this.ident = ident;
			this.expressionList = expressionList;
		}
		
		public String toString(String indent) {
			return indent
					+ "<RoutineCall>\n"
					+ ident.toString(indent + '\t')
					+ expressionList.toString(indent + '\t')
					+ indent
					+ "</RoutineCall>\n";
		}

        public Ident getIdent() {
            return ident;
        }

        public ExpressionList getExprList() {
            return expressionList;
        }
		
	}
	
	
	public class ExpressionList {
		private final Expression expression;
		private final ExpressionList expressionList;

		public ExpressionList(Expression expression, ExpressionList expressionList) {
			this.expression = expression;
			this.expressionList = expressionList;
		}

		public String toString(String indent) {
			return indent
					+ "<ExprList>\n"
					+ expression.toString(indent + '\t')
					+ (expressionList!=null?expressionList.toString(indent + '\t'):"")
					+ indent
					+ "</ExprList>\n";
		}

        public Expression getExpression() {
            return expression;
        }

        public ExpressionList getExpressionList() {
            return expressionList;
        }
	}
	
	public final class GlobalInits {
		private final Ident ident;
		private final GlobalInits globalInits;

		public GlobalInits(Ident ident, GlobalInits globalInits) {
			this.ident = ident;
			this.globalInits = globalInits;
		}

		public String toString(String indent) {
			return indent
					+ "<GlobInit>\n"
					+ ident.toString(indent + '\t')
					+ indent
					+ (globalInits != null?globalInits.toString(indent + '\t'):"<noNextElement/>\n")
					+ indent
					+ "</GlobInit>\n";
		}

        public Ident getIdent() {
            return ident;
        }

        public GlobalInits getGlobalInits() {
            return globalInits;
        }
	}
	
	public class GlobalImport {
		private final FlowMode flowMode;
		private final ChangeMode changeMode;
		private final Ident ident;
		private final GlobalImport nextGlobalImport;
		
		public GlobalImport(FlowMode flowMode, ChangeMode changeMode, Ident ident, GlobalImport globalImport){
			this.flowMode = flowMode;
			this.changeMode = changeMode;
			this.ident = ident;
			this.nextGlobalImport = globalImport;
		}
		
		public String toString(final String indent){
			return indent
					+ "<GlobalImport>\n"
					+ flowMode.toString(indent + '\t')
					+ changeMode.toString(indent + '\t')
					+ ident.toString(indent + '\t')
					+ nextGlobalImport.toString(indent + '\t')
					+ indent
					+ "</GloablImport>\n";
		}
		public FlowMode getFlowMode() {
			return flowMode;
		}
		public ChangeMode getChangeMode() {
			return changeMode;
		}
		public Ident getIdent() {
			return ident;
		}
		public GlobalImport getNextGlobalImport() {
			return nextGlobalImport;
		}
	}

	
	
}
