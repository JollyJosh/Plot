package edu.ua.cs.acm.Plot;

/**
 * Created by jzarobsky on 12/1/16.
 */
public class Recognizer {
    private Lexeme pending = null;
    private Lexer lexer = null;
    private boolean supressLegal = false;

    /**
     * A driver for the recognizer.
     * @param args the arguments passed into the driver.
     */
    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++) {
            new Recognizer(args[0]).parse();
        }
    }

    /**
     * Creates a new Recognizer with print not suppresed.
     * @param fileName The path to the file to recognize.
     */
    public Recognizer(String fileName) { lexer = new Lexer(fileName); }

    /**
     * Creates a new recognizer.
     * @param fileName The path to the file to recognize.
     * @param supress Supresses "legal" when program is valid.
     */
    public Recognizer(String fileName, boolean supress) {
        this(fileName);
        supressLegal = supress;
    }

    /**
     * Parses a given file and returns the parse tree associated with it.
     * @return a program lexeme!
     */
    public Lexeme parse() {
        pending = lexer.lex();
        Lexeme r = program();
        if(!supressLegal) System.out.println("legal");
        return r;
    }

    /**
     * Matches the pending lexeme with the type.
     * @param type the type to match.
     * @return the lexeme that was matched.
     */
    private Lexeme match(Types type) {
        if(check(type)) {
            Lexeme c = pending;
            advance();
            return c;
        }


        System.err.println("illegal");

        throw new RuntimeException(
                String.format(
                        "Expected: %s,Actual: %s,Line: %d, Value:%s\n",
                        type.toString(),
                        pending.getType().toString(),
                        lexer.getLineNumber(),
                        pending.getValue()
                )
        );
    }

    /**
     * @param type the type to check.
     * @return if the pending lexeme is of a given type.
     */
    private boolean check(Types type) { return pending.getType() == type; }

    /**
     * Advances the lexer.
     */
    private void advance() {
        pending = lexer.lex();
    }

    /*
        program : optExprList EOF
    */
    private Lexeme program() {
        Lexeme r = optExprList();
        match(Types.EOF);
        return r;
    }

    /*
        optExprList      : *none*
                         | exprList

    */
    private Lexeme optExprList() {
        if(exprListPending()) {
            return exprList();
        }
        return null;
    }

    private boolean exprListPending() { return exprPending(); }

    /*              GLUE
     *              /   \
     *          expr    GLUE
     *                  /   \
     *                expr   ...
     */

    private Lexeme exprList() {
        Lexeme expr = null, nextExpr = null;

        expr = exprPending() ? expr() : null;

        if(exprListPending()) {
            nextExpr = exprList();
        }

        return new Lexeme(Types.GLUE, expr, nextExpr);
    }

    private boolean exprPending() {
        return check(Types.ID) || check(Types.STRING) ||
                check(Types.INTEGER) || check(Types.TRUE) ||
                check(Types.FALSE) || check(Types.NIL) ||
                check(Types.OPAREN);
    }
    /*
     *      ID | STRING | INTEGER | TRUE | FALSE |
     *
     *      FUNCTIONCALL
     *      /   \
     *    expr  GLUE
     *         /
     *        expr
     */
    private Lexeme expr() {
        Lexeme result = null;

        if(check(Types.OPAREN)) {
            match(Types.OPAREN);
            result = exprList();
            match(Types.CPAREN);
            result.setType(Types.FUNCTION_CALL);
            return result;
        }

        return match(pending.getType());
    }
}
