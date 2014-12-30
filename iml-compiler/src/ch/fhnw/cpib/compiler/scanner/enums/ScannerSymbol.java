package ch.fhnw.cpib.compiler.scanner.enums;

public enum ScannerSymbol {

	LPAREN('('), RPAREN(')'), LBRACE('{'), RBRACE('}'), 
	COMMA(','), COLON(':'), SEMICOLON(';'), EQUALS('='), 
	ASTERISK('*'), PLUS('+'), MINUS('-'),
	SLASH('/'), LT('<'), GT('>'), DOT('.');
	
	private int charValue;
	
	ScannerSymbol(int charValue){
		this.charValue = charValue;
	}
	
	public char getCharValue(){
		return (char)charValue;
	}
	
	public static boolean contains(int value){
		for(ScannerSymbol s : values()){
			if(value == s.charValue){
				return true;
			}
		}
		return false;
	}
	
	
}
