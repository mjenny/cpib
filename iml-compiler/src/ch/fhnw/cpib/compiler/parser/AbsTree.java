package ch.fhnw.cpib.compiler.parser;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.fhnw.cpib.compiler.scanner.enums.ModeAttribute;
import ch.fhnw.cpib.compiler.scanner.enums.OperatorAttribute;
import ch.fhnw.cpib.compiler.scanner.enums.Terminals;
import ch.fhnw.cpib.compiler.scanner.enums.TypeAttribute;
import ch.fhnw.cpib.compiler.scanner.token.*;
import ch.fhnw.cpib.compiler.scanner.token.Mode.*;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.HeapTooSmallError;
import ch.fhnw.lederer.virtualmachineHS2010.IVirtualMachine.CodeTooSmallError;
import ch.fhnw.cpib.compiler.context.GlobImp;
import ch.fhnw.cpib.compiler.context.Procedure;
import ch.fhnw.cpib.compiler.context.Record;
import ch.fhnw.cpib.compiler.context.Routine;
import ch.fhnw.cpib.compiler.context.Routine.RoutineTypes;
import ch.fhnw.cpib.compiler.context.Scope;
import ch.fhnw.cpib.compiler.context.Store;
import ch.fhnw.cpib.compiler.error.ContextError;
import ch.fhnw.cpib.compiler.Compiler;

@SuppressWarnings("rawtypes")
public interface AbsTree {
	
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
							+ (programParameter!=null?programParameter.toString(indent + '\t'):"\t<noProgramParameter/>\n")
							+ (declaration!=null?declaration.toString(indent + '\t'):"\t<noDeclarations/>\n")
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
		
		public int getLine(){
			return ident.getLine();
		}
		
		public void check() throws ContextError, HeapTooSmallError {
			if(programParameter != null)
				programParameter.checkDeclaration();
			if(declaration != null)
				declaration.checkDeclaration();
			if(declaration != null)
				declaration.check(-1);
			Compiler.setScope(
					new Scope(Compiler.getGlobalStoreTable().clone()));
			cmd.check(false);
		}
		
		public int code(final int loc) throws CodeTooSmallError {
			int loc1 = cmd.code(loc);
			Compiler.getVM().Stop(loc1);
			if(declaration != null)
				loc1 = declaration.code(loc1 + 1);
			for (Routine routine : Compiler.getRoutineTable().getTable().values()) {
				routine.codeCalls();
			}
			return loc1;
		}
		
	}
	
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
					+ (nextProgramParameter!=null?nextProgramParameter.toString(indent + '\t'):"\t<noNextProgramParameter/>\n")
					+ indent
					+ "</ProgramParameter>\n";
		}
		
		public FlowMode getFlowMode() { return flowMode; }
		public ChangeMode getChangeMode() { return changeMode; }
		public TypedIdent getTypedIdent() { return typedIdent; }
		public ProgramParameter getNextProgramParameter() { return nextProgramParameter; }
		
		public Store getStore() {
        	return new Store(typedIdent.getIdent().getName(),
        			typedIdent,
        			changeMode.getAttribute() == ModeAttribute.CONST);
        }
		
		public void checkDeclaration() throws ContextError, HeapTooSmallError {
			Store store = getStore();
    		if (!Compiler.getGlobalStoreTable().addStore(store.getIdent(), store)) {
	        	throw new ContextError("Store already declared: "
	        			+ typedIdent.getIdent().getName(), typedIdent.getIdent().getLine());
	        }
    		if (!store.isRecord()) {
    	        if (((TypedIdentType)typedIdent).getType().getAttribute() == TypeAttribute.BOOL) {
    	        	store.setAddress(Compiler.getVM().BoolInitHeapCell());
    	        	store.setRelative(false);
    	        } else {
    	        	store.setAddress(Compiler.getVM().IntInitHeapCell());
    	        	store.setRelative(false);
    	        }
    		}
    		if(nextProgramParameter!=null)
    			nextProgramParameter.checkDeclaration();
		}
		
		public Store check() throws ContextError {
			Store store = getStore();
        	TypedIdent t = store.getType();
        	Ident ident = typedIdent.getIdent();
        	if (t instanceof TypedIdentIdent) {
        		if (!Compiler.getScope().getStoreTable().addStore(ident.getName(), store)) {
            		throw new ContextError("Ident already declared: "
            				+ ident.getName(), ident.getLine());
            	}
        	} else {
        		if (!Compiler.getScope().getStoreTable().addStore(ident.getName(), store)) {
            		throw new ContextError("Ident already declared: "
            				+ ident.getName(), ident.getLine());
            	}
        	}
        	
        	return store;
		}
	}
	
	public abstract class Declaration {
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
		
		public abstract void checkDeclaration() throws ContextError, HeapTooSmallError;
		public abstract int check(int locals) throws ContextError, HeapTooSmallError;
		public abstract int code(int loc) throws CodeTooSmallError;
	}
	
	public class RecordField {
		private final DeclarationRecordField declarationRecordField;
		private final DeclarationRecordField nextDeclarationRecordField;
		
		public RecordField(DeclarationRecordField delcarationRecordField){
			this.declarationRecordField = delcarationRecordField;
			this.nextDeclarationRecordField = declarationRecordField.getNextDeclarationRecordField();
		}
		
		public String toString(String indent){
			return indent
					+ "<RecordField>\n"
					+ declarationRecordField.toString(indent + '\t')
					+ indent
					+ "</RecordField>\n";
		}
		
		public DeclarationRecordField getDeclarationRecordField(){
			return declarationRecordField;
		}
		
		public DeclarationRecordField getNextDeclarationRecordField(){
			return nextDeclarationRecordField;
		}
		
		
	}
	
	public class DeclarationFunction extends Declaration{
		private final Ident ident;
		private final Parameter param;
		private final DeclarationStore returnDecl;
		private final GlobalImport globImp;
		private final Declaration dcl;
		private final Declaration nextDecl;
		private final Cmd cmd;
		
		public DeclarationFunction(Ident ident, Parameter param, DeclarationStore returnDecl, GlobalImport globImp, Declaration dcl, Cmd cmd, Declaration nextDecl){
			super(nextDecl);
			this.ident = ident;
			this.param = param;
			this.returnDecl = returnDecl;
			this.globImp = globImp;
			this.dcl = dcl;
			this.nextDecl = nextDecl;
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
		
		public void checkDeclaration() throws ContextError, HeapTooSmallError {
			if (nextDecl != null) nextDecl.checkDeclaration();
		}

		@Override
		public int check(int locals) throws ContextError, HeapTooSmallError {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int code(int loc) throws CodeTooSmallError {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public class DeclarationProcedure extends Declaration {
		private final Ident ident;
		private final Parameter param;
		private final GlobalImport globalImport;
		private final Declaration decl;
		private final Declaration nextDecl;
		private final Cmd cmd;
		private int countDecls = 0;

		public DeclarationProcedure(Ident ident, Parameter parameter, GlobalImport globalImport, Declaration decl, Cmd cmd, Declaration nextDecl) {
			super(nextDecl);
			this.ident = ident;
			this.param = parameter;
			this.globalImport = globalImport;
			this.decl = decl;
			this.nextDecl = nextDecl;
			this.cmd = cmd;
			
			Declaration tmpDecl = decl;
			while (tmpDecl != null) {
				++countDecls;
				tmpDecl = tmpDecl.getNextDecl();
			}
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
		
		public int getCount() {
			return countDecls;
		}
		
		public Ident getIdent() { return ident;}
		public Parameter getParam() { return param;}
		public GlobalImport getGlobImp() { return globalImport;}
		public Declaration getDecl() { return decl;}
		public Cmd getCmd() { return cmd;}
		
		@Override
		public void checkDeclaration() throws ContextError, HeapTooSmallError {
			Procedure procedure = new Procedure(
					ident.getName());
			Compiler.setScope(procedure.getScope());
			
			if (!Compiler.getRoutineTable().addRoutine(procedure)) {
				throw new ContextError("Ident already declared: "
						+ ident.getName(), ident.getLine());
			}
			
			param.check(procedure);
			Compiler.setScope(null);
			if (nextDecl != null) nextDecl.checkDeclaration();
		}
		
		@Override
		public int check(final int locals)
				throws ContextError, HeapTooSmallError {
			if (locals >= 0) {
				throw new ContextError(
						"Function declarations are only allowed globally!",
						ident.getLine());
			}
			Routine routine = Compiler.getRoutineTable().getRoutine(
					ident.getName());
			Compiler.setScope(routine.getScope());
			if(globalImport != null)
				globalImport.check(routine);
			int localsCount = param.calculateAddress(routine.getParamList().size(), 0);
			
			if(decl!=null)
				decl.check(localsCount);
			
			cmd.check(false);
			Compiler.setScope(null);
			return -1;
		}
		
		@Override
		public int code(final int loc) throws CodeTooSmallError {
			int loc1 = loc;
			Routine routine = Compiler.getRoutineTable().getRoutine(
					ident.getName());
			Compiler.setScope(routine.getScope());
			routine.setAddress(loc1);
			Compiler.getVM().Enter(
					loc1++,
					routine.getInOutCopyCount() + getCount(),
					0);
			loc1 = param.codeIn(
					loc1,
					routine.getParamList().size(),
					0);
			loc1 = cmd.code(loc1);
			loc1 = param.codeOut(loc1,
					routine.getParamList().size(),
					0);
			Compiler.getVM().Return(loc1++, 0);
			Compiler.setScope(null);
			return loc1;
			//return (nextDecl!=null?nextDecl.code(loc1):loc1);
		}
	}
	
	public class DeclarationStore extends Declaration {
		private final ChangeMode changeMode;
		private final TypedIdent typedIdent;
		private final Declaration nextDeclaration;

		public DeclarationStore(ChangeMode changeMode, TypedIdent typedIdent, Declaration nextDeclaration) {
			super(nextDeclaration);
			this.changeMode = changeMode;
			this.typedIdent = typedIdent;
			this.nextDeclaration = nextDeclaration;
		}

		public String toString(final String indent) {
			return indent
					+ "<DeclStore>\n"
					+ changeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ indent
					+ "</DeclStore>\n"
					+ super.toString(indent + '\t');
		}
		public ChangeMode getChangeMode() {
            return changeMode;
        }

        public TypedIdent getTypedIdent() {
            return typedIdent;
        }
        
        public Store getStore() {
        	return new Store(typedIdent.getIdent().getName(),
        			typedIdent,
        			changeMode.getAttribute() == ModeAttribute.CONST);
        }
        
        @Override
        public int check(final int locals)
        		throws ContextError, HeapTooSmallError {
        	if (locals < 0) {
        		return -1;
        	} else {
        		Store store = check();
		       if (store.isRecord()) {
		    	   return locals;
		       } else {
			       store.setAddress(2 + locals + 1);
			       store.setRelative(true);
			       store.setReference(false);
			       return locals + 1;
		       }
	        }
        }
        
        @Override
        public void checkDeclaration() throws ContextError, HeapTooSmallError {
        	if (this.getTypedIdent() instanceof TypedIdentIdent) {
        		if(Compiler.getGlobalStoreTable().containsIdent(typedIdent.getIdent().getName())) {
        			throw new ContextError("Store already declared: "
    	        			+ typedIdent.getIdent().getName(), typedIdent.getIdent().getLine());
        		}
        		Ident record = (Ident)typedIdent.getType();
        		if(!Compiler.getGlobalRecordTable().containsRecord(record.getName())) {
        			throw new ContextError("Record is not declared: " 
        					+ typedIdent.getIdent().getName(),
        					+ typedIdent.getIdent().getLine());
        		}
        		
        		Record r = Compiler.getGlobalRecordTable().getRecord(record.getName());
        		for (DeclarationRecordField recordField : r.getRecordFields()) {
        			if (changeMode.getAttribute() == ModeAttribute.CONST && recordField.getChangeMode().getAttribute() != changeMode.getAttribute())
        				throw new ContextError("Store " + typedIdent.getIdent().getName()
        						+ " is defined as CONST but " + record.getName()
        						+ " contains a field " + recordField.getTypedIdent().getIdent().getName()
        						+ " which is not CONST", typedIdent.getIdent().getLine());
        			Ident i = new Ident(typedIdent.getIdent().getName() + "." + recordField.getTypedIdent().getIdent().getName());
        			TypedIdent t;
        			if (recordField.getTypedIdent() instanceof TypedIdentIdent) {
        				t = new TypedIdentIdent(i, ((TypedIdentIdent)recordField.getTypedIdent()).getType());
        			} else {
        				t = new TypedIdentType(i, ((TypedIdentType)recordField.getTypedIdent()).getType());
        			}
        			Store store = new Store(i.getName(), t, (recordField.getChangeMode().getAttribute()==ModeAttribute.CONST));
        			if (((TypedIdentType)recordField.getTypedIdent()).getType().getAttribute() == TypeAttribute.BOOL) {
        	        	store.setAddress(Compiler.getVM().BoolInitHeapCell());
        	        	store.setRelative(false);
        	        } else {
        	        	store.setAddress(Compiler.getVM().IntInitHeapCell());
        	        	store.setRelative(false);
        	        }
        			Compiler.getGlobalStoreTable().addStore(i.getName(), store);
        		}
        		Compiler.getGlobalStoreTable().addStore(typedIdent.getIdent().getName(), getStore());
        		Compiler.getGlobalIdentRecordTable().put(typedIdent.getIdent().getName(), record.getName());
        	} else {
        		Store store = getStore();
        		if (!Compiler.getGlobalStoreTable().addStore(store.getIdent(), store)) {
    	        	throw new ContextError("Store already declared: "
    	        			+ typedIdent.getIdent().getName(), typedIdent.getIdent().getLine());
    	        }
        		if (!store.isRecord()) {
	    	        if (((TypedIdentType)typedIdent).getType().getAttribute() == TypeAttribute.BOOL) {
	    	        	store.setAddress(Compiler.getVM().BoolInitHeapCell());
	    	        	store.setRelative(false);
	    	        } else {
	    	        	store.setAddress(Compiler.getVM().IntInitHeapCell());
	    	        	store.setRelative(false);
	    	        }
        		}
        	}
        	if (nextDeclaration != null) nextDeclaration.checkDeclaration();
        }
        
        public Store check() throws ContextError {
        	Store store = getStore();
        	TypedIdent t = store.getType();
        	Ident ident = typedIdent.getIdent();
        	if (t instanceof TypedIdentIdent) {
        		if (!Compiler.getScope().getStoreTable().addStore(ident.getName(), store)) {
            		throw new ContextError("Ident already declared: "
            				+ ident.getName(), ident.getLine());
            	}
        	} else {
        		if (!Compiler.getScope().getStoreTable().addStore(ident.getName(), store)) {
            		throw new ContextError("Ident already declared: "
            				+ ident.getName(), ident.getLine());
            	}
        	}
        	
        	return store;
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	return loc;
        }
	}
	
	public class DeclarationRecord extends Declaration {
		private final Ident ident;
		private final RecordField recordField;
		private final Declaration nextDeclaration;
		
		public DeclarationRecord(Ident ident, RecordField recordField, Declaration nextDeclaration) {
			super(nextDeclaration);
			this.ident = ident;
			this.recordField = recordField;
			this.nextDeclaration = nextDeclaration;
		}
		public String toString(final String indent) {
			return indent
					+ "<DeclarationRecord>\n"
					+ ident.toString(indent + '\t')
					+ recordField.toString(indent + '\t')
					+ indent
					+ "</DeclarationRecord>\n"
					+ super.toString(indent + "\t");
		}
		
		public Ident getIdent() { return ident; }
		public RecordField getRecordField(){ return recordField; }
		
		public void checkDeclaration() throws ContextError, HeapTooSmallError {
			if(Compiler.getGlobalRecordTable().containsRecord(this.ident.getName())) {
				throw new ContextError("Record already declared: "
						+ this.ident.getName(), this.ident.getLine());
			}
			ArrayList<DeclarationRecordField> recordFields = new ArrayList<DeclarationRecordField>();
			DeclarationRecordField currentRecordField = recordField.getDeclarationRecordField();
			currentRecordField.checkDeclaration();
			while (currentRecordField != null) {
				for(DeclarationRecordField r : recordFields) {
					if (r.getTypedIdent().getIdent().getName().equals(currentRecordField.getTypedIdent().getIdent().getName()))
						throw new ContextError("Field " + currentRecordField.getTypedIdent().getIdent().getName()
								+ " in record "  + this.ident.getName()
								+ " already declared", currentRecordField.getTypedIdent().getIdent().getLine());
				}
				recordFields.add(currentRecordField);
				currentRecordField = currentRecordField.getNextDeclarationRecordField();
			}
			Record r = new Record(this.ident.getName(), recordFields);
			Compiler.getGlobalRecordTable().addRecord(r);
			if (nextDeclaration != null) nextDeclaration.checkDeclaration();
		}
		
		@Override
		public int check(int locals) throws ContextError, HeapTooSmallError {
			// TODO Auto-generated method stub
			if (locals < 0) {
        		return -1;
        	} else {
        		return locals;
        	}
		}
		
		@Override
		public int code(int loc) throws CodeTooSmallError {
			// TODO Auto-generated method stub
			return loc;
		}
		
		
	}
	
	public class DeclarationRecordField {
		private final ChangeMode changeMode;
		private final TypedIdent typedIdent;
		private final DeclarationRecordField nextDeclarationRecordField;

		public DeclarationRecordField(ChangeMode changeMode, TypedIdent typedIdent, DeclarationRecordField nextDeclarationRecordField) {
			this.changeMode = changeMode;
			this.typedIdent = typedIdent;
			this.nextDeclarationRecordField = nextDeclarationRecordField;
		}

		public String toString(final String indent) {
			return indent
					+ "<DeclarationRecordField>\n"
					+ changeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ indent
					+ "</DeclarationRecordField>\n"
					+ ((nextDeclarationRecordField!=null)?nextDeclarationRecordField.toString(indent + '\t'):"");
		}
		public ChangeMode getChangeMode() {
            return changeMode;
        }

        public TypedIdent getTypedIdent() {
            return typedIdent;
        }
        
        public DeclarationRecordField getNextDeclarationRecordField() {
        	return nextDeclarationRecordField;
        }
        
        public void checkDeclaration() throws ContextError {
        	if(typedIdent instanceof TypedIdentIdent)
        		throw new ContextError("Currently not allowed to set type of a field to a record. Field: " 
        				+ typedIdent.getIdent().getName(), typedIdent.getIdent().getLine());
        }

	}
	
	//18.12.
	public class Parameter {
		private final FlowMode flowMode;
		private final MechMode mechMode;
		private final DeclarationStore declarationStorage;
		private final Parameter nextParam;
		private Store store;

		public Parameter(FlowMode flowMode, MechMode mechMode, DeclarationStore declarationStorage, Parameter nextParam) {
			this.flowMode = flowMode;
			this.mechMode = mechMode;
			this.declarationStorage = declarationStorage;
			this.nextParam = nextParam;
		}

		public String toString(String indent) {
			return indent
					+ "<Parameter>\n"
					+ flowMode.toString(indent + '\t')
					+ mechMode.toString(indent + '\t')
					+ declarationStorage.changeMode.toString(indent + '\t')
					+ declarationStorage.typedIdent.toString(indent + '\t')
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
        
        public DeclarationStore getDeclarationStore() {
        	return declarationStorage;
        }

        public ChangeMode getChangeMode() {
            return declarationStorage.getChangeMode();
        }

        public Parameter getNextParam() {
            return nextParam;
        }
        
        public TypedIdent getTypedIdent() {
        	return declarationStorage.getTypedIdent();
        }
        
        public int getLine() {
        	return declarationStorage.getTypedIdent().getIdent().getLine();
        }
        
        public void check(final Routine routine) throws ContextError {
        	store = declarationStorage.check();
        	switch (flowMode.getAttribute()) {
        		case IN:
			        if (mechMode.getAttribute() == ModeAttribute.REF && !store.isConst()) {
			        	throw new ContextError(
			        			"IN reference parameter can not be var! Ident: "
			        					+ store.getIdent(),
			        					declarationStorage.getTypedIdent().getIdent().getLine());
			        }
			        store.initialize();
			        break;
		        case INOUT:
		        	if (routine.getRoutineType() != RoutineTypes.PROCEDURE) {
		        		throw new ContextError(
		        				"INOUT parameter in function declaration! Ident: "
		        						+ store.getIdent(),
		        						declarationStorage.getTypedIdent().getIdent().getLine());
		        	}
		        	
		        	if (store.isConst()) {
		        		throw new ContextError(
		        				"INOUT parameter can not be constant! Ident: "
		        						+ store.getIdent(),
		        						declarationStorage.getTypedIdent().getIdent().getLine());
		        	}
		        	
		        	store.initialize();
		        	break;
		        case OUT:
		        	if (routine.getRoutineType() != RoutineTypes.PROCEDURE) {
		        		throw new ContextError(
		        				"OUT parameter in function declaration! Ident: "
		        						+ store.getIdent(),
		        						declarationStorage.getTypedIdent().getIdent().getLine());
		        	}
		        	break;
		        default:
		        	break;
        	}
        	
        	if (store.isRecord())
        		throw new ContextError("Record " + declarationStorage.getTypedIdent().getIdent().getName()
        				+ " cannot yet be hand over to a routine", declarationStorage.getTypedIdent().getIdent().getLine());
        	
        	Mode changeMode = new Mode(Terminals.CHANGEMODE, ModeAttribute.CONST);
        	
        	if (!store.isConst()) {
        		changeMode = new Mode(Terminals.CHANGEMODE, ModeAttribute.CONST);
        	}
        	
	        routine.addParam(new ch.fhnw.cpib.compiler.context.Parameter(
	        getFlowMode(),
	        getMechMode(),
	        changeMode,
	        store.getType()));
	        if (nextParam != null)
	        	nextParam.check(routine);
        }
        
        public void checkInit() throws ContextError {
	        if (flowMode.getAttribute() == ModeAttribute.OUT) {
		        if (!((Store)Compiler.getScope().getStoreTable().getStore(
			        declarationStorage.getTypedIdent().getIdent().getName())).isInitialized()) {
			        	throw new ContextError(
			        			"OUT parameter is never initialized! Ident: "
			        					+ declarationStorage.getTypedIdent().getIdent().getName(),
			        					declarationStorage.getTypedIdent().getIdent().getLine());
		        }
	        }
	        if(nextParam != null)
	        	nextParam.checkInit();
        }
        
        public int calculateAddress(final int count, final int locals) {
        	int locals1 = locals;
        	if (flowMode.getAttribute() == ModeAttribute.IN
        			|| mechMode.getAttribute() == ModeAttribute.REF) {
        		store.setAddress(-count);
        		store.setRelative(true);
        		if (mechMode.getAttribute() == ModeAttribute.REF) {
        			store.setReference(true);
        		} else {
        			store.setReference(false);
        		}
        	} else {
		        store.setAddress(2 + ++locals1);
		        store.setRelative(true);
		        store.setReference(false);
        	}
        	
        	return (nextParam!=null?nextParam.calculateAddress(count - 1, locals1):locals1);
        }
        
        public int codeIn(final int loc, final int count, final int locals)
        		throws CodeTooSmallError {
        	int locals1 = locals;
        	int loc1 = loc;
        	if (flowMode.getAttribute() != ModeAttribute.IN
        			&& mechMode.getAttribute() == ModeAttribute.COPY) {
        		if (flowMode.getAttribute() == ModeAttribute.INOUT) {
        			Compiler.getVM().CopyIn(loc1++, -count, 3 + locals1);
        		}
        		locals1++;
        	}
        	return (nextParam != null?nextParam.codeIn(loc1, count - 1, locals1):loc);
        }
        
        public int codeOut(final int loc, final int count, final int locals)
        		throws CodeTooSmallError {
        	int locals1 = locals;
        	int loc1 = loc;
        	if (flowMode.getAttribute() != ModeAttribute.IN
        			&& mechMode.getAttribute() == ModeAttribute.COPY) {
        		Compiler.getVM().CopyOut(loc1++, 2 + ++locals1, -count);
        	}
        	return (nextParam!=null?nextParam.codeOut(loc1, count - 1, locals1):loc);
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
	
	
	public abstract class Cmd{
		public abstract Cmd getNextCmd();
		public abstract int getLine();
		public abstract String toString(final String ident);
		public abstract void check(boolean canInit) throws ContextError;
		public abstract int code(int loc) throws CodeTooSmallError;
	}
	
	public class CmdSkip extends Cmd {

		private final Cmd nextCmd;

		public CmdSkip(Cmd nextCmd) {
	    	this.nextCmd = nextCmd;
		}

		@Override
		public String toString(final String indent) {
			return indent
					+ "<CmdSkip>\n"
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</CmdSkip>\n";
		}
		
		@Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
		
		@Override
		public int getLine() {
			return -1;
		}

		@Override
		public void check(final boolean canInit) throws ContextError {
			// TODO Auto-generated method stub
			if (nextCmd != null)
				nextCmd.check(canInit);
		}

		@Override
		public int code(int loc) throws CodeTooSmallError {
			// TODO Auto-generated method stub
			return (nextCmd!=null?nextCmd.code(loc):loc);
		}
	}
	
	public class CmdAssi extends Cmd {
		private Expression targetExpression;
		private Expression sourceExpression;
		private final Cmd nextCmd;

		public CmdAssi(Expression targetExpression, Expression sourceExpression, Cmd nextCmd) {
			this.targetExpression = targetExpression;
			this.sourceExpression = sourceExpression;
			this.nextCmd = nextCmd;
		}

		@Override
	    public String toString(final String indent) {
			return indent
					+ "<CmdAssi>\n"
					+ targetExpression.toString(indent + '\t')
					+ sourceExpression.toString(indent + '\t')
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</CmdAssi>\n";
		}

        public Expression getTargetExpression() {
            return targetExpression;
        }

        public Expression getSourceExpression() {
            return sourceExpression;
        }
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
        
        @Override
        public int getLine() {
        	return targetExpression.getLine();
        }
        
        @Override
        public void check(final boolean canInit) throws ContextError {
        	Type typeL;
        	Object tmp = targetExpression.checkL(canInit);
        	if (tmp instanceof TypedIdentType) {
        		typeL = ((TypedIdentType)tmp).getType();
        	} else {
        		if(tmp instanceof TypedIdentIdent)
        			throw new ContextError(
    	        			"Only record fields can be used in assingments."
    	        					+ " Record " + ((TypedIdentIdent)tmp).getIdent().getName()
    	        					+ " cannot be part of an assignment",
    	        					targetExpression.getLine());
        		typeL = (Type)tmp;
        	}
        	
        	Type typeR;
        	tmp = sourceExpression.checkR();
        	if (tmp instanceof TypedIdentType) {
        		typeR = ((TypedIdentType)tmp).getType();
        	} else {
        		typeR = (Type)tmp;
        	}
        	

	        if (typeR.getAttribute() != typeL.getAttribute()) {
	        	throw new ContextError(
	        			"Types in assignemt don't match!: "
	        			, targetExpression.getLine());
	        }
	        
	        if(nextCmd != null)
	        	nextCmd.check(canInit);
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	if (sourceExpression instanceof ExprDyadic && ((ExprDyadic)sourceExpression).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
        		sourceExpression  = (ExprStore)((ExprDyadic)sourceExpression).getExpr1();
        	}
        	
        	if (targetExpression instanceof ExprDyadic && ((ExprDyadic)targetExpression).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
        		targetExpression  = (ExprStore)((ExprDyadic)targetExpression).getExpr1();
        	}
        	
        	int loc1 = sourceExpression.code(loc);
	        if (!(targetExpression instanceof ExprStore)) {
	        	loc1 = targetExpression.code(loc1);
	        } else {
		        loc1 = ((ExprStore) targetExpression).codeRef(loc1);
		        Compiler.getVM().Store(loc1++);
	        }
	        return (nextCmd!=null?nextCmd.code(loc1):loc1);
        }
	}
	public class CmdCond extends Cmd {
		private final Expression expression;
		private final Cmd ifCmd;
		private final Cmd elseCmd;
		private final Cmd nextCmd;

		public CmdCond(Expression expression, Cmd ifCmd, Cmd elseCmd, Cmd nextCmd) {
			this.expression = expression;
			this.ifCmd = ifCmd;
			this.elseCmd = elseCmd;
			this.nextCmd = nextCmd;
		}

		@Override
	    public String toString(String indent) {
			return indent
					+ "<CmdCond>\n"
					+ expression.toString(indent + '\t')
					+ ifCmd.toString(indent + '\t')
					+ elseCmd.toString(indent + '\t')
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
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
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
             
        @Override
        public int getLine() {
        	return expression.getLine();
        }
        
        @Override
        public void check(final boolean canInit) throws ContextError {
        	Object tmp = expression.checkR();
        	Type type;
        	
        	if (tmp instanceof TypedIdentType) {
        		type = ((TypedIdentType)tmp).getType();
        	} else {
        		type = (Type)tmp;
        	}
        	
        	if (type.getAttribute() != TypeAttribute.BOOL) {
	        	throw new ContextError(
	        			"IF condition must be a boolean! ",
	        			expression.getLine());
	        }
	        
	        Scope parentScope = Compiler.getScope();
	        Scope ifScope
	        = new Scope(parentScope.getStoreTable().clone());
	        Scope elseScope
	        = new Scope(parentScope.getStoreTable().clone());
	        Compiler.setScope(ifScope);
	        ifCmd.check(canInit);
	        Compiler.setScope(elseScope);
	        elseCmd.check(canInit);
	        
	        Set<String> keys = parentScope.getStoreTable().getTable().keySet();
	        for (String key : keys) {
	        	Store store = (Store) parentScope.getStoreTable().getStore(key);
		        if (!store.isInitialized()) {
			        Store storeIf
			        = (Store)ifScope.getStoreTable().getStore(store.getIdent());
			        Store storeElse
			        = (Store)elseScope.getStoreTable().getStore(store.getIdent());
			        if (storeIf.isInitialized() != storeElse.isInitialized()) {
				        throw new ContextError(
				        		"Initialization must happen in both branches of an"
				        		+ " IF command! Ident: " + store.getIdent(),
				        		expression.getLine());
			        }
			        
			        if (storeIf.isInitialized()) {
			        	store.initialize();
			        }
		        }
	        }
	        
	        Compiler.setScope(parentScope);
	        if(nextCmd!= null)
	        	nextCmd.check(canInit);
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
	        int loc1 = expression.code(loc);
	        int loc2 = ifCmd.code(loc1 + 1);
	        Compiler.getVM().CondJump(loc1, loc2 + 1);
	        int loc3 = elseCmd.code(loc2 + 1);
	        Compiler.getVM().UncondJump(loc2, loc3);
	        return (nextCmd!=null?nextCmd.code(loc3):loc3);
        }
	}
	
	public class CmdWhile extends Cmd {
		private final Expression expression;
		private final Cmd cmd;
		private final Cmd nextCmd;

		public CmdWhile(Expression expression, Cmd cmd, Cmd nextCmd) {
			this.expression = expression;
			this.cmd = cmd;
			this.nextCmd = nextCmd;
		}

		@Override
	    public String toString(final String indent) {
			return indent
					+ "<CmdWhile>\n"
					+ expression.toString(indent + '\t')
					+ cmd.toString(indent + '\t')
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</CmdWhile>\n";
		}

        public Expression getExpression() {
            return expression;
        }

        public Cmd getCmd() {
            return cmd;
        }
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
        
        @Override
        public int getLine() {
        	return expression.getLine();
        }
        
        @Override
        public void check(final boolean canInit) throws ContextError {
        	Object tmp = expression.checkR();
        	Type type;
        	
        	if (tmp instanceof TypedIdentType) {
        		type = ((TypedIdentType)tmp).getType();
        	} else {
        		type = (Type)tmp;
        	}
	        if (type.getAttribute() != TypeAttribute.BOOL) {
	        	throw new ContextError(
	        			"WHILE condition must be a boolean! ",
	        			expression.getLine());
	        }
	        cmd.check(true);
	        if(nextCmd!=null)
	        	nextCmd.check(canInit);
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	int loc1 = expression.code(loc);
        	int loc2 = cmd.code(loc1 + 1);
        	Compiler.getVM().CondJump(loc1, loc2 + 1);
        	Compiler.getVM().UncondJump(loc2, loc);
        	return (nextCmd!=null?nextCmd.code(loc2 + 1):(loc2+1));
        } 
	}
	
	public class CmdProcCall extends Cmd {
		
		private final RoutineCall routineCall;
		private final GlobalInit globalInit;
		private final Cmd nextCmd;

		public CmdProcCall(RoutineCall routineCall, GlobalInit globalInit, Cmd nextCmd) {
			this.routineCall = routineCall;
			this.globalInit = globalInit;
			this.nextCmd = nextCmd;
		}

		@Override
		public String toString(final String indent) {
			return indent
					+ "<ExprCall>\n"
					+ routineCall.toString(indent + '\t')
					+ (globalInit!=null?globalInit.toString(indent + '\t'):"\t<noGlobalInit/>\n")
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</ExprCall>\n";
		}

        public RoutineCall getRoutineCall() {
            return routineCall;
        }

        public GlobalInit getGlobalInit() {
            return globalInit;
        }
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}

		@Override
		public int getLine() {
			return routineCall.getIdent().getLine();
		}

		public void check(boolean canInit) throws ContextError {
			Ident ident = routineCall.getIdent();
			RoutineTypes type = Compiler.getRoutineTable().getType(
					ident.getName());
			if (type == null) {
				throw new ContextError(
						"Ident " + ident.getName() + " not declared",
						ident.getLine());
			} else if (type != RoutineTypes.PROCEDURE){
				throw new ContextError(
						"Function call "
						+ ident.getName()
						+ " found in left part of an assignement",
						ident.getLine());
			}
			
			Routine routine = Compiler.getRoutineTable().getRoutine(
					ident.getName());
			
			List<ch.fhnw.cpib.compiler.context.Parameter> paramList = new ArrayList<ch.fhnw.cpib.compiler.context.Parameter>(routine.getParamList());
			Set<String> aliasList = new HashSet<String>();
			routineCall.getExprList().check(paramList, aliasList, canInit);
			
			Set<String> globInits;
			if (globalInit != null) 
				 globInits = globalInit.check(new HashSet<String>());
			else
				globInits = null;
			
			for (GlobImp globImp
					: routine.getGlobImpList()) {
				switch (globImp.getFlowMode().getAttribute()) {
					case IN:
					case INOUT:
						if (!((Store)Compiler.getScope().getStoreTable().getStore(
								globImp.getIdent())).isInitialized()) {
							throw new ContextError(
									"Global import of function not initialized!"
									+ " Ident: " + globImp.getIdent(),
									ident.getLine());
						}
						break;
					case OUT:
						if (globInits.contains(globImp.getIdent())) {
							((Store)Compiler.getScope().getStoreTable().getStore(
									globImp.getIdent())).initialize();
							globInits.remove(globImp.getIdent());
						}
						break;
					default:
						throw new RuntimeException();
				}
				
				if (aliasList.contains(globImp.getIdent())) {
					throw new ContextError(
							"Global import is already used as a parameter! Ident: "
									+ globImp.getIdent(),
									ident.getLine());
				}
			}
			
			if (globInits != null && globInits.size() > 0) {
				throw new ContextError(
						"Global init is not importet! Ident: "
								+ globInits.iterator().next(),
								ident.getLine());
			}
			
			if(nextCmd!=null)
				nextCmd.check(canInit);
		}

		@Override
		public int code(final int loc) throws CodeTooSmallError {
			int loc1 = loc;
			loc1 = routineCall.getExprList().code(loc1);
			Compiler.getRoutineTable().getRoutine(
					routineCall.getIdent().getName()).addCall(loc1++);
			return (nextCmd!=null?nextCmd.code(loc1):loc1);
		}
	}
	
	public class CmdInput extends Cmd {
		private Expression expr;
		private final Cmd nextCmd;
		private Type type;

		public CmdInput(Expression expr, Cmd nextCmd) {
			this.expr = expr;
			this.nextCmd = nextCmd;
		}

		@Override
	    public String toString(String indent) {
			return indent
					+ "<CmdInput>\n"
					+ expr.toString(indent + '\t')
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</CmdInput>\n";
		}

        public Expression getExpr() {
            return expr;
        }
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
        
        @Override
        public int getLine() {
        	return expr.getLine();
        }
        
        @Override
        public void check(final boolean canInit) throws ContextError {
        	
        	Object tmp = expr.checkL(canInit);
        	
        	if (tmp instanceof TypedIdentType) {
        		type = ((TypedIdentType)tmp).getType();
        	} else {
        		type = (Type)tmp;
        	}
        	
        	if (!(expr instanceof ExprStore)
        			&& !((expr instanceof ExprDyadic) 
					&& ((ExprDyadic)expr).getOperator().getOperatorAttribute() == OperatorAttribute.DOT 
					&& (((ExprDyadic)expr).getExpr2() instanceof ExprStore))) {
        		throw new ContextError(
        				"Input needs to be assigned to a store!",
        				expr.getLine());
        	}
        	if(nextCmd != null)
        		nextCmd.check(canInit);
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	int loc1;
        	if (expr instanceof ExprDyadic && ((ExprDyadic)expr).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
        		ExprStore expr1 = (ExprStore)((ExprDyadic)expr).getExpr1();
        		expr = expr1;
        		loc1 = ((ExprStore)expr).codeRef(loc);
        	} else {
        		loc1 = ((ExprStore) expr).codeRef(loc);
        	}

        	if (type.getAttribute() == TypeAttribute.BOOL) {
        		Compiler.getVM().BoolInput(
        				loc1++, ((ExprStore) expr).getIdent().getName());
        	} else {
        		Compiler.getVM().IntInput(
        				loc1++, ((ExprStore) expr).getIdent().getName());
        	}
        	return (nextCmd!=null?nextCmd.code(loc1):loc1);
        }
	}
	
	public class CmdOutput extends Cmd {
		private Expression expr;
		private final Cmd nextCmd;
		private Type type;

		public CmdOutput(Expression expr, Cmd nextCmd) {
			this.expr = expr;
			this.nextCmd = nextCmd;
		}

		@Override
	    public String toString(String indent) {
			return indent
					+ "<CmdOutput>\n"
					+ expr.toString(indent + '\t')
					+ (nextCmd != null?nextCmd.toString(indent + '\t'):indent+"\t<noNextElement/>\n")
					+ indent
					+ "</CmdOutput>\n";
		}

        public Expression getExpr() {
            return expr;
        }
        
        @Override
		public Cmd getNextCmd() {
			return nextCmd;
		}
        
        @Override
        public int getLine() {
        	return expr.getLine();
        }
        
        @Override
        public void check(final boolean canInit) throws ContextError {
        	Object tmp = expr.checkR();
        	
        	if (tmp instanceof TypedIdentType) {
        		type = ((TypedIdentType)tmp).getType();
        	} else {
        		type = (Type)tmp;
        	}
        	
        	if (!(expr instanceof ExprStore)
        			&& !((expr instanceof ExprDyadic) 
        					&& ((ExprDyadic)expr).getOperator().getOperatorAttribute() == OperatorAttribute.DOT 
        					&& (((ExprDyadic)expr).getExpr2() instanceof ExprStore))) {
        		throw new ContextError(
        				"Output needs to be a store!",
        				expr.getLine());
        	}
        	
        	if(nextCmd != null)
        		nextCmd.check(canInit);
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	int loc1;
        	if (expr instanceof ExprDyadic && ((ExprDyadic)expr).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
        		ExprStore expr1 = (ExprStore)((ExprDyadic)expr).getExpr1();
        		ExprStore expr2 = (ExprStore)((ExprDyadic)expr).getExpr2();
        		Store store = (Store)Compiler.getGlobalStoreTable().getStore(expr2.getIdent().getName() + "." + expr1.getIdent().getName());
        		expr1.setIdent(store.getType().getIdent());
        		expr = expr1;
        		loc1 = expr.code(loc);
        	} else {
        		loc1 = ((ExprStore) expr).code(loc);
        	}
        	
        	
        	if (type.getAttribute() == TypeAttribute.BOOL) {
        		Compiler.getVM().BoolOutput(
        				loc1++, ((ExprStore) expr).getIdent().getName());
        	} else {
        		Compiler.getVM().IntOutput(
        				loc1++, ((ExprStore) expr).getIdent().getName());
        	}
        	return (nextCmd!=null?nextCmd.code(loc1):loc1);
        }
	}
	
	public abstract class TypedIdent<T> {
		
		public abstract String toString(String indent);
		
		public abstract Ident getIdent();
		
		public abstract T getType();
		
	}
	public class TypedIdentIdent extends TypedIdent<Ident> {
		
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
		
		public Ident getType() {
			return ident;
		}
	}
	public class TypedIdentType extends TypedIdent<Type> {
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
	
	public abstract class Expression<T> {
		public abstract String toString(String indent);
		
		abstract T checkR() throws ContextError;
		abstract T checkL(boolean canInit) throws ContextError;
		abstract int code(int loc) throws CodeTooSmallError;
		abstract int getLine();
	}
	
	public class ExprLiteral extends Expression<Type> {
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
        
        @Override
        public int getLine() {
        	return literal.getLine();
        }
        
        @Override
        public Type checkR() throws ContextError {
        	return literal.getType();
        }
        
        @Override
        public Type checkL(final boolean canInit) throws ContextError {
        	throw new ContextError(
        			"Found literal "
        					+ literal.getLiteral()
        					+ "in the left part of an assignement",
        					literal.getLine());
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	Compiler.getVM().IntLoad(loc, literal.getLiteral());
        	return loc + 1;
        }
	}
	
	public class ExprStore extends Expression<TypedIdent> {
		private Ident ident;
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
        
        public void setIdent(Ident ident) {
        	this.ident = ident;
        }

        public boolean isInit() {
            return isInit;
        }
        
        @Override
        public int getLine() {
        	return ident.getLine();
        }
        
        public Store getStore() {
        	return (Store)Compiler.getGlobalStoreTable().getStore(ident.getName());
        }
        
        @Override
        public TypedIdentType checkR() throws ContextError {
	        TypedIdent type = Compiler.getScope().getType(
	        		ident.getName());
	        
	        if (type == null) {
	        	throw new ContextError(
	        			"Ident " + ident.getName() + " not declared",
	        			ident.getLine());
	        }
	        
	        if (type instanceof TypedIdentIdent) {
	        	throw new ContextError(
	        			"Records cannot be used here. "
	        					+ " Record " + ident.getName(),
	        					ident.getLine());
	        }
	        
	        if (isInit) {
	        	throw new ContextError(
	        			"Initialization of "
	        					+ ident.getName()
	        					+ " found in right part of an assignement",
	        					ident.getLine());
	        	}
	        
	        	if (!((Store)Compiler.getScope().getStoreTable().getStore(
	        			ident.getName())).isInitialized()) {
	        		throw new ContextError(
	        				"Store "
	        						+ ident.getName()
	        						+ " is not initialized",
	        						ident.getLine());
	        }
	        	
	        return (TypedIdentType)type;
        }
        
        @Override
        public TypedIdent checkL(final boolean canInit) throws ContextError {
        	TypedIdent type = Compiler.getScope().getType(ident.getName());
        	
        	if (type == null) {
        		throw new ContextError(
        				"Ident " + ident.getName() + " not declared",
        				ident.getLine());
        	}
        	
        	Store store = (Store) Compiler.getScope().getStoreTable().getStore(
	        			ident.getName());
        	
	        if (isInit) {
	        	
	        	if(store.isRecord()) {
	        		throw new ContextError(
	        				"Records cannot yet be directly initialized",
	        						ident.getLine());
	        	}
	        	
	        	if (canInit) {
	        		throw new ContextError(
	        				"Store can not be initialized here "
	        						+ "(loop or inout parameter)!",
	        						ident.getLine());
	        	}
	        	
	        	if (store.isInitialized()) {
	        		throw new ContextError(
	        				"Store "
	        						+ ident.getName()
	        						+ " is already initialized",
	        						ident.getLine());
	        	}
	        	
	        	store.initialize();
	        	
	        } else if (!store.isInitialized() && !store.isRecord()) {
	        	throw new ContextError(
	        			"Store "
	        					+ ident.getName()
	        					+ " is not initialized",
	        					ident.getLine());
	        } else if (!store.isWriteable()) {
	        	throw new ContextError(
	        				"Store "
	        						+ ident.getName()
	        						+ " is not writeable",
	        						ident.getLine());
	        }
	        
	        return type;
        }
        
        @Override
        public int code(final int loc) throws CodeTooSmallError {
        	Store store = (Store)Compiler.getScope().getStoreTable().getStore(
        			ident.getName());
        	return((store != null && !store.isRecord())?store.codeLoad(loc):loc);
        }
        
        public int codeRef(final int loc) throws CodeTooSmallError {
        	Store store = (Store)Compiler.getScope().getStoreTable().getStore(
        			ident.getName());
        	return((store != null && !store.isRecord())?store.codeRef(loc):loc);
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
					+ indent
					+ "</ExprCall>\n";
		}

        public RoutineCall getRoutineCall() {
            return routineCall;
        }

		@Override
		Object checkR() throws ContextError {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		Object checkL(boolean canInit) throws ContextError {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		int code(int loc) throws CodeTooSmallError {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		int getLine() {
			// TODO Auto-generated method stub
			return 0;
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

		@Override
		Object checkR() throws ContextError {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		Object checkL(boolean canInit) throws ContextError {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		int code(int loc) throws CodeTooSmallError {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		int getLine() {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public final class ExprDyadic extends Expression<Type> {
		private final Operator operator;
		private Expression expr1;
		private Expression expr2;
		private ContextError error;

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
        
        @Override
        public int getLine() {
        	return operator.getLine();
        }
        
        @Override
        public Type checkR() throws ContextError {
	        if (error != null) {
	        	throw error;
	        }
	        
	        /**
	         * Check if dyadic expression is of operator DOT
	         */
	        if (operator.getOperatorAttribute() == OperatorAttribute.DOT) {
	        	ExprStore es1 = ((ExprStore)expr1);
	        	ExprStore es2 = ((ExprStore)expr2);
	        	String storeName = es2.getIdent().getName() + "." + es1.getIdent().getName();
	        	Ident i = new Ident(storeName);
	        	i.setLine(es1.getIdent().getLine());
	        	ExprStore esRecord = new ExprStore(i, es1.isInit());
	        	Type type = esRecord.checkR().getType();
	        	return type;
	        } else {
	        	/**
	        	 * If first expression in EXPRESSION OPERATOR EXPRESSION is a record:
	        	 * replace expr1 with the field of the record
	        	 */
	        	if (expr1 instanceof ExprDyadic && ((ExprDyadic)expr1).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
	        		ExprStore es1 = (ExprStore)((ExprDyadic)expr1).getExpr1();
	        		ExprStore es2 = (ExprStore)((ExprDyadic)expr1).getExpr2();
	        		Store store = (Store)Compiler.getGlobalStoreTable().getStore(es2.getIdent().getName() + "." + es1.getIdent().getName());
	        		int lineNumber = es1.getIdent().getLine();
	        		es1.setIdent(store.getType().getIdent());
	        		es1.getIdent().setLine(lineNumber);
	        		expr1 = es1;
	        	}
	        	
	        	/**
	        	 * If second expression in EXPRESSION OPERATOR EXPRESSION is a record:
	        	 * replace expr2 with the field of the record
	        	 */
	        	if (expr2 instanceof ExprDyadic && ((ExprDyadic)expr2).getOperator().getOperatorAttribute() == OperatorAttribute.DOT) {
	        		ExprStore es1 = (ExprStore)((ExprDyadic)expr2).getExpr1();
	        		ExprStore es2 = (ExprStore)((ExprDyadic)expr2).getExpr2();
	        		Store store = (Store)Compiler.getGlobalStoreTable().getStore(es2.getIdent().getName() + "." + es1.getIdent().getName());
	        		int lineNumber = es1.getIdent().getLine();
	        		es1.setIdent(store.getType().getIdent());
	        		es1.getIdent().setLine(lineNumber);
	        		expr2 = es1;
	        	}
	        	
	        	Type type1;
	        	Object tmp = expr1.checkR();
	        	if (tmp instanceof TypedIdentType) {
	        		type1 = ((TypedIdentType)tmp).getType();
	        	} else {
	        		type1 = (Type)tmp;
	        	}
	        	
	        	Type type2;
	        	tmp = expr2.checkR();
	        	if (tmp instanceof TypedIdentType) {
	        		type2 = ((TypedIdentType)tmp).getType();
	        	} else {
	        		type2 = (Type)tmp;
	        	}
		        
		        switch(operator.getOperatorAttribute()) {
			        case PLUS:
			        case MINUS:
			        case TIMES:
			        case DIV:
			        case MOD:
			        	if (type1.getAttribute() == TypeAttribute.INT32
			        		&& type2.getAttribute() == TypeAttribute.INT32) {
			        		return new Type(TypeAttribute.INT32);
			        	} else {
			        		throw new ContextError(
			        				"Type error in Operator "
			        						+ operator.getOperatorAttribute(),
			        						operator.getLine());
			        	}
			        case EQ:
			        case NE:
			        	if (type1.getAttribute() == TypeAttribute.BOOL
			        		&& type2.getAttribute() == TypeAttribute.BOOL) {
			        		return new Type(TypeAttribute.BOOL);
			        	}
			        case GT:
			        case LT:
			        case GE:
			        case LE:
			        	if (type1.getAttribute() == TypeAttribute.INT32
			        		&& type2.getAttribute() == TypeAttribute.INT32) {
			        		return new Type(TypeAttribute.BOOL);
			        	} else {
			        		throw new ContextError(
			        				"Type error in Operator "
			        						+ operator.getOperatorAttribute(),
			        						operator.getLine());
			        	}
			        case CAND:
			        case COR:
			        	if (type1.getAttribute() == TypeAttribute.BOOL
			        		&& type2.getAttribute() == TypeAttribute.BOOL) {
			        		return new Type(TypeAttribute.BOOL);
			        	} else {
			        		throw new ContextError(
			        				"Type error in Operator "
			        						+ operator.getOperatorAttribute(),
			        						operator.getLine());
			        	}
			        default:
			        	throw new RuntimeException();
		        }
	        }
        }
        
        @Override
        public Type checkL(final boolean canInit) throws ContextError {
        	if (error != null) {
	        	throw error;
	        }
        	
        	switch(operator.getOperatorAttribute()) {
	        	case DOT:
	        		TypedIdent type = (TypedIdent) expr2.checkL(canInit);
	        		if (!Compiler.getGlobalStoreTable().containsIdent(type.getIdent().getName())) {
	        			throw new ContextError("Ident " + type.getIdent().getName()
	        					+ "not declared", type.getIdent().getLine());
	        		} 
	        		Store store = (Store)Compiler.getGlobalStoreTable().getStore(type.getIdent().getName() + "." + ((ExprStore)expr1).getIdent().getName());
	        		int lineNumber = ((ExprStore)expr1).getIdent().getLine();
	        		((ExprStore)expr1).setIdent(store.getType().getIdent());
	        		((ExprStore)expr1).getIdent().setLine(lineNumber);
	        		expr1.checkL(canInit);
	        		return ((TypedIdentType)store.getType()).getType();
	        	default:
	        		throw new ContextError(
	        				"Found operator "
        					+ operator.getOperatorAttribute()
        					+ "in the left part of an assignement",
        					operator.getLine());
	        }
	    }
        
	    @Override
        public int code(final int loc) throws CodeTooSmallError {
	    	int loc1 = expr1.code(loc);
	    	
	    	if (operator.getOperatorAttribute() != OperatorAttribute.CAND
	    			&& operator.getOperatorAttribute() != OperatorAttribute.COR) {
	    		loc1 = expr2.code(loc1);
	    		
		        switch (operator.getOperatorAttribute()) {
			        case DOT:
			        	break;
			        case PLUS:
			        	Compiler.getVM().IntAdd(loc1);
			        	break;
			        case MINUS:
				        Compiler.getVM().IntSub(loc1);
				        break;
			        case TIMES:
				        Compiler.getVM().IntMult(loc1);
				        break;
			        case DIV:
				        Compiler.getVM().IntDiv(loc1);
				        break;
			        case MOD:
				        Compiler.getVM().IntMod(loc1);
				        break;
			        case EQ:
				        Compiler.getVM().IntEQ(loc1);
				        break;
			        case NE:
				        Compiler.getVM().IntNE(loc1);
				        break;
			        case GT:
				        Compiler.getVM().IntGT(loc1);
				        break;
			        case LT:
				        Compiler.getVM().IntLT(loc1);
				        break;
			        case GE:
				        Compiler.getVM().IntGE(loc1);
				        break;
			        case LE:
				        Compiler.getVM().IntLE(loc1);
				        break;
			        default:
			        	throw new RuntimeException();
		        }
		        
		        return loc1 + 1;
	    	} else if (operator.getOperatorAttribute() == OperatorAttribute.CAND) {
	    		int loc2 = expr2.code(loc1 + 1);
		        Compiler.getVM().UncondJump(loc2++, loc2 + 1);
		        Compiler.getVM().CondJump(loc1, loc2);
		        Compiler.getVM().IntLoad(loc2++, 0);
		        return loc2;
	    	} else {
		        int loc2 = expr2.code(loc1 + 2);
		        Compiler.getVM().UncondJump(loc2++, loc2 + 1);
		        Compiler.getVM().CondJump(loc1, loc1 + 2);
		        Compiler.getVM().UncondJump(loc1 + 1, loc2);
		        Compiler.getVM().IntLoad(loc2++, 1);
		        return loc2;
	        }
        }
	    
        public void setError(final ContextError contextError) {
        	error = contextError;
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
		private ch.fhnw.cpib.compiler.context.Parameter param;

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
        
        public void check (
        		final List<ch.fhnw.cpib.compiler.context.Parameter> paramList,
        		final Set<String> aliasList,
        		final boolean canInit)
        		throws ContextError {
	        if (paramList.size() <= 0) {
		        throw new ContextError("Routione takes less parameters than provided. ",
		        expression.getLine());
	        }
	        param = paramList.get(0);
	        paramList.remove(0);
	        Type type;
	        switch(param.getFlowMode().getAttribute()) {
	        	case IN:
	        		if (param.getMechMode().getAttribute() == ModeAttribute.COPY) {
	        			Object tmp = expression.checkR();
	        			if (tmp instanceof TypedIdentType)
	        				type = ((TypedIdentType) tmp).getType();
	        			else
	        				type = (Type)tmp;
	        		} else {
	        			Object tmp = expression.checkL(false);
	        			if (tmp instanceof TypedIdentType)
	        				type = ((TypedIdentType) tmp).getType();
	        			else
	        				type = (Type)tmp;
	        			if (!(expression instanceof ExprStore)) {
	        				throw new ContextError(
	        						"Only stores can be used as IN REF parameter!",
	        						expression.getLine());
	        			}
	        			if (aliasList.contains(
	        					((ExprStore) expression).getStore().getIdent())) {
	        				throw new ContextError(
	        						"Store is already used a parameter!",
	        						expression.getLine());
	        			}
	        			aliasList.add(((ExprStore) expression).getStore().getIdent());
	        		}
	        		break;
	        	case INOUT:
	        		type = checkINOUTstore(false, aliasList);
	        		break;
	        	case OUT:
	        		type = checkINOUTstore(canInit, aliasList);
	        		break;
	        	default:
	        		throw new RuntimeException();
	        }
	        
	        if (type.getAttribute() != ((TypedIdentType)param.getType()).getType().getAttribute()) {
	        	throw new ContextError(
	        		"Wrong paramter type!",
	        		expression.getLine());
	        }
	        
	        if (expressionList != null)
	        	expressionList.check(paramList, aliasList, canInit);
        }
        
        private Type checkINOUTstore(
        		final boolean canInit,
        		final Set<String> aliasList) throws ContextError {
        		Type type;
        		Object tmp = expression.checkL(canInit);
        		if(tmp instanceof TypedIdentType)
        			type = ((TypedIdentType) tmp).getType();
        		else
        			type = (Type)tmp;
        		
        		if (!(expression instanceof ExprStore)) {
        			throw new ContextError(
        					"Only stores can be used as INOUT/OUT parameter!",
        					expression.getLine());
        		}
        		
        		if (aliasList.contains(
        				((ExprStore) expression).getIdent())) {
        			throw new ContextError(
        					"Store is already used a parameter!",
        					expression.getLine());
        		}
        		
        		if (!((ExprStore) expression).getStore().isWriteable()) {
        			throw new ContextError(
        					"INOUT/OUT parameter is not writeable!",
        					expression.getLine());
        		}
        		
        		aliasList.add(((ExprStore) expression).getStore().getIdent());
        		
        		return type;
        }
        
        public int code(final int loc) throws CodeTooSmallError {
        	int loc1;
        	if (param.getFlowMode().getAttribute() == ModeAttribute.IN
        			&& param.getMechMode().getAttribute() == ModeAttribute.COPY) {
        		loc1 = expression.code(loc);
        	} else {
        		loc1 = ((ExprStore) expression).codeRef(loc);
        	}
        	
        	return (expressionList!=null?expressionList.code(loc1):loc1);
        }
	}
	
	public final class GlobalInit {
		private final Ident ident;
		private final GlobalInit globalInit;

		public GlobalInit(Ident ident, GlobalInit globalInit) {
			this.ident = ident;
			this.globalInit = globalInit;
		}

		public String toString(String indent) {
			return indent
					+ "<GlobInit>\n"
					+ ident.toString(indent + '\t')
					+ (globalInit != null?globalInit.toString(indent + '\t'):"<noNextElement/>\n")
					+ indent
					+ "</GlobInit>\n";
		}
		
		public int getLine() {
			return ident.getLine();
		}

        public Ident getIdent() {
            return ident;
        }

        public GlobalInit getGlobalInit() {
            return globalInit;
        }
        
        public Set<String> check(final Set<String> initList) throws ContextError {
        	if (initList.contains(ident.getName())) {
        		throw new ContextError(
        				"Global init already declared!"
        						+ " Ident: " + ident.getName(),
        						ident.getLine());
        	} else {
        		initList.add(ident.getName());
        	}
        	return globalInit.check(initList);
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
		
		public int getLine() {
			return ident.getLine();
		}
		
		public void check(final Routine routine) throws ContextError {
			Store globalStore = (Store)Compiler.getGlobalStoreTable().getStore(
					ident.getName());
			
			if (globalStore == null) {
				throw new ContextError(
						"Global import is not declared! Ident: "
								+ ident.getName(), ident.getLine());
			}
			
			if (globalStore.isConst() && changeMode.getAttribute() != ModeAttribute.CONST) {
				throw new ContextError(
						"Cannot import global constant as variable! Ident: "
								+ ident.getName(), ident.getLine());
			}
			
			Store localStore = new Store(
					globalStore.getIdent(),
					globalStore.getType(),
					changeMode.getAttribute() == ModeAttribute.CONST);
				localStore.setAddress(globalStore.getAddress());
				localStore.setReference(false);
				localStore.setRelative(false);
				
				if (!Compiler.getScope().getStoreTable().addStore(localStore.getType().getIdent().getName(), localStore)) {
					throw new ContextError(
							"Global ident already used! Ident: "
									+ ident.getName(), ident.getLine());
			}
			
			switch (flowMode.getAttribute()) {
				case IN:
					localStore.initialize();
					break;
				case INOUT:
					if (routine.getRoutineType() != RoutineTypes.PROCEDURE) {
						throw new ContextError(
								"FlowMode INOUT is not allowed for functions! "
										+ "Ident: " + ident.getName(), ident.getLine());
					}
					
					if (changeMode.getAttribute() == ModeAttribute.CONST) {
						throw new ContextError(
								"ChangeMode CONST is not allowed for FlowMode INOUT! "
										+ "Ident: " + ident.getName(), ident.getLine());
					}
					
					localStore.initialize();
					break;
				case OUT:
					if (routine.getRoutineType() != RoutineTypes.PROCEDURE) {
						throw new ContextError(
								"FlowMode OUT is not allowed for functions! Ident: "
										+ ident.getName(), ident.getLine());
					}
					break;
				default:
					throw new RuntimeException();
			}
			
			routine.addGlobImp(new GlobImp(
					getFlowMode(),
					getChangeMode(),
					ident.getName()));
				nextGlobalImport.check(routine);
		}
		
		public void checkInit() throws ContextError {
			if (flowMode.getAttribute() == ModeAttribute.OUT) {
				if (!((Store)Compiler.getScope().getStoreTable().getStore(
						ident.getName())).isInitialized()) {
					throw new ContextError(
							"OUT global import is never initialized! Ident: "
									+ ident.getName(),
									ident.getLine());
				}
			}
			nextGlobalImport.checkInit();
		}
	}
}
