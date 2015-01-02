package ch.fhnw.cpib.compiler.parser;

import ch.fhnw.cpib.compiler.scanner.enums.ModeAttribute;
import ch.fhnw.cpib.compiler.scanner.token.*;
import ch.fhnw.cpib.compiler.scanner.token.Mode.*;
import ch.fhnw.cpib.compiler.scanner.token.Operator.*;

public interface ConcTree {

	public class RecordDeclaration extends Declaration {
		private final Ident ident;
		private final RecordFieldList recordFieldList;
		public RecordDeclaration(Ident ident, RecordFieldList recordStorageList){
			this.ident = ident;
			this.recordFieldList = recordStorageList;
		}
		public String toString(String indent){
			return indent
					+ "<RecordDeclaration>\n"
					+ ident.toString(indent + '\t')
					+ recordFieldList.toString(indent + '\t')
					+ indent
					+ "</RecordDeclaration>\n";
		}
		public AbsTree.DeclarationRecord toAbstract(RepeatingOptionalDeclarations repeatingOptionalDeclarations) {
			return new AbsTree.DeclarationRecord(ident, recordFieldList.toAbstract(), repeatingOptionalDeclarations.toAbstract());
		}

	}
	public class RecordFieldList extends Declaration {
		private final RecordFieldDeclaration recordFieldDeclaration;
		private final RepeatingOptionalRecordFieldDeclarations repeatingOptionalrecordFieldDeclarations;
		
		public RecordFieldList(RecordFieldDeclaration recordFieldDeclaration, RepeatingOptionalRecordFieldDeclarations repeatingOptionalrecordFieldDeclarations){
			this.recordFieldDeclaration = recordFieldDeclaration;
			this.repeatingOptionalrecordFieldDeclarations = repeatingOptionalrecordFieldDeclarations;
		}
		public String toString(String indent){
			return indent
					+ "<RecordFieldList>\n"
					+ recordFieldDeclaration.toString(indent + '\t')
					+ repeatingOptionalrecordFieldDeclarations.toString(indent + '\t')
					+ "</RecordFieldList>\n";
		}
		public AbsTree.RecordField toAbstract(){
			return new AbsTree.RecordField(recordFieldDeclaration.toAbstract(null), repeatingOptionalrecordFieldDeclarations.toAbstract());
		}
		
	}
	
	public class RecordFieldDeclaration {
		private final OptionalChangeMode optionalChangeMode;
		private final TypedIdent typedIdent;

		public RecordFieldDeclaration(OptionalChangeMode optionalChangeMode, TypedIdent typedIdent) {
			this.optionalChangeMode = optionalChangeMode;
			this.typedIdent = typedIdent;
		}

		public String toString(String indent) {
			return indent
					+ "<RecordFieldDeclaration>\n"
					+ optionalChangeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ indent
					+ "</RecordFieldDeclaration>\n";
		}
		
		public AbsTree.DeclarationRecordField toAbstract(RepeatingOptionalRecordFieldDeclarations repeatingOptionalRecordFieldDeclarations) {
			return new AbsTree.DeclarationRecordField(optionalChangeMode.toAbstract(), typedIdent.toAbstract(), (repeatingOptionalRecordFieldDeclarations!=null?repeatingOptionalRecordFieldDeclarations.toAbstract():null));
		}
	}
	
	public class RepeatingOptionalRecordFieldDeclarations {
		private final RecordFieldDeclaration recordFieldDeclaration;
		private final RepeatingOptionalRecordFieldDeclarations repeatingOptionalRecordFieldDeclarations;
		
		public RepeatingOptionalRecordFieldDeclarations(RecordFieldDeclaration recordFieldDeclaration, RepeatingOptionalRecordFieldDeclarations repeatingOptionalRecordFieldDeclaration){
			this.recordFieldDeclaration = recordFieldDeclaration;
			this.repeatingOptionalRecordFieldDeclarations = repeatingOptionalRecordFieldDeclaration;
			
		}
		public String toString(String indent){
			return indent
					+ "<RepeatingOptionalRecordFieldDeclarations>\n"
					+ recordFieldDeclaration.toString(indent + '\t')
					+ repeatingOptionalRecordFieldDeclarations.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalRecordFieldDeclarations>\n";
		}
		public AbsTree.DeclarationRecordField toAbstract(){
			return recordFieldDeclaration.toAbstract(repeatingOptionalRecordFieldDeclarations);
		}
	}
	
	public class RepeatingOptionalRecordFieldDeclarationsEpsilon extends RepeatingOptionalRecordFieldDeclarations{
		public RepeatingOptionalRecordFieldDeclarationsEpsilon(){
			super(null,null);
		}
		public String toString(String indent){
			return indent + "<RepeatingOptionalRecordFieldDeclarationsEpsilon />\n";
		}
		public AbsTree.DeclarationRecordField toAbstract(){ return null; }
	}
	
	public class RepeatingOptionalStorageDeclarations extends RepeatingOptionalDeclarations{
		private final StorageDeclaration storageDeclaration;
		private final RepeatingOptionalStorageDeclarations repeatingOptionalStorageDeclarations;
		public RepeatingOptionalStorageDeclarations(StorageDeclaration storageDeclaration, RepeatingOptionalStorageDeclarations repeatingOptionalStorageDeclarations){
			super(storageDeclaration, repeatingOptionalStorageDeclarations);
			this.storageDeclaration = storageDeclaration;
			this.repeatingOptionalStorageDeclarations = repeatingOptionalStorageDeclarations;
			
		}
		public String toString(String indent){
			return indent
					+ "<RepeatingOptionalStorageDeclarations>\n"
					+ storageDeclaration.toString(indent + '\t')
					+ repeatingOptionalStorageDeclarations.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalStorageDeclarations>\n";
		}
		public AbsTree.DeclarationStore toAbstract(){
			return storageDeclaration.toAbstract(repeatingOptionalStorageDeclarations);
		}
	}
	
	public class RepeatingOptionalStorageDeclarationsEpsilon extends RepeatingOptionalStorageDeclarations{
		public RepeatingOptionalStorageDeclarationsEpsilon(){
			super(null,null);
		}
		public String toString(String indent){
			return indent + "<RepeatingOptionalStorageDeclarationsEpsilon />\n";
		}
		public AbsTree.DeclarationStore toAbstract(){ return null; }
	}
	
	
	public class RepeatingOptionalDeclarationsEpsilon extends
			RepeatingOptionalDeclarations {
		public RepeatingOptionalDeclarationsEpsilon() {
			super(null,null);
		}

		public String toString(String indent) {
			return indent + "<RepeatingOptionalDeclarationsEpsilon />\n";
		}
		public AbsTree.Declaration toAbstract(){ return null; }

	}
	
	public class RepeatingOptionalDeclarations{
		private final Declaration declaration;
		private final RepeatingOptionalDeclarations repeatingOptionalDeclarations;

		public RepeatingOptionalDeclarations(Declaration declaration, RepeatingOptionalDeclarations repeatingOptionalDeclarations) {
			this.declaration = declaration;
			this.repeatingOptionalDeclarations = repeatingOptionalDeclarations;
		}
		
		public String toString(String indent){
			return indent
					+ "<RepeatingOptionalDeclarations>\n"
					+ declaration.toString(indent + '\t')
					+ repeatingOptionalDeclarations.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalDeclarations>\n";
		}
		
		public AbsTree.Declaration toAbstract() {
			return declaration.toAbstract(repeatingOptionalDeclarations);
		}
		
	}
	public class Declarations {
		private final Declaration declaration;
		private final RepeatingOptionalDeclarations repeatingOptionalDeclarations;
		
		public Declarations(Declaration declaration, RepeatingOptionalDeclarations repeatingOptionalDeclarations){
			this.declaration = declaration;
			this.repeatingOptionalDeclarations = repeatingOptionalDeclarations;
		}
		
		public String toString(String indent){
			return indent
					+ "<Declarations>\n"
					+ declaration.toString(indent + '\t')
					+ repeatingOptionalDeclarations.toString(indent + '\t')
					+ indent
					+ "</Declarations>\n";
		}
		public AbsTree.Declaration toAbstract() {
			return declaration.toAbstract(repeatingOptionalDeclarations);
		}
		
		
	}
	public class OptionalGlobalDeclarationsEpsilon extends
			OptionalGlobalDeclarations {
		public OptionalGlobalDeclarationsEpsilon(){
			super(null);
		}
		public String toString(String indent){
			return indent + "<OptionalGlobalDeclarationsEpsilon/>\n";
		}
		public AbsTree.Declaration toAbstract(){ return null; }

	}
	public class OptionalGlobalDeclarations {
		private final Declarations declarations;
		
		public OptionalGlobalDeclarations(Declarations declarations) {
			this.declarations = declarations;
		}
		public String toString (String indent){
			return indent
					+ "<OptionalGlobalDeclarations>\n"
					+ declarations.toString(indent + '\t')
					+ indent
					+ "</OptionalGlobalDeclarations>\n";
		}
		public AbsTree.Declaration toAbstract() {
			return declarations.toAbstract();
		}

	}
	public class Program {
		private final Ident ident;
		private final ProgramParameterList programParameterList;
		private final OptionalGlobalDeclarations optionalGlobalDeclarations;
		private final BlockCmd blockCmd;
		
		public Program(Ident ident, ProgramParameterList programParameterList, OptionalGlobalDeclarations optionalGlobalDeclarations, BlockCmd blockCmd) {
			this.ident = ident;
			this.programParameterList = programParameterList;
			this.optionalGlobalDeclarations = optionalGlobalDeclarations;
			this.blockCmd = blockCmd;
			
		}
		
		public String toString() { return toString(""); }

		public String toString(String indent) {
			return indent
					+ "<Program>\n"
					+ ident.toString(indent + '\t') 
					+ programParameterList.toString(indent + '\t')
					+ optionalGlobalDeclarations.toString(indent + '\t')
					+ blockCmd.toString(indent + '\t')
					+ indent
					+ "</Program>\n";
		}
		
		public AbsTree.Program toAbstract() {
			return new AbsTree.Program(ident, programParameterList.toAbstract(), optionalGlobalDeclarations.toAbstract(), blockCmd.toAbstract());
		}
	}
	

	public abstract class Declaration {
		public abstract String toString(String indent);

		public AbsTree.Declaration toAbstract(RepeatingOptionalDeclarations repeatingOptionalDeclaration) { return null; }
	}
	
	//gemacht 18.12.
	public class StorageDeclaration extends Declaration {
		private final OptionalChangeMode optionalChangeMode;
		private final TypedIdent typedIdent;

		public StorageDeclaration(OptionalChangeMode optionalChangeMode, TypedIdent typedIdent) {
			this.optionalChangeMode = optionalChangeMode;
			this.typedIdent = typedIdent;
		}

		public String toString(String indent) {
			return indent
					+ "<StorageDeclaration>\n"
					+ optionalChangeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ indent
					+ "</StorageDeclaration>\n";
		}
		
		public AbsTree.DeclarationStore toAbstract(RepeatingOptionalDeclarations repeatingOptionalDeclarations) {
			return new AbsTree.DeclarationStore(optionalChangeMode.toAbstract(), typedIdent.toAbstract(), (repeatingOptionalDeclarations!=null?repeatingOptionalDeclarations.toAbstract():null));
		}
	}
	
	public class FunctionDeclaration extends Declaration {
		private final Ident ident;
		private final ParameterList parameterList;
		private final StorageDeclaration storageDeclaration;
		private final OptionalGlobalImports optionalGlobalImports;
		private final OptionalLocalStorageDeclarations optionalLocalStorageDeclarations;
		private final BlockCmd blockCmd;

		public FunctionDeclaration(Ident ident, ParameterList parameterList, StorageDeclaration storageDeclaration, OptionalGlobalImports optionalGlobalImports, OptionalLocalStorageDeclarations optionalLocalStorageDeclarations, BlockCmd blockCmd) {
			this.ident = ident;
			this.parameterList = parameterList;
			this.storageDeclaration = storageDeclaration;
			this.optionalGlobalImports = optionalGlobalImports;
			this.optionalLocalStorageDeclarations = optionalLocalStorageDeclarations;
			this.blockCmd = blockCmd;
		}

		public String toString(String indent) {
			return indent
					+ "<FunctionDeclaration>\n"
					+ ident.toString(indent + '\t')
					+ parameterList.toString(indent + '\t')
					+ storageDeclaration.toString(indent + '\t')
					+ optionalGlobalImports.toString(indent + '\t')
					+ optionalLocalStorageDeclarations.toString(indent + '\t')
					+ blockCmd.toString(indent + '\t')
					+ indent
					+ "</FunctionDeclaration>\n";
		}
		
		public AbsTree.DeclarationFunction toAbstract(RepeatingOptionalDeclarations repeatingOptionalDeclarations) {
			return new AbsTree.DeclarationFunction(ident, parameterList.toAbstract(), storageDeclaration.toAbstract(null), optionalGlobalImports.toAbstract(), optionalLocalStorageDeclarations.toAbstract(), blockCmd.toAbstract(), repeatingOptionalDeclarations.toAbstract());
		}
	}
	
	public class ProcedureDeclaration extends Declaration {
		private final Ident ident;
		private final ParameterList parameterList;
		private final OptionalGlobalImports optionalGlobalImports;
		private final OptionalLocalStorageDeclarations optionalLocalStorageDeclarations;
		private final BlockCmd blockCmd;

		public ProcedureDeclaration(Ident ident, ParameterList parameterList, OptionalGlobalImports optionalGlobalImports, OptionalLocalStorageDeclarations optionalLocalStorageDeclarations, BlockCmd blockCmd) {
			this.ident = ident;
			this.parameterList = parameterList;
			this.optionalGlobalImports = optionalGlobalImports;
			this.optionalLocalStorageDeclarations = optionalLocalStorageDeclarations;
			this.blockCmd = blockCmd;
		}

		public String toString(String indent) {
			return indent
					+ "<ProcedureDeclaration>\n"
					+ ident.toString(indent + '\t') 
					+ parameterList.toString(indent + '\t')
					+ optionalGlobalImports.toString(indent + '\t')
					+ optionalLocalStorageDeclarations.toString(indent + '\t')
					+ blockCmd.toString(indent + '\t')
					+ indent
					+ "</ProcedureDeclaration>\n";
		}
		
		public AbsTree.DeclarationProcedure toAbstract(RepeatingOptionalDeclarations repeatingOptionalDeclaration) {
			return new AbsTree.DeclarationProcedure(ident, parameterList.toAbstract(), optionalGlobalImports.toAbstract(), optionalLocalStorageDeclarations.toAbstract(), blockCmd.toAbstract(), repeatingOptionalDeclaration.toAbstract());
		}
	}
	//neu
	public class ProgramParameterList {
		private final OptionalProgramParameters optionalProgramParameters;

		public ProgramParameterList(OptionalProgramParameters optionalProgramParameters) {
			this.optionalProgramParameters = optionalProgramParameters;
		}

		public String toString(String indent) {
			return indent
					+ "<ProgramParameterList>\n"
					+ optionalProgramParameters.toString(indent + '\t')
					+ indent
					+ "</ProgramParameterList>\n";
		}
		
		public AbsTree.ProgramParameter toAbstract() {
			return optionalProgramParameters.toAbstract();
		}
	}
	//neu
	public class TypedIdent {
		private final Ident ident;
		private final TypeDeclaration typeDeclaration;

		public TypedIdent(Ident ident, TypeDeclaration typeDeclaration){
			this.ident = ident;
			this.typeDeclaration = typeDeclaration;
		}
		
		public String toString(String indent) {
			return indent
					+ "<TypedIdent>\n"
					+ ident.toString(indent + '\t')
					+ typeDeclaration.toString(indent + '\t')
					+ indent
					+ "</TypedIdent>\n";
		}
		public AbsTree.TypedIdent toAbstract() {
			return typeDeclaration.toAbstract(ident);
		}
	}
	public abstract class TypeDeclaration {
		public abstract String toString(String indent);
		public abstract AbsTree.TypedIdent toAbstract(Ident ident);
		
	}
	public class TypeDeclarationType extends TypeDeclaration {
		
		private final Type type;
		
		public TypeDeclarationType(Type type){
			this.type = type;
		}
		
		public String toString(String indent) {
			return indent
					+ "<TypeDeclarationType>\n"
					+ type.toString(indent + '\t')
					+ indent
					+ "</TypeDeclarationType>\n";
		}

		public AbsTree.TypedIdentType toAbstract(Ident ident) {
			return new AbsTree.TypedIdentType(ident, type);
		}
		
	}
	public class TypeDeclarationIdent extends TypeDeclaration {
		private final Ident ident;
		
		public TypeDeclarationIdent(Ident ident){
			this.ident = ident;
		}
		public String toString(String indent) {
			return indent
					+ "<TypeDeclarationIdent>\n"
					+ ident.toString(indent + '\t')
					+ indent
					+ "</TypeDeclarationIdent>\n";
		}

		public AbsTree.TypedIdentIdent toAbstract(Ident firstIdent) {
			return new AbsTree.TypedIdentIdent(firstIdent, ident);
		}
		
	}
	//gemacht 18.12.
	public class OptionalProgramParameters {
		private final OptionalFlowMode optionalFlowMode;
		private final OptionalChangeMode optionalChangeMode;
		private final TypedIdent typedIdent;
		private final RepeatingOptionalProgramParameters repeatingOptionalProgramParameters;

		public OptionalProgramParameters(OptionalFlowMode optionalFlowMode, OptionalChangeMode optionalChangeMode, TypedIdent typedIdent, RepeatingOptionalProgramParameters repeatingOptionalProgramParameters) {
			this.optionalFlowMode= optionalFlowMode;
			this.optionalChangeMode = optionalChangeMode;
			this.typedIdent = typedIdent;
			this.repeatingOptionalProgramParameters = repeatingOptionalProgramParameters;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalProgramParameters>\n"
					+ optionalFlowMode.toString(indent + '\t')
					+ optionalChangeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ repeatingOptionalProgramParameters.toString(indent + '\t')
					+ indent
					+ "</OptionalProgramParameters>\n";
		}
		
		public AbsTree.ProgramParameter toAbstract() {
			return new AbsTree.ProgramParameter(optionalFlowMode.toAbstract(), optionalChangeMode.toAbstract(), typedIdent.toAbstract(), repeatingOptionalProgramParameters.toAbstract());
		}
	}
	
	public class OptionalProgramParametersEpsilon extends OptionalProgramParameters {
		public OptionalProgramParametersEpsilon() {
			super(null, null, null, null);
		}
		public String toString(String indent) {
			return indent + "<OptionalProgramParamersEpsilon/>\n";
		}
		
		public AbsTree.ProgramParameter toAbstract() { return null; }
	}
	//gemacht 18.12.
	public class Parameter {
		private final OptionalFlowMode optionalFlowMode;
		private final OptionalMechMode optionalMechMode;
		private final OptionalChangeMode optionalChangeMode;
		private final TypedIdent typedIdent;

		public Parameter(OptionalFlowMode optionalFlowMode, OptionalMechMode optionalMechMode, OptionalChangeMode optionalChangeMode, TypedIdent typedIdent) {
			this.optionalFlowMode = optionalFlowMode;
			this.optionalMechMode = optionalMechMode;
			this.optionalChangeMode = optionalChangeMode;
			this.typedIdent = typedIdent;
		}

		public String toString(String indent) {
			return indent
					+ "<Parameter>\n"
					+ optionalFlowMode.toString(indent + '\t')
					+ optionalMechMode.toString(indent + '\t')
					+ optionalChangeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ indent
					+ "</Parameter>\n";
		}
		
		public AbsTree.Parameter toAbstract(RepeatingOptionalParameters repeatingOptionalParameters) {
			return new AbsTree.Parameter(optionalFlowMode.toAbstract(), optionalMechMode.toAbstract(), optionalChangeMode.toAbstract(), typedIdent.toAbstract(), repeatingOptionalParameters.toAbstract());
		}
	}
	//18.12.
	public class ParameterList {
		private final OptionalParameters optionalParameters;
		
		public ParameterList(OptionalParameters optionalParameters){
			this.optionalParameters = optionalParameters;
		}
		
		public String toString(String indent){
			return indent
					+ "<ParameterList>\n"
					+ optionalParameters.toString(indent + '\t')
					+ indent
					+ "</ParameterList>\n";
		}
		public AbsTree.Parameter toAbstract() {
			return optionalParameters.toAbstract();
		}
	}
	public class OptionalParameters {
		private final Parameter parameter;
		private final RepeatingOptionalParameters repeatingOptionalParameters;
		
		public OptionalParameters(Parameter parameter, RepeatingOptionalParameters repeatingOptionalParameters){
			this.parameter = parameter;
			this.repeatingOptionalParameters = repeatingOptionalParameters;
		}
		
		public String toString(String indent){
			return indent
					+ "<OptionalParameters>\n"
					+ parameter.toString(indent + '\t')
					+ repeatingOptionalParameters.toString(indent + '\t')
					+ indent
					+ "</OptionalParameters";
		}
		public AbsTree.Parameter toAbstract() {
			return parameter.toAbstract(repeatingOptionalParameters);
		}
	}
	public class OptionalParametersEpsilon extends OptionalParameters {
		public OptionalParametersEpsilon(){
			super(null, null);
		}
		public String toString(String indent){
			return indent + "<OptionalParametersEpsilon />\n";
		}
		public AbsTree.Parameter toAbstract() { return null; }
	}
	public class RepeatingOptionalParameters {
		private final Parameter parameter;
		private final RepeatingOptionalParameters repeatingOptionalParameters;
		
		public RepeatingOptionalParameters(Parameter parameter, RepeatingOptionalParameters repeatingOptionalParameters){
			this.parameter = parameter;
			this.repeatingOptionalParameters = repeatingOptionalParameters;
		}
		public String toString(String indent){
			return indent
					+ "<RepeatingOptionalParameters>\n"
					+ parameter.toString(indent + '\t')
					+ repeatingOptionalParameters.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalParameters>\n";
		}
		public AbsTree.Parameter toAbstract() {
			return parameter.toAbstract(repeatingOptionalParameters);
		}
	
	}
	public class RepeatingOptionalParametersEpsilon extends RepeatingOptionalParameters {
		public RepeatingOptionalParametersEpsilon(){
			super(null, null);
		}
		public String toString(String indent){
			return indent + "<RepeatingOptionalParametersEpsilon />\n";
		}
		public AbsTree.Parameter toAbstract() { return null; }
	}
	//neu abstract fertig
	public class RepeatingOptionalProgramParameters {
		
		private final OptionalFlowMode optionalFlowMode;
		private final OptionalChangeMode optionalChangeMode;
		private final TypedIdent typedIdent;
		private final RepeatingOptionalProgramParameters repeatingOptionalProgramParameters;
		

		public RepeatingOptionalProgramParameters(OptionalFlowMode optionalFlowMode, OptionalChangeMode optionalChangeMode, TypedIdent typedIdent, RepeatingOptionalProgramParameters repeatingOptionalProgramParameters) {
			this.optionalFlowMode = optionalFlowMode;
			this.optionalChangeMode = optionalChangeMode;
			this.typedIdent = typedIdent;
			this.repeatingOptionalProgramParameters = repeatingOptionalProgramParameters;
		}

		public String toString(String indent) {
			return indent
					+ "<RepeatingOptionalProgramParameters\n"
					+ optionalFlowMode.toString(indent + '\t')
					+ optionalChangeMode.toString(indent + '\t')
					+ typedIdent.toString(indent + '\t')
					+ repeatingOptionalProgramParameters.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalProgramParameters\n";
		}
		
		public AbsTree.ProgramParameter toAbstract() {
			return new AbsTree.ProgramParameter(optionalFlowMode.toAbstract(), optionalChangeMode.toAbstract(), typedIdent.toAbstract(), repeatingOptionalProgramParameters.toAbstract());
		}
	}
	
	public class RepeatingOptionalProgramParametersEpsilon extends RepeatingOptionalProgramParameters {
		public RepeatingOptionalProgramParametersEpsilon() {
			super(null, null, null, null);
		}
		public String toString(String indent) {
			return indent + "<RepeatingOptionalProgramParametersEpsilon/>\n";
		}
		public AbsTree.ProgramParameter toAbstract() { return null; }
	}
	
	public class RepeatingOptionalIdents {
		private final Ident ident;
		private final Idents idents;

		public RepeatingOptionalIdents(Ident ident, Idents idents) {
			this.ident = ident;
			this.idents = idents;
		}

		public String toString(String indent) {
			return indent
					+ "<RepeatingOptionalIdents>\n"
					+ ident.toString(indent + '\t')
					+ idents.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalIdents>\n";
		}
		
		public AbsTree.GlobalInits toAbstract() {
			return new AbsTree.GlobalInits(ident, idents.toAbstract());
		}
	}
	
	public class RepeatingOptionalIdentsEpsilon extends RepeatingOptionalIdents {
		public RepeatingOptionalIdentsEpsilon() {
			super(null,null);
		}
		public String toString(String indent) {
			return indent + "<RepeatingOptionalIdentsEpsilon/>\n";
		}
		public AbsTree.GlobalInits toAbstract() { return null; }
	}
	
	
	public class OptionalChangeMode {
		private final ChangeMode changeMode;
		public OptionalChangeMode(ChangeMode changeMode) {
			this.changeMode = changeMode;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalChangeMode>\n"
					+ changeMode.toString(indent + '\t')
					+ indent
					+ "</OptionalChangeMode>\n";
		}
		
		public ChangeMode toAbstract() {
			return changeMode;
		}
	}
	
	public class OptionalChangeModeEpsilon extends OptionalChangeMode {
		public OptionalChangeModeEpsilon() {
			super(null);
		}
		public String toString(String indent) {
			return indent
					+ "<OptionalChangeModeEpsilon/>\n";
		}
		public ChangeMode toAbstract() {
			return new ChangeMode(ModeAttribute.CONST);
		}
	}
	
	public class OptionalLocalStorageDeclarations {
		
		private final StorageDeclaration storageDeclaration;
		private final RepeatingOptionalStorageDeclarations repeatingOptionalStorageDeclarations;
		
		public OptionalLocalStorageDeclarations(StorageDeclaration storageDeclaration, RepeatingOptionalStorageDeclarations repeatingOptionalStorageDeclarations) {
			this.storageDeclaration = storageDeclaration;
			this.repeatingOptionalStorageDeclarations = repeatingOptionalStorageDeclarations;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalLocalStorageDeclarations>\n"
					+ storageDeclaration.toString(indent + '\t')
					+ repeatingOptionalStorageDeclarations.toString(indent + '\t')
					+ indent
					+ "</OptionalLocalStorageDeclarations>\n";
		}
		
		public AbsTree.Declaration toAbstract() {
			return storageDeclaration.toAbstract(repeatingOptionalStorageDeclarations);
		}
	}
	
	public class OptionalLocalStorageDeclarationsEpsilon extends OptionalLocalStorageDeclarations {
		public OptionalLocalStorageDeclarationsEpsilon() {
			super(null,null);
		}
		public String toString(String indent) {
			return indent + "<OptionalLocalStorageDeclarationsEpsilon/>\n";
		}
		public AbsTree.Declaration toAbstract() { return null; }
	}
	
	public class OptionalFlowMode {
		private final FlowMode flowMode;

		public OptionalFlowMode(FlowMode flowMode) {
			this.flowMode = flowMode;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalFlowMode>\n"
					+ flowMode.toString(indent + '\t') 
					+ indent
					+ "</OptionalFlowMode>\n";
		}
		
		public FlowMode toAbstract() { return flowMode; }
	}
	
	public class OptionalFlowModeEpsilon extends OptionalFlowMode {

		public OptionalFlowModeEpsilon() {
			super(null);
		}

		public String toString(String indent) {
			return indent + "<OptionalFlowModeEpsilon/>\n";
		}
		
		public FlowMode toAbstract() { return new FlowMode(ModeAttribute.IN); }
	}
	
	public class OptionalMechMode {
		private final MechMode mechMode;

		public OptionalMechMode(MechMode mechMode) {
			this.mechMode = mechMode;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalMechMode>\n"
					+ mechMode.toString(indent + '\t') 
					+ indent
					+ "</OptionalMechMode>\n";
		}
		
		public MechMode toAbstract() { return mechMode; }
	}
	
	public class OptionalMechModeEpsilon extends OptionalMechMode {
		public OptionalMechModeEpsilon() {
			super(null);
		}
		public String toString(String indent) {
			return indent + "<OptionalMechModeEpsilon/>\n";
		}
		public MechMode toAbstract() { return new MechMode(ModeAttribute.COPY); }
	}
	
	public class BlockCmd {
		private final Cmd cmd;
		private final RepeatingOptionalCmds repeatingOptionalCmds;

		public BlockCmd(Cmd cmd, RepeatingOptionalCmds repeatingOptionalCmds) {
			this.cmd = cmd;
			this.repeatingOptionalCmds = repeatingOptionalCmds;
		}

		public String toString(String indent) {
			return indent
					+ "<BlockCmd>\n"
					+ cmd.toString(indent + '\t')
					+ repeatingOptionalCmds.toString(indent + '\t')
					+ indent
					+ "</BlockCmd>\n";
		}
		
		public AbsTree.Cmd toAbstract() {
			return cmd.toAbstract(repeatingOptionalCmds);
		}
	}
	
	public abstract class Cmd {
		public abstract String toString(String indent);
		public abstract AbsTree.Cmd toAbstract(RepeatingOptionalCmds repeatingOptionalCmds);
	}
	
	public class RepeatingOptionalCmds {
		private final Cmd cmd;
		private final RepeatingOptionalCmds repeatingOptionalCmds;
		
		public RepeatingOptionalCmds(Cmd cmd, RepeatingOptionalCmds repeatingOptionalCmds) {
			this.cmd = cmd;
			this.repeatingOptionalCmds = repeatingOptionalCmds;
		}

		public String toString(String indent) {
			return indent
					+ "<RepeatingCmds>\n"
					+ cmd.toString(indent + '\t')
					+ repeatingOptionalCmds.toString(indent + '\t')
					+ indent
					+ "</RepeatingCmds>\n";
		}
		
		public AbsTree.Cmd toAbstract() {
			return cmd.toAbstract(repeatingOptionalCmds);
		}
	}
	
	public class RepeatingCmdsEpsilon extends RepeatingOptionalCmds {
		
		public RepeatingCmdsEpsilon() {
			super(null,null);
		}
		
		public String toString(String indent) {
			return indent + "<RepeatingCmdsEpsilon/>\n";
		}
		
		public AbsTree.Cmd toAbstract() { return null; }
	}
	
	public class CmdSkip extends Cmd {

		public String toString(String indent) {
			return indent + "<CmdSkip/>\n";
		}
		
		public AbsTree.CmdSkip toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
			return new AbsTree.CmdSkip(null);
		}
	}
	
	public class CmdExpression extends Cmd{
		private final Expression expression;
		private final Expression nextExpression;

		public CmdExpression(Expression expression, Expression nextExpression) {
			this.expression = expression;
			this.nextExpression = nextExpression;
		}

		public String toString(String indent) {
			return indent
					+ "<CmdExpression>\n"
					+ expression.toString(indent + '\t')
					+ nextExpression.toString(indent + '\t')
					+ indent
					+ "</CmdExpression>\n";
		}
		
		public AbsTree.CmdAssi toAbstract(RepeatingOptionalCmds repCmd) {
			return new AbsTree.CmdAssi(expression.toAbstract(), nextExpression.toAbstract(), repCmd.toAbstract());
		}
	}
	
	public class CmdCall extends Cmd {
		private final Ident ident;
		private final ExpressionList expressionList;
		private final OptionalGlobalInits optionalGlobalInits;

		public CmdCall(Ident ident, ExpressionList expressionList, OptionalGlobalInits optionalGlobalInits) {
			this.ident = ident;
			this.expressionList = expressionList;
			this.optionalGlobalInits = optionalGlobalInits;
		}

		public String toString(String indent) {
			return indent
					+ "<CmdCall>\n"
					+ ident.toString(indent + '\t')
					+ expressionList.toString(indent + '\t')
					+ optionalGlobalInits.toString(indent + '\t')
					+ indent
					+ "</CmdCall>\n";
		}
		
		public AbsTree.CmdProcCall toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
			return new AbsTree.CmdProcCall(new AbsTree.RoutineCall(ident, expressionList.toAbstract()), optionalGlobalInits.toAbstract(), repeatingOptionalCmds.toAbstract());
		}
	}
	
	public class CmdWhile extends Cmd {
		private final Expression expression;
		private final BlockCmd blockCmd;

		public CmdWhile(Expression expression, BlockCmd blockCmd) {
			this.expression = expression;
			this.blockCmd = blockCmd;
		}

		public String toString(final String indent) {
			return indent
					+ "<CmdWhile>\n"
					+ expression.toString(indent + '\t')
					+ blockCmd.toString(indent + '\t')
					+ indent
					+ "</CmdWhile>\n";
		}
		
		public AbsTree.CmdWhile toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
			return new AbsTree.CmdWhile(expression.toAbstract(), blockCmd.toAbstract(), repeatingOptionalCmds.toAbstract());
		}
	}
	
	public class CmdIf extends Cmd {
		private final Expression expr;
		private final BlockCmd ifCmd;
		private final BlockCmd elseCmd;

		public CmdIf(Expression expr, BlockCmd ifCmd, BlockCmd elseCmd) {
			this.expr = expr;
			this.ifCmd = ifCmd;
			this.elseCmd = elseCmd;
		}

	    public String toString(String indent) {
			return indent
					+ "<CmdIf>\n"
					+ expr.toString(indent + '\t')
					+ ifCmd.toString(indent + '\t')
					+ elseCmd.toString(indent + '\t')
					+ indent
					+ "</CmdIf>\n";
		}
	    
	    public AbsTree.CmdCond toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
	    	return new AbsTree.CmdCond(expr.toAbstract(), ifCmd.toAbstract(), elseCmd.toAbstract(), repeatingOptionalCmds.toAbstract());
	    }
	}
	
	public class CmdDebugIn extends Cmd {
		private final Expression expression;

		public CmdDebugIn(Expression expression) {
			this.expression = expression;
		}

		public String toString(String indent) {
			return indent
					+ "<CmdDebugIn>\n"
					+ expression.toString(indent + '\t')
					+ indent
					+ "</CmdDebugIn>\n";
		}
		
		public AbsTree.CmdInput toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
			return new AbsTree.CmdInput(expression.toAbstract(), repeatingOptionalCmds.toAbstract());
		}
	}
	
	public class CmdDebugOut extends Cmd {
		private final Expression expression;

		public CmdDebugOut(Expression expression) {
			this.expression = expression;
		}

		public String toString(String indent) {
			return indent
					+ "<CmdDebugOut>\n"
					+ expression.toString(indent + '\t')
					+ indent
					+ "</CmdDebugOut>\n";
		}
		
		public AbsTree.CmdOutput toAbstract(RepeatingOptionalCmds repeatingOptionalCmds) {
			return new AbsTree.CmdOutput(expression.toAbstract(), repeatingOptionalCmds.toAbstract());
		}
	}
	
	public class OptionalGlobalInits {
		private final Idents idents;

		public OptionalGlobalInits(Idents idents) {
			this.idents = idents;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalGlobalInits>\n"
					+ idents.toString(indent + '\t')
					+ indent
					+ "</OptionalGlobalInits>\n";
		}
		
		public AbsTree.GlobalInits toAbstract() { return idents.toAbstract(); }
	}
	
	public class OptionalGlobalInitsEpsilon extends OptionalGlobalInits {
		
		public OptionalGlobalInitsEpsilon() {
			super(null);
		}
		
		public String toString(String indent) {
			return indent + "<OptionalGlobalInitsEpsilon/>\n";
		}
		
		public AbsTree.GlobalInits toAbstract() { return null; }
	}
	
	public class Idents {
		private final Ident ident;
		private final RepeatingOptionalIdents repeatingOptionalIdents;

		public Idents(Ident ident, RepeatingOptionalIdents repeatingOptionalIdents) {
			this.ident = ident;
			this.repeatingOptionalIdents = repeatingOptionalIdents;
		}

		public String toString(String indent) {
			return indent
					+ "<Idents>\n"
					+ ident.toString(indent + '\t')
					+ repeatingOptionalIdents.toString(indent + '\t')
					+ indent
					+ "</Idents>\n";
		}
		
		public AbsTree.GlobalInits toAbstract() {
			return new AbsTree.GlobalInits(ident, repeatingOptionalIdents.toAbstract());
		}
	}
	
	public class ExpressionList {
		private final OptionalExpressions optionalExpressions;

		public ExpressionList(OptionalExpressions optionalExpressions) {
			this.optionalExpressions = optionalExpressions;
		}

		public String toString(String indent) {
			return indent
					+ "<ExpressionList>\n"
					+ optionalExpressions.toString(indent + '\t')
					+ indent
					+ "</ExpressionList>\n";
		}
		
		public AbsTree.ExpressionList toAbstract() {
			return optionalExpressions.toAbstract();
		}
	}
	
	public class OptionalExpressions {
		private final Expression expression;
		private final RepeatingOptionalExpressions repeatingOptionalExpressions;

		public OptionalExpressions(Expression expression, RepeatingOptionalExpressions repeatingOptionalExpressions) {
			this.expression = expression;
			this.repeatingOptionalExpressions = repeatingOptionalExpressions;
		}

		public String toString(String indent) {
			return indent
					+ "<RepeatingOptionalExpressions>\n"
					+ expression.toString(indent + '\t')
					+ repeatingOptionalExpressions.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalExpressions>\n";
		}
		
		public AbsTree.ExpressionList toAbstract() {
			return new AbsTree.ExpressionList(expression.toAbstract(), repeatingOptionalExpressions.toAbstract());
		}
	}
	
	public class OptionalExpressionsEpsilon extends OptionalExpressions {
		public OptionalExpressionsEpsilon() {
			super(null, null);
		}
		public String toString(String indent) {
			return indent + "<OptionalExpressionsEpsilon/>\n";
		}
		
		public AbsTree.ExpressionList toAbstract() { return null; }
	}
	
	
	public class Expression {
		private final Term1 term1;
		private final RepBOOLOPRterm1 repBOOLOPRterm1;

		public Expression(Term1 term1, RepBOOLOPRterm1 repBOOLOPRterm1) {
			this.term1 = term1;
			this.repBOOLOPRterm1 = repBOOLOPRterm1;
		}

		public String toString(String indent) {
			return indent
					+ "<Expression>\n"
					+ term1.toString(indent + '\t')
					+ repBOOLOPRterm1.toString(indent + '\t')
					+ indent
					+ "</Expression>\n";
		}
		
		public AbsTree.Expression toAbstract() {
			return repBOOLOPRterm1.toAbstract(term1.toAbstract());
		}
	}
	
	public class RepeatingOptionalExpressions {
		
		private final Expression expression;
		private final RepeatingOptionalExpressions repeatingOptionalExpressions;

		public RepeatingOptionalExpressions(Expression expression, RepeatingOptionalExpressions repeatingOptionalExpressions) {
			this.expression = expression;
			this.repeatingOptionalExpressions = repeatingOptionalExpressions;
		}

		public String toString(String indent) {
			return indent
					+ "<RepeatingOptionalExpressions>\n"
					+ expression.toString(indent + '\t')
					+ repeatingOptionalExpressions.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalExpressions>\n";
		}
		
		public AbsTree.ExpressionList toAbstract() {
			return new AbsTree.ExpressionList(expression.toAbstract(), repeatingOptionalExpressions.toAbstract());
		}
	}
	
	public class RepeatingOptionalExpressionsEpsilon extends RepeatingOptionalExpressions {
		public RepeatingOptionalExpressionsEpsilon() {
			super(null,null);
		}
		public String toString(String indent) {
			return indent + "<RepeatingOptionalExpressionsEpsilon/>\n";
		}
		public AbsTree.ExpressionList toAbstract() { return null; }
	}
	
	public class Term1 {
		private final Term2 term2;
		private final RepRELOPRterm2 repRELOPRterm2;

		public Term1(Term2 term2, RepRELOPRterm2 repRELOPRterm2) {
			this.term2 = term2;
			this.repRELOPRterm2 = repRELOPRterm2;
		}

		public String toString(String indent) {
			return indent
					+ "<Term1>\n"
					+ term2.toString(indent + '\t')
					+ repRELOPRterm2.toString(indent + '\t')
					+ indent
					+ "</Term1>\n";
		}
		
		public AbsTree.Expression toAbstract() {
			return repRELOPRterm2.toAbstract(term2.toAbstract());
		}
	}
	
	public class RepBOOLOPRterm1 {
		private final BoolOpr boolOpr;
		private final Term1 term1;
		private final RepBOOLOPRterm1 repBOOLOPRterm1;

		public RepBOOLOPRterm1(BoolOpr boolOpr, Term1 term1, RepBOOLOPRterm1 repBOOLPORterm1) {
			this.boolOpr = boolOpr;
			this.term1 = term1;
			this.repBOOLOPRterm1 = repBOOLPORterm1;
		}

		public String toString(String indent) {
			return indent
					+ "<repBOOLPORterm1>\n"
					+ boolOpr.toString(indent + '\t')
					+ term1.toString(indent + '\t')
					+ repBOOLOPRterm1.toString(indent + '\t')
					+ indent
					+ "</repBOOLPORterm1>\n";
		}
		
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) {
			return repBOOLOPRterm1.toAbstract(new AbsTree.ExprDyadic(boolOpr, expression, term1.toAbstract()));
		}
	}
	
	public class RepBOOLOPRterm1Epsilon extends RepBOOLOPRterm1 {
		public RepBOOLOPRterm1Epsilon() {
			super(null, null, null);
		}
		public String toString(String indent) {
			return indent + "<RepBOOLOPRTerm1Epsilon/>\n";
		}
		
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) {
			return expression;
		}
	}
	
	public class Term2 {
		private final Term3 term3;
		private final RepADDOPRterm3 repADDOPRTerm3;

		public Term2(Term3 term3, RepADDOPRterm3 repADDOPRTerm3) {
			this.term3 = term3;
			this.repADDOPRTerm3 = repADDOPRTerm3;
		}

		public String toString(String indent) {
			return indent
					+ "<Term2>\n"
					+ term3.toString(indent + '\t')
					+ repADDOPRTerm3.toString(indent + '\t')
					+ indent
					+ "</Term2>\n";
		}
		public AbsTree.Expression toAbstract() {
			return repADDOPRTerm3.toAbstract(term3.toAbstract());
		}
	}
	
	public class RepRELOPRterm2 {
		private final RelOpr relOpr;
		private final Term2 term2;
		private final RepRELOPRterm2 repADDOPRTerm2;

		public RepRELOPRterm2(RelOpr relOpr, Term2 term2, RepRELOPRterm2 repADDOPRTerm2) {
			this.relOpr = relOpr;
			this.term2 = term2;
			this.repADDOPRTerm2 = repADDOPRTerm2;
		}

		public String toString(String indent) {
			return indent
					+ "<RepADDOPRTerm2>\n"
					+ relOpr.toString(indent + '\t')
					+ term2.toString(indent + '\t')
					+ repADDOPRTerm2.toString(indent + '\t')
					+ indent
					+ "</RepADDOPRTerm2>\n";
		}
		
		public AbsTree.Expression toAbstract(AbsTree.Expression expr) {
			return repADDOPRTerm2.toAbstract(new AbsTree.ExprDyadic(relOpr, expr, term2.toAbstract()));
		}
	}
	
	public class RepRELOPRterm2Epsilon extends RepRELOPRterm2 {
		public RepRELOPRterm2Epsilon() {
			super(null, null, null);
		}
		public String toString(String indent) {
			return indent + "<RepRELOPRterm2Epsilon />\n";
		}
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) {
			return expression;
		}
	}
	
	public class Term3 {
		private final Term4 term4;
		private final RepMULTOPRterm4 repMULTOPRterm4;

		public Term3(Term4 term4, RepMULTOPRterm4 repMULTOPRterm4) {
			this.term4 = term4;
			this.repMULTOPRterm4 = repMULTOPRterm4;
		}

		public String toString(String indent) {
			return indent
					+ "<Term3>\n"
					+ term4.toString(indent + '\t')
					+ repMULTOPRterm4.toString(indent + '\t')
					+ indent
					+ "</Term3>\n";
		}
		
		public AbsTree.Expression toAbstract() {
			return repMULTOPRterm4.toAbstract(term4.toAbstract());
		}
	}
	
	public class RepADDOPRterm3 {
		private final AddOpr addOpr;
		private final Term3 term3;
		private final RepADDOPRterm3 repADDOPRTerm3;

		public RepADDOPRterm3(AddOpr addOpr, Term3 term3, RepADDOPRterm3 repTerm3) {
			this.addOpr = addOpr;
			this.term3 = term3;
			this.repADDOPRTerm3 = repTerm3;
		}

		public String toString(String indent) {
			return indent
					+ "<RepADDOPRTerm3>\n"
					+ addOpr.toString(indent + '\t')
					+ term3.toString(indent + '\t')
					+ repADDOPRTerm3.toString(indent + '\t')
					+ indent
					+ "</RepADDOPRTerm3>\n";
		}
		
		public AbsTree.Expression toAbstract(AbsTree.Expression expr) {
			return repADDOPRTerm3.toAbstract(new AbsTree.ExprDyadic(addOpr, expr, term3.toAbstract()));
		}
	}
	
	public class RepADDOPRterm3Epsilon extends RepADDOPRterm3 {
		public RepADDOPRterm3Epsilon() {
			super(null, null, null);
		}
		public String toString(String indent) {
			return indent + "<RepADDOPRTerm3Eps/>\n";
		}
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) { return expression; }
	}
	
	public abstract class Factor {
		public abstract String toString(String indent);
		public abstract AbsTree.Expression toAbstract();
	}
	
	public class RepMULTOPRterm4 {
		private final MultOpr multOpr;
		private final Term4 term4;
		private final RepMULTOPRterm4 repMULTOPRterm4;

		public RepMULTOPRterm4(MultOpr multOpr, Term4 term4, RepMULTOPRterm4 repMULTOPRterm4) {
			this.multOpr = multOpr;
			this.term4 = term4;
			this.repMULTOPRterm4 = repMULTOPRterm4;
		}

		public String toString(String indent) {
			return indent
					+ "<RepMULTOPRterm4>\n"
					+ multOpr.toString(indent + '\t')
					+ term4.toString(indent + '\t')
					+ repMULTOPRterm4.toString(indent + '\t')
					+ indent
					+ "</RepMULTOPRterm4>\n";
		}
		
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) {
			return repMULTOPRterm4.toAbstract(new AbsTree.ExprDyadic(multOpr, expression, term4.toAbstract()));
		}
	}
	public class RepMULTOPRterm4Epsilon extends RepMULTOPRterm4 {
		public RepMULTOPRterm4Epsilon(){
			super(null,null,null);
		}
		public String toString(String indent){
			return indent + "<RepMULTOPRterm4Espilon />\n";
		}
		public AbsTree.Expression toAbstract(AbsTree.Expression expression) { return expression; }
	}
	public class RepDOTOPRfactor {
		private final Operator dotOpr;
		private final Factor factor;
		private final RepDOTOPRfactor repDOTOPRfactor;
		public RepDOTOPRfactor(Operator dotOpr, Factor factor, RepDOTOPRfactor repDOTOPRfactor) {
			this.dotOpr = dotOpr;
			this.factor = factor;
			this.repDOTOPRfactor = repDOTOPRfactor;
		}
		public String toString(String indent){
			return indent
					+ "<RepDOTOPRfactor>\n"
					+ dotOpr.toString(indent + '\t')
					+ factor.toString(indent + '\t')
					+ repDOTOPRfactor.toString(indent + '\t')
					+ indent
					+ "</RepDOTPORfactor>\n";
		}
		public AbsTree.Expression toAbstract(AbsTree.Expression expression){
			return repDOTOPRfactor.toAbstract(new AbsTree.ExprDyadic(dotOpr, factor.toAbstract(), expression));
		}

	}
	public class RepDOTOPRfactorEpsilon extends RepDOTOPRfactor {
		public RepDOTOPRfactorEpsilon(){
			super(null,null,null);
		}
		public String toString(String indent){
			return indent + "<RepDOTOPRfactorEpsilon />\n";
		}
		public AbsTree.Expression toAbstract(AbsTree.Expression expression){
			return expression;
		}
	}
	
	public class Term4 {
		private final Factor factor;
		private final RepDOTOPRfactor repDOTOPRfactor;
		
		public Term4(Factor factor, RepDOTOPRfactor repDOTOPRfactor) {
			this.factor = factor;
			this.repDOTOPRfactor = repDOTOPRfactor;
		}
		public String toString(String indent){
			return indent
					+ "<Term4>\n"
					+ factor.toString(indent + '\t')
					+ repDOTOPRfactor.toString(indent + '\t')
					+ indent
					+ "</Term4>\n";
		}
		public AbsTree.Expression toAbstract(){
			return repDOTOPRfactor.toAbstract(factor.toAbstract());
		}
	}
	
	public class FactorLiteral extends Factor {
		private final Literal literal;

		public FactorLiteral(Literal literal) {
			this.literal = literal;
		}

		public String toString(String indent) {
			return indent
					+ "<FactorLiteral>\n"
					+ literal.toString(indent + '\t')
					+ indent
					+ "</FactorLiteral>\n";
		}
		
		public AbsTree.ExprLiteral toAbstract() {
			return new AbsTree.ExprLiteral(literal);
		}
	}
	
	public class FactorIdent extends Factor {
		private final Ident ident;
		private final OptionalIdent optionalIdent;

		public FactorIdent(Ident ident, OptionalIdent optionalIdent) {
			this.ident = ident;
			this.optionalIdent = optionalIdent;
		}

		public String toString(String indent) {
			return indent
					+ "<FactorIdent>\n"
					+ ident.toString(indent + '\t')
					+ optionalIdent.toString(indent + '\t')
					+ indent
					+ "</FactorIdent>\n";
		}
		
		public AbsTree.Expression toAbstract() {
			return optionalIdent.toAbstract(ident);
		}
	}
	
	public class FactorMonadicOperator extends Factor {
		private final MonadicOperator monadicOpr;
		private final Factor factor;

		public FactorMonadicOperator(MonadicOperator monadicOpr, Factor factor) {
			this.monadicOpr = monadicOpr;
			this.factor = factor;
		}

		public String toString(String indent) {
			return indent
					+ "<FactorMonadicOperator>\n"
					+ monadicOpr.toString(indent + '\t')
					+ factor.toString(indent + '\t')
					+ indent
					+ "</FactorMonadicOperator>\n";
		}
		
		public AbsTree.ExprMonadic toAbstract() {
			return new AbsTree.ExprMonadic(monadicOpr.toAbstract(), factor.toAbstract());
		}
	}
	
	public class FactorExpression extends Factor {
		private final Expression expr;

		public FactorExpression(Expression expr) {
			this.expr = expr;
		}

		public String toString(String indent) {
			return indent
					+ "<FactorExpression>\n"
					+ expr.toString(indent + '\t')
					+ indent
					+ "</FactorExpression>\n";
		}

		public AbsTree.Expression toAbstract() {
			return expr.toAbstract();
		}
	}
	
	public abstract class OptionalIdent {
		public abstract String toString(String indent);
		public abstract AbsTree.Expression toAbstract(Ident ident);
	}
	
	public class OptionalIdentInit extends OptionalIdent {

		public String toString(String indent) {
			return indent + "<OptionalIdentInit/>\n";
		}
		
		public AbsTree.ExprStore toAbstract(Ident ident) {
			return new AbsTree.ExprStore(ident, true);
		}
	}
	
	public class OptionalIdentExpressionList extends OptionalIdent {
		private final ExpressionList expressionList;

		public OptionalIdentExpressionList(ExpressionList expressionList) {
			this.expressionList = expressionList;
		}

		public String toString(String indent) {
			return indent
					+ "<OptionalIdentExpressionList>\n"
					+ expressionList.toString(indent + '\t')
					+ indent
					+ "</OptionalIdentExpressionList>\n";
		}
		
		public AbsTree.ExprFunCall toAbstract(Ident ident) {
			return new AbsTree.ExprFunCall(new AbsTree.RoutineCall(ident, expressionList.toAbstract()));
		}
	}
	
	public class OptionalIdentEpsilon extends OptionalIdent {

		public String toString(String indent) {
			return indent + "<OptionalIdentEpsilon/>\n";
		}
		
		public AbsTree.ExprStore toAbstract(Ident ident) {
			return new AbsTree.ExprStore(ident, false);
		}
	}
	
	public class MonadicOperator {
		private final Operator operator;

		public MonadicOperator(Operator operator) {
			this.operator = operator;
		}

		public String toString(String indent) {
			return indent
					+ "<MonadicOperator>\n"
					+ operator.toString(indent + '\t')
					+ indent
					+ "</MonadicOperator>\n";
		}
		
		public Operator toAbstract() {
			return operator;
		}
	}
	public class OptionalGlobalImports {
		private final GlobalImport globalImport;
		private final RepeatingOptionalGlobalImports repeatingOptionalGlobalImports;
		public OptionalGlobalImports(GlobalImport globalImport, RepeatingOptionalGlobalImports repeatingOptionalGlobalImports){
			this.globalImport = globalImport;
			this.repeatingOptionalGlobalImports = repeatingOptionalGlobalImports;
		}
		
		public String toString(String indent){
			return indent
					+ "<OptionalGlobalImports>\n"
					+ globalImport.toString(indent + '\t')
					+ repeatingOptionalGlobalImports.toString(indent + '\t')
					+ indent
					+ "</OptionalGlobalImports>\n";
		}
		public AbsTree.GlobalImport toAbstract() {
			return globalImport.toAbstract(repeatingOptionalGlobalImports);
		}
		
	}
	public class OptionalGlobalImportsEpsilon extends OptionalGlobalImports {
		
		public OptionalGlobalImportsEpsilon(){
			super(null,null);
		}
		
		public String toString(String indent){
			return indent + "<OptionalGlobalImportsEpsilon/>\n";
		}
		public AbsTree.GlobalImport toAbstract(){ return null; }
	}
	public class GlobalImport {
		private final OptionalFlowMode optionalFlowMode;
		private final OptionalChangeMode optionalChangeMode;
		private final Ident ident;
		
		public GlobalImport(OptionalFlowMode optionalFlowMode, OptionalChangeMode optionalChangeMode, Ident ident){
			this.optionalFlowMode = optionalFlowMode;
			this.optionalChangeMode = optionalChangeMode;
			this.ident = ident;
		}
		
		public String toString(String indent){
			return indent
					+ "<GlobalImport>\n"
					+ optionalFlowMode.toString(indent + '\t')
					+ optionalChangeMode.toString(indent + '\t')
					+ ident.toString(indent + '\t')
					+ indent
					+ "</GlobalImport>\n";
		}
		public AbsTree.GlobalImport toAbstract(RepeatingOptionalGlobalImports repeatingOptionalGlobalImports) {
			return new AbsTree.GlobalImport(optionalFlowMode.toAbstract(), optionalChangeMode.toAbstract(), ident, repeatingOptionalGlobalImports.toAbstract());
		}
	}
	public class RepeatingOptionalGlobalImports {
		private final GlobalImport globalImport;
		private final RepeatingOptionalGlobalImports repeatingOptionalGlobalImports;
		
		public RepeatingOptionalGlobalImports(GlobalImport globalImport, RepeatingOptionalGlobalImports repeatingOptionalGlobalImports){
			this.globalImport = globalImport;
			this.repeatingOptionalGlobalImports = repeatingOptionalGlobalImports;
		}
		
		public String toString(String indent){
			return indent
					+ "<RepeatingOptionalGlobalImports>\n"
					+ globalImport.toString(indent + '\t')
					+ repeatingOptionalGlobalImports.toString(indent + '\t')
					+ indent
					+ "</RepeatingOptionalGlobalImports>\n";
		}
		public AbsTree.GlobalImport toAbstract(){
			return globalImport.toAbstract(repeatingOptionalGlobalImports);
		}
	}
	public class RepeatingOptionalGlobalImportsEpsilon extends RepeatingOptionalGlobalImports{
		public RepeatingOptionalGlobalImportsEpsilon(){
			super(null,null);
		}
		public String toString(String indent){
			return indent + "<RepeatingOptionalGlobalImportsEpsilon/>\n";
		}
		public AbsTree.GlobalImport toAbstract() { return null;}
	}
	

}
