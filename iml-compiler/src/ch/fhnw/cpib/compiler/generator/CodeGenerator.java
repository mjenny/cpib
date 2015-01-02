package ch.fhnw.cpib.compiler.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.fhnw.cpib.compiler.error.GenerationError;
import ch.fhnw.cpib.compiler.parser.AbsTree.DeclarationProcedure;
import ch.fhnw.cpib.compiler.parser.AbsTree.Declaration;
import ch.fhnw.cpib.compiler.parser.AbsTree.DeclarationFunction;
import ch.fhnw.cpib.compiler.parser.AbsTree.*;
import ch.fhnw.cpib.compiler.scanner.enums.OperatorAttribute;
import ch.fhnw.cpib.compiler.scanner.enums.Terminals;


/**
 * This class generates vm code from a abstract syntax tree
 * 
 */
public class CodeGenerator {
	
	/**
	 * The starting point from the abstract syntax tree
	 */
	private final Program tree;
	
	/**
	 * The counter of the current line 
	 */
	private Integer lineCounter = 0;
	
	/**
	 * The code in a string
	 */
	private StringBuilder code = new StringBuilder();
	
	/**
	 * The storage for the addresses of the variables
	 */
	private HashMap<String,Integer> variables = new HashMap<String,Integer>();
	
	/**
	 * Helper for defined records
	 */
	private HashMap<String,List<TypedIdent>> records = new HashMap<String,List<TypedIdent>>();
	
	/**
	 * Helper for initialized records
	 */
	private HashMap<String,List<TypedIdent>> initRecords = new HashMap<String,List<TypedIdent>>();
	
	/**
	 * A multi dimension counter of commands
	 */
	private ArrayList<Integer> cmdCounter = new ArrayList<Integer>();
	
	/**
	 * The state of addLine if the line will added to the string or we just count the commands
	 */
	private boolean countingState = false;
	
	/**
	 * Constructor of the code generator
	 * @param Program The Abstract Tree that should be generated.
	 */
	public CodeGenerator(Program tree) {
		this.tree = tree;
	}
	
	/**
	 * This function starts the generation process
	 * @return String with the vm code
	 * @throws GenerationError
	 */
	public String generate() throws GenerationError {

		Declaration declaration = tree.getDeclarations();
		Cmd commands = tree.getCommands();

		// do store Declaration
		int storeCount = 0;
		Declaration currentDeclaration = declaration;
		while (currentDeclaration != null) {
			if (currentDeclaration instanceof DeclarationStore) {
				if(((DeclarationStore) currentDeclaration).getTypedIdent() instanceof TypedIdentType){
					TypedIdentType typedIdent = (TypedIdentType)((DeclarationStore) currentDeclaration).getTypedIdent();
					variables.put(typedIdent.getIdent().getName(), storeCount);
					storeCount++;
				}else if(((DeclarationStore) currentDeclaration).getTypedIdent() instanceof TypedIdentIdent){
					TypedIdentIdent typedIdent = (TypedIdentIdent)((DeclarationStore) currentDeclaration).getTypedIdent();
					//Check if TypeIdent is already declared
					
					if(records.containsKey(typedIdent.getTypeIdent().getName())) {
						variables.put(typedIdent.getIdent().getName(), storeCount);
						storeCount++;
					}else throw new GenerationError("Record "+typedIdent.getTypeIdent().getName()+" is not declared!");	
				}
				
			} else if (currentDeclaration instanceof DeclarationRecord) {
				
			}

			currentDeclaration = currentDeclaration.getNextDecl();
		}
		
		addLine("Alloc", storeCount);
		
		// do commands
		Cmd currentCmd = commands;
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		
		addLine("Stop");
		
		// do fun Declarations
		currentDeclaration = declaration;
		while (currentDeclaration != null) {
			if (declaration instanceof DeclarationFunction) buildRoutine(declaration, true);
			currentDeclaration = currentDeclaration.getNextDecl();
		}
		
		// do proc Declarations
		currentDeclaration = declaration;
		while (currentDeclaration != null) {
			if (declaration instanceof DeclarationProcedure) buildRoutine(declaration, false);
			currentDeclaration = currentDeclaration.getNextDecl();
		}
		// do record Declarations
		currentDeclaration = declaration;
		while(currentDeclaration != null){
			if(declaration instanceof DeclarationRecord) buildRecord(declaration);
			currentDeclaration = currentDeclaration.getNextDecl();
		}
		// TODO: replace routine call addresses
		
		// remove the last comma
		code.delete(code.length() - 2, code.length() - 1);
		
		// return the code in a string
		return code.toString();
	}
	
	private void buildRecord(Declaration declaration) {
		// TODO Auto-generated method stub

	}

	/**
	 * This builds the code for all kind of commands
	 * @param Cmd from the Abstract Tree
	 * @throws GenerationError
	 */
	private void buildCommands(Cmd cmd) throws GenerationError {
		
		// assignment of a expression to a variable 
		if (cmd instanceof CmdAssi) buildCmdAssi((CmdAssi) cmd);
		
		// code for a if/else condition 
		else if (cmd instanceof CmdCond) buildCmdCond((CmdCond) cmd);
		
		// code for a user input
		else if (cmd instanceof CmdInput) buildCmdInput((CmdInput) cmd);
		
		// code for a output to the console
		else if (cmd instanceof CmdOutput) buildCmdOutput((CmdOutput) cmd);
		
		// code for a call of a procedure c
		else if (cmd instanceof CmdProcCall) buildCmdProcCall((CmdProcCall) cmd);
		
		// jump to the next command. 
		else if (cmd instanceof CmdSkip) buildCmdSkip((CmdSkip) cmd);
		
		// code for a while loop 
		else if (cmd instanceof CmdWhile) buildCmdWhile((CmdWhile) cmd);
		
		else throw new GenerationError("unknown Command");
	}
	
	/**
	 * This build the code for a assignment of a expression to a variable
	 * @param Cmd from the Abstract Tree
	 * @throws GenerationError
	 */
	private void buildCmdAssi(CmdAssi cmd) throws GenerationError {
		if (cmd.getTargetExpression() instanceof ExprStore) {
			// resolve the source expression
			resolveExpression(cmd.getSourceExpression());
			
			// get the address for the target variable
			String variableName = ((ExprStore) cmd.getTargetExpression()).getIdent().getName();
			addLine("IntLoad", variables.get(variableName));
			
			// store the source in the target variable
			addLine("Store");
		} else throw new GenerationError("wrong target expression for Cmd Assi");
	}
	
	/**
	 * This build the code for a if/else condition
	 * @param Cmd from the Abstract Tree
	 * @throws GenerationError
	 */
	private void buildCmdCond(CmdCond cmd) throws GenerationError {
		
		// count the commands from the if part
		startCountingState();
		Cmd currentCmd = cmd.getIfCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		Integer cmdIfCount = stopCountingState();
		
		// count the commands from the else part
		startCountingState();
		currentCmd = cmd.getElseCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		Integer cmdElseCount = stopCountingState();
		
		// jump to the else part when the expression is false
		resolveExpression(cmd.getExpression());
		addLine("CondJump", lineCounter + cmdIfCount + 2);
		
		// build if commands
		currentCmd = cmd.getIfCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		addLine("UncondJump", lineCounter + cmdElseCount + 1);
		
		// build else commands
		currentCmd = cmd.getElseCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
	}
	
	/**
	 * This builds the code for a user input
	 * @param Cmd from the Abstract Tree
	 */
	private void buildCmdInput(CmdInput cmd) {
		
		ExprStore expr = (ExprStore) cmd.getExpr();
		String variableName = expr.getIdent().getName();
		
		addLine("IntLoad", variables.get(variableName));
		addLine("IntInput", variableName);
	}
	
	/**
	 * This builds the code for a output to the console
	 * @param Cmd from the Abstract Tree
	 */
	private void buildCmdOutput(CmdOutput cmd) {
		
		ExprStore expr = (ExprStore) cmd.getExpr();
		String variableName = expr.getIdent().getName();
		
		addLine("IntLoad", variables.get(variableName));
		addLine("Deref");
		addLine("IntOutput", variableName);
	}
	
	/**
	 * This builds the code for a call of a procedure
	 * @param Cmd from the Abstract Tree
	 * @throws GenerationError
	 */
	private void buildCmdProcCall(CmdProcCall cmd) throws GenerationError {
		addLine("Alloc", 0);
		ExpressionList currentList = cmd.getRoutineCall().getExprList();
		while (currentList != null) {
			resolveExpression(currentList.getExpression());
			currentList = currentList.getExpressionList();
		}
		addLine("Call", getCallReplacement(cmd.getRoutineCall().getIdent().getName()));
	}
	
	/**
	 * This builds a jump to the next command. Used for a Condition Command if the else command isn't needed.
	 * @param Cmd from the Abstract Tree
	 */
	private void buildCmdSkip(CmdSkip cmd) {
		addLine("UncondJump", lineCounter + 1);
	}
	
	/**
	 * This builds the code for a while loop
	 * @param  Cmd from the Abstract Tree
	 * @throws GenerationError
	 */
	private void buildCmdWhile(CmdWhile cmd) throws GenerationError {
		
		// count the commands for the expression handling
		startCountingState();
		resolveExpression(cmd.getExpression());
		Integer expCount = stopCountingState();
		
		// count the commands in the while loop
		startCountingState();
		Cmd currentCmd = cmd.getCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		Integer cmdCount = stopCountingState();
		
		// jump out of the wile when the expression is false
		resolveExpression(cmd.getExpression());
		addLine("CondJump", lineCounter + cmdCount + 2);
		
		// build the commands
		currentCmd = cmd.getCmd();
		while (currentCmd != null) {
			buildCommands(currentCmd);
			currentCmd = currentCmd.getNextCmd();
		}
		
		// jump back at the start of expression handling
		addLine("UncondJump", lineCounter - cmdCount - expCount - 1);
	}
	
	/**
	 * This builds the code for the expressions recursively
	 * @param expr
	 * @throws GenerationError
	 */
	private void resolveExpression(Expression expr) throws GenerationError {
		
		// handles bool operations, relative operations and maths
		if (expr instanceof ExprDyadic) resolveExprDyadic((ExprDyadic) expr);
		
		// calls a function
		else if (expr instanceof ExprFunCall) resolveExprFunCall((ExprFunCall) expr);
		
		// loads a literal
		else if (expr instanceof ExprLiteral) resolveExprLiteral((ExprLiteral) expr); 
		
		// inverts the value of a expression
		else if (expr instanceof ExprMonadic) resolveExprMonadic((ExprMonadic) expr);
		
		// loads a variable from the store
		else if (expr instanceof ExprStore) resolveExprStore((ExprStore) expr);
		
		else throw new GenerationError("unknown expression");
	}
	
	/**
	 * This handles bool operations, relative operations and maths
	 * @param ExprDyadic from the AbstractTree
	 * @throws GenerationError
	 * TODO: Dot behandeln
	 */
	private void resolveExprDyadic(ExprDyadic e) throws GenerationError {
		if (e.getOperator().getTerminal() == Terminals.BOOLOPR) {
			
			// resolve expression 1
			resolveExpression(e.getExpr1());
			
			// count the commands for expression 2
			startCountingState();
			resolveExpression(e.getExpr2());
			Integer cmdCount = stopCountingState();
			
			// jump if expression 1 is false
			if (e.getOperator().getOperatorAttribute() == OperatorAttribute.COR) addLine("IntInv");
			addLine("CondJump", lineCounter + cmdCount + 1);
			
			// resolve expression 2
			resolveExpression(e.getExpr2());
			
		} else if (e.getOperator().getTerminal() == Terminals.ADDOPR || 
				   e.getOperator().getTerminal() == Terminals.MULTOPR ||
				   e.getOperator().getTerminal() == Terminals.RELOPR ||
				   e.getOperator().getTerminal() == Terminals.DOTOPR) {
			
			resolveExpression(e.getExpr1());
			resolveExpression(e.getExpr2());
			
			switch (String.valueOf(e.getOperator().getOperatorAttribute())) {
				case "PLUS": addLine("IntAdd"); break;
				case "MINUS": addLine("IntSub"); break;
				case "TIMES": addLine("IntMult"); break;
				case "DIV": addLine("IntDiv"); break;
				case "MOD": addLine("IntMod"); break;
				//TODO: what to do in case of DOT
				case "DOT": break;
				default: addLine("Int" + e.getOperator().getOperatorAttribute()); break;
			}
		} 
		else throw new GenerationError("unknown terminal for a Dyadic Expression");
	}
	
	/**
	 * this calls a function
	 * @param ExprFunCall from the AbstractTree
	 * @throws GenerationError
	 */
	private void resolveExprFunCall(ExprFunCall e) throws GenerationError {
		addLine("Alloc", 1);
		ExpressionList currentList = e.getRoutineCall().getExprList();
		while (currentList != null) {
			resolveExpression(currentList.getExpression());
			currentList = currentList.getExpressionList();
		}
		addLine("Call", getCallReplacement(e.getRoutineCall().getIdent().getName()));
	}
	
	/**
	 * This loads a literal
	 * @param ExprLiteral from the AbstractTree
	 */
	private void resolveExprLiteral(ExprLiteral e) {
		addLine("IntLoad", e.getLiteral().getIntVal());
	}
	
	/**
	 * This inverts the value of a expression
	 * @param ExprMonadic from the AbstractTree
	 * @throws GenerationError
	 */
	private void resolveExprMonadic(ExprMonadic e) throws GenerationError {
		if (e.getOperator().getTerminal() == Terminals.NOT ||
		    (e.getOperator().getTerminal() == Terminals.ADDOPR && 
		    e.getOperator().getOperatorAttribute() == OperatorAttribute.MINUS)) {
				resolveExpression(e.getExpr());
				addLine("IntInv");
		} else throw new GenerationError("unknown prefix for a monadic expression");
	}
	
	/**
	 * This loads a variable from the store
	 * @param ExprStore from the AbstractTree
	 */
	private void resolveExprStore(ExprStore e) {
		addLine("IntLoad", variables.get(e.getIdent().getName()));
		addLine("Deref");
	}

	private void buildRoutine(Declaration declaration, boolean isFun) {
		// TODO: Routines
	}

	/**
	 * This adds a line of code with a command string
	 * @param String of the command
	 */
	private void addLine(String cmd) {
		addLine(cmd, "");
	}
	
	/**
	 * This adds a line of code with a command string and a int value for a param
	 * @param String of the command
	 * @param Integer value as param
	 */
	private void addLine(String cmd, Integer param) {
		addLine(cmd, String.valueOf(param));
	}
	
	/**
	 * This adds a line of code and a string of params
	 * @param String of the command
	 * @param String of the params
	 */
	private void addLine(String cmd, String params) {
		if (countingState) cmdCounter.set(cmdCounter.size() - 1, cmdCounter.get(cmdCounter.size() - 1) + 1);
		else {
			code.append("(");
			code.append(lineCounter);
			code.append(',');
			code.append(cmd);
			if (params.length() > 0) {
				code.append(' ');
				code.append(params);
			}
			code.append("),\n");
			lineCounter++;
		}
	}
	
	/**
	 * Increase the level of command counting
	 */
	private void startCountingState() {
		cmdCounter.add(0);
		countingState = true;
	}
	
	/**
	 * Decrease the level of command counting
	 * @return Integer of the amount of the commands from the top level
	 */
	private Integer stopCountingState() {
		Integer result = cmdCounter.get(cmdCounter.size() - 1);
		cmdCounter.remove(cmdCounter.size() - 1);
		if (cmdCounter.size() == 0) countingState = false;
		return result;
	}
	
	/**
	 * Return a replacement string for a routine
	 * @param String of the name of the routine
	 * @return String of the replacement
	 */
	private String getCallReplacement(String name) { return ">>" + name + "<<"; }
}