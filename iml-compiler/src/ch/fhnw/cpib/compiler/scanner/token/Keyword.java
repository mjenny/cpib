package ch.fhnw.cpib.compiler.scanner.token;

import ch.fhnw.cpib.compiler.scanner.enums.Terminals;

public abstract class Keyword extends AbstractToken {

	/**
	 * Serial id which is used for deep copy
	 */
	private static final long serialVersionUID = -6810226925973049239L;
	
	/**
	 * Constructor for a new keyword
	 * @param terminal
	 */
	public Keyword(Terminals terminal) {
		super(terminal);
	}
	
	@Override
	public String toString(){
		return getTerminal().toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o.getClass() == this.getClass()) {
			return super.equals((AbstractToken) o);
		}
		return true;
	}
	/**
	 *  Class for Program token.
	 */
	public static class Program extends Keyword {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5815372530572339268L;
		/**
		 * Creates a new Program token.
		 */
		public Program(){
			super(Terminals.PROGRAM);
		}
	}
	/**
	 * Class for Call token
	 */
	public static class Call extends Keyword {

		/**
		 * Serial id which is used for deep copy
		 */
		private static final long serialVersionUID = -8188251253347398870L;

		/**
		 * Constructor for a new Call Keyword token
		 * @param terminal
		 */
		public Call() {
			super(Terminals.CALL);
		}
		
	}
	/**
	 * Class for If token
	 */
	public static class If extends Keyword {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6214316121358451884L;
		/**
		 * Creates a new If token.
		 */
		public If(){
			super(Terminals.IF);
		}
	}
	/**
	 * Class for Then token
	 */
	public static class Then extends Keyword {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4604795975828427891L;
		
		/**
		 * Creates a new Then token.
		 */
		public Then(){
			super(Terminals.THEN);
		}
		
	}
	/**
	 * Class for Then token
	 */
	public static class EndIf extends Keyword {

		/**
		 * 
		 */
		private static final long serialVersionUID = -116749683864267224L;
		
		/**
		 * Creates a new Then token.
		 */
		public EndIf(){
			super(Terminals.ENDIF);
		}
		
	}
	/**
	 * Class for Else token.
	 */
	public static class Else extends Keyword {
        /**
		 * 
		 */
		private static final long serialVersionUID = 704682170635172024L;      

        /**
         * Creates a new Else token.
         */
        public Else() {
            super(Terminals.ELSE);
        }
    }
	/**
	 * Class for Fun token.
	 */
	public static class Fun extends Keyword {
       

        /**
		 * 
		 */
		private static final long serialVersionUID = 1353579237521853193L;

		/**
         * Creates a new Fun token.
         */
        public Fun() {
            super(Terminals.FUN);
        }
    }
	/**
	 * Class for Proc token.
	 */
    public static class Proc extends Keyword {
        

        /**
		 * 
		 */
		private static final long serialVersionUID = 3149130879402650807L;

		/**
         * Creates a new Proc token.
         */
        public Proc() {
            super(Terminals.PROC);
        }
    }
    /**
     * Class for Global token.
     */
    public static class Global extends Keyword {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -2015093342905421953L;

        /**
         * Creates a new Global token.
         */
        public Global() {
            super(Terminals.GLOBAL);
        }
    }

    /**
     * Class for Local token.
     */
    public static class Local extends Keyword {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -1398811311850458778L;

        /**
         * Creates a new Local token.
         */
        public Local() {
            super(Terminals.LOCAL);
        }
    }

    /**
     * Class for Not token.
     */
    public static class Not extends Keyword {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -3210418048940606523L;

        /**
         * Creates a new Not token.
         */
        public Not() {
            super(Terminals.NOT);
        }
    }

    /**
     * Class for Init token.
     */
    public static class Init extends Keyword {
        /**
         * Serial id for serialization (used for deep copy).
         */
        private static final long serialVersionUID = -2426734801243585840L;

        /**
         * Creates a new Init token.
         */
        public Init() {
            super(Terminals.INIT);
        }
    }
    
    /**
     * Class for Record token.
     */
    public static class Record extends Keyword {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7485378943587132076L;
		
		/**
		 * Creates a new Record token.
		 */
		public Record(){
			super(Terminals.RECORD);
		}
    	
    }

    /**
     * Class for Returns token.
     */
    public static class Returns extends Keyword {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 2611768262112582381L;

		/**
         * Creates a new Returns token.
         */
        public Returns() {
            super(Terminals.RETURNS);
        }
    }

    /**
     * Class for Skip token.
     */
    public static class Skip extends Keyword {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 7738237754751655795L;

		/**
         * Creates a new Skip token.
         */
        public Skip() {
            super(Terminals.SKIP);
        }
    }

    /**
     * Class for While token.
     */
    public static class While extends Keyword {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 5011665742296078493L;

		/**
         * Creates a new While token.
         */
        public While() {
            super(Terminals.WHILE);
        }
    }

    /**
     * Class for Do token.
     */
    public static class Do extends Keyword {
       
        /**
		 * 
		 */
		private static final long serialVersionUID = -5570772880427905083L;

		/**
         * Creates a new Do token.
         */
        public Do() {
            super(Terminals.DO);
        }
    }

    /**
     * Class for EndWhile token.
     */
    public static class EndWhile extends Keyword {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = 2404358663544053755L;

		/**
         * Creates a new EndWhile token.
         */
        public EndWhile() {
            super(Terminals.ENDWHILE);
        }
    }
    
    /**
     * Class for EndProgram token
     */
    public static class EndProgram extends Keyword{

		/**
		 * 
		 */
		private static final long serialVersionUID = -5363478223512079229L;
		
		/**
		 * Creates a new EndProgram token
		 */
		public EndProgram(){
			super(Terminals.ENDPROGRAM);
		}
    	
    }

    /**
     * Class for Sentinel token.
     */
    public static class Sentinel extends Keyword {
        
        /**
		 * 
		 */
		private static final long serialVersionUID = -8411302878199657186L;

		/**
         * Creates a new Sentinel token.
         */
        public Sentinel() {
            super(Terminals.SENTINEL);
        }
    }
    /**
     * Class for DebugIn token
     */
    public static class DebugIn extends Keyword{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2098934568743028935L;
		
		/**
		 * Creates a new DebugIn token.
		 */
		public DebugIn(){
			super(Terminals.DEBUGIN);
		}
    	
    }
    /**
     * Class for DebugOut token
     */
    public static class DebugOut extends Keyword{
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -2616268073367983206L;

		/**
		 * Creates a new DebugOut token.
		 */
		public DebugOut(){
			super(Terminals.DEBUGOUT);
		}
    	
    }
    /**
     * Class for EndFun token
     */
    public static class EndFun extends Keyword{
    	
		/**
		 * 
		 */
		private static final long serialVersionUID = 6488361885775536109L;

		/**
		 * Creates a new EndFun token.
		 */
		public EndFun(){
			super(Terminals.ENDFUN);
		}
    	
    }
    /**
     * Class for EndProc token
     */
    public static class EndProc extends Keyword{
    	
		/**
		 * 
		 */
		private static final long serialVersionUID = 3570349512689805990L;

		/**
		 * Creates a new EndFun token.
		 */
		public EndProc(){
			super(Terminals.ENDPROC);
		}
    	
    }
	
}
