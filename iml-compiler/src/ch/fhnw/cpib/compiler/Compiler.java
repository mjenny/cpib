package ch.fhnw.cpib.compiler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;

import ch.fhnw.cpib.compiler.context.RecordTable;
import ch.fhnw.cpib.compiler.context.RoutineTable;
import ch.fhnw.cpib.compiler.context.Scope;
import ch.fhnw.cpib.compiler.context.StoreTable;
import ch.fhnw.cpib.compiler.error.ContextError;
import ch.fhnw.cpib.compiler.error.GrammarError;
import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.parser.AbsTree;
import ch.fhnw.cpib.compiler.parser.ConcTree;
import ch.fhnw.cpib.compiler.parser.Parser;
import ch.fhnw.cpib.compiler.scanner.ITokenList;
import ch.fhnw.cpib.compiler.scanner.Scanner;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.ExecutionError;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.HeapTooSmallError;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.CodeTooSmallError;
import ch.fhnw.lederer.virtualmachineHS2010.VirtualMachine;

public final class Compiler {
	private static final int CODE_SIZE = 1000;
	private static final int STORE_SIZE = 1000;
	
	private static RoutineTable routineTable = new RoutineTable();
	private static StoreTable globalStoreTable = new StoreTable();
	private static RecordTable globalRecordTable = new RecordTable();
	private static HashMap<String, String> globalIdentRecordTable = new HashMap<String, String>(); 
	private static Scope scope = null;
	private static IVirtualMachine vm = new VirtualMachine(CODE_SIZE, STORE_SIZE);
	
	public static IVirtualMachine getVM() {
		return vm;
	}
	
	public static StoreTable getGlobalStoreTable() {
		return globalStoreTable;
	}
	
	public static RecordTable getGlobalRecordTable() {
		return globalRecordTable;
	}
	
	public static RoutineTable getRoutineTable() {
		return routineTable;
	}
	
	public static Scope getScope() {
		return scope;
	}
	
	public static void setScope(final Scope scope) {
		Compiler.scope = scope;
	}
	
	public static HashMap<String, String> getGlobalIdentRecordTable() {
		return globalIdentRecordTable;
	}
	
	private Compiler() {
		throw new AssertionError("Instantiating utility class...");
	}
	
	public static synchronized void compile(BufferedReader source)
			throws IOException, LexicalError, GrammarError, ContextError, 
			HeapTooSmallError, CodeTooSmallError, ExecutionError {
		System.out.println("Code Preparation");
		String currentLine = "";
		StringBuilder program = new StringBuilder();
		
		while ((currentLine = source.readLine()) != null) {
			System.out.println(currentLine);
			program.append(currentLine+"\n");
		}
		
		System.out.println("Scanning:");
		ITokenList tokenList = null;
		Scanner scanner = new Scanner();
		try {
			tokenList = scanner.scan(new BufferedReader(new StringReader(program.toString())));
		} catch(Exception e) {
			System.out.println("ERROR! " + e.getMessage());
		}
		System.out.println("Success!");
		System.out.println("\nTokenList:");
		System.out.println(tokenList);
		System.out.println("\nParsing:");
		final Parser parser = new Parser(tokenList);
		final ConcTree.Program concTree = parser.parse();
		System.out.println("\nSuccess!");
		System.out.println("\nConcrete syntax tree:");
		System.out.println(concTree.toString(""));
		System.out.println("Generating abstract syntax tree:");
		final AbsTree.Program absTree = concTree.toAbstract();
		System.out.println("Success!");
		System.out.println("\nAbstract syntax tree:");
		System.out.println(absTree.toString(""));
		System.out.println("Context check:");
		absTree.check();
		System.out.println("Success!");
		System.out.println("\nCode generation:");
		absTree.code(0);
		System.out.println("Success!");
		
		System.out.println("\nExecuting:");
		vm.execute();
		System.out.println("\nSuccess!");
		
	}
	
	public static void main(String[] args) {
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream("res/code.iml"));
			Compiler.compile(new BufferedReader(isr));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
