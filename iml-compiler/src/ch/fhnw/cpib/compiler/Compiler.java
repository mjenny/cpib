package ch.fhnw.cpib.compiler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import ch.fhnw.cpib.compiler.error.GenerationError;
import ch.fhnw.cpib.compiler.error.GrammarError;
import ch.fhnw.cpib.compiler.error.LexicalError;
import ch.fhnw.cpib.compiler.generator.CodeGenerator;
import ch.fhnw.cpib.compiler.parser.AbsTree;
import ch.fhnw.cpib.compiler.parser.ConcTree;
import ch.fhnw.cpib.compiler.parser.Parser;
import ch.fhnw.cpib.compiler.scanner.ITokenList;
import ch.fhnw.cpib.compiler.scanner.Scanner;
import ch.fhnw.cpib.vm.Machine;
import ch.fhnw.cpib.vm.MachineError;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.ExecutionError;

public class Compiler {
	private static final int CODE_SIZE = 1000;
	private static final int STORE_SIZE = 1000;
	
	private static Machine vm = new Machine(CODE_SIZE, STORE_SIZE);
	
	public static Machine getVM() {
		return vm;
	}
	
	/*private Compiler() {
		throw new AssertionError("Instantiating utility class...");
	}*/
	
	public void compile(BufferedReader source) throws IOException, LexicalError, GrammarError, GenerationError, ExecutionError, MachineError {
		System.out.println("Compiling iml:");
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
		final CodeGenerator generator = new CodeGenerator(absTree);
		/**
		 * Not possible to check? Should probalby be seperated from generate()
		 *
		 * System.out.println("Context check:");
		 *
		 * generator.check();
		 * System.out.println("Success!");
		*/
		System.out.println("\nCode generation:");
		String code = generator.generate();
		System.out.println("Success!");
		System.out.println("\nExecuting:");

		vm.run(new BufferedReader(new StringReader(code)));
			
	}
	
	public static void main(String[] args) {
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream("res/code.iml"));
			Compiler compiler = new Compiler();
			compiler.compile(new BufferedReader(isr));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
