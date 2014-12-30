package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;

public abstract class Symbol extends AbstractToken {

	/**
	 * Serial id which is used for deep copy
	 */
	private static final long serialVersionUID = -8221775855125717987L;
	
	public Symbol(Terminals terminal) {
		super(terminal);
	}
	
	@Override
	public String toString() {
		return getTerminal().toString();
	}
	@Override
	public boolean equals(Object o){
		if (o != null && o.getClass() == this.getClass()) {
			return (super.equals((AbstractToken) o));
	    }
	    return false;
	}
	
	public static class Colon extends Symbol {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -4297409361508947952L;
		
		/**
		 * Constructor to create a new Colon token
		 * @param terminal
		 */
		public Colon() {
			super(Terminals.COLON);
		}
	}
	
	public static class Comma extends Symbol {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -6431349416123719880L;

		/**
		 * Constructor to create a new Comma token
		 * @param terminal
		 */
		public Comma() {
			super(Terminals.COMMA);
		}
		
	}
	
	public static class Semicolon extends Symbol {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -6431349416123719880L;

		/**
		 * Constructor to create a new Semicolon token
		 * @param terminal
		 */
		public Semicolon() {
			super(Terminals.SEMICOLON);
		}
	}
	 /**
     * Class for Exclamation Mark token.
     */
    public static class DebugOut extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -4438981643984196791L;

        /**
         * Creates a new Exclamation Mark token.
         */
        public DebugOut() {
            super(Terminals.DEBUGOUT);
        }
    }

    /**
     * Class for Question Mark token.
     */
    public static class DebugIn extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -4230336958022241898L;

        /**
         * Creates a new Question Mark token.
         */
        public DebugIn() {
            super(Terminals.DEBUGIN);
        }
    }

    /**
     * Class for Becomes token ':='.
     */
    public static class Becomes extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = 4869909955231080470L;

        /**
         * Creates a new Becomes token ':='.
         */
        public Becomes() {
            super(Terminals.BECOMES);
        }
    }

    /**
     * Class for Left Brace token.
     */
    public static class LBrace extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = 4194464821518588448L;

        /**
         * Creates a new Left Brace token.
         */
        public LBrace() {
            super(Terminals.LBRACE);
        }
    }

    /**
     * Class for Right Brace token.
     */
    public static class RBrace extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -7875174837681837309L;

        /**
         * Creates a new Right Brace token.
         */
        public RBrace() {
            super(Terminals.RBRACE);
        }
    }

    /**
     * Class for Left Parenthesis token.
     */
    public static class LParen extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -574865805781420981L;

        /**
         * Creates a new Left Parenthesis token.
         */
        public LParen() {
            super(Terminals.LPAREN);
        }
    }

    /**
     * Class for Right Parenthesis token.
     */
    public static class RParen extends Symbol {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = 912277612849109158L;

        /**
         * Creates a new Right Parenthesis token.
         */
        public RParen() {
            super(Terminals.RPAREN);
        }
    }
    
    /**
     * Class for Dot token
     */
    public static class DotOpr extends Symbol {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7501617552991053685L;
    	
		/**
		 * Creates now Dot token
		 */
		public DotOpr() {
			super(Terminals.DOTOPR);
		}
    }
}
