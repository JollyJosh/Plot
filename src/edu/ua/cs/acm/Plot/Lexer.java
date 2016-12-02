package edu.ua.cs.acm.Plot;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.PushbackReader;
import java.nio.charset.Charset;
import java.io.IOException;
import java.lang.StringBuilder;

/**
 * Created by jzarobsky on 12/1/16.
 */
public class Lexer {
    /**
     * Driver for the Lexer.
     * @param args the arguments for the driver.
     */
    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++) {
            Lexer lex = new Lexer(args[i]);
            lexFile(lex);
        }
    }

    private static void lexFile(Lexer lexer) {
        for(Lexeme lexeme = lexer.lex();
            !lexeme.getType().equals(Types.EOF); lexeme = lexer.lex()) {
            System.out.println(lexeme.toString());
        }
    }

    private static void fatal(String error) {
        System.err.println(error);
        System.exit(1);
    }

    private InputStream inputStream;
    private Reader  reader;
    private PushbackReader pbReader;
    private int lineNumber = 1;

    /**
     * Constructor to initalize a Lexer for the given file.
     * @param fileName the path to the file to lex.
     */
    public Lexer(String fileName) {
        try {

            inputStream = new FileInputStream(fileName);

            reader = new InputStreamReader(
                    inputStream,
                    Charset.forName("UTF-8"));

            pbReader = new PushbackReader(reader);

        } catch (Exception ex) {
            fatal(ex.getMessage());
        }
    }

    /**
     *
     * @return Line number the lexer is currently on.
     */
    public int getLineNumber() { return lineNumber; }


    /**
     *
     * @return The next Lexeme.
     */
    public Lexeme lex() {

        skipWhiteSpace();
        char ch = readChar();
        char temp;
        if(ch == (char) 0) return new Lexeme(Types.EOF);

        boolean alpha;

        if((alpha = isAlphaOrOp(ch)) || isNumber(ch)) {
            if(alpha) return lexWord(ch);
            return lexNumber(ch, 1);
        } else {
            switch(ch) {
                case '(':
                    return new Lexeme(Types.OPAREN);
                case ')':
                    return new Lexeme(Types.CPAREN);
                case '\"':
                    return lexString();
                default:
                    System.out.println(ch);
                    fatal("bad character");
                    break;
            }
        }

        return null;
    }

    private void skipWhiteSpace() {
        boolean isComment = false;
        for(char c = readChar(); c != (char) 0 ; c = readChar())
        {
            if(c == ';') { isComment = true; }

            if(isComment) { if(c == '\r' || c =='\n') isComment = false; }

            if(c =='\n') {
                lineNumber++;
            }

            if(c != ' ' && c != '\t' && c != '\n' && c !='\r' && !isComment)
            {
                pushChar(c);
                break;
            }
        }
    }

    private boolean isAlphaOrOp(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '#'
            || c == '+' || c == '-' || c == '*' || c == '/');
    }

    private boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private char readChar() {
        int r = -1;

        try {
            r = pbReader.read();
        } catch (IOException ex) {
            fatal(ex.getMessage());
        }

        return r > 0 ? (char)r : (char) 0;
    }

    private void pushChar(char c) {
        try {
            pbReader.unread((int) c);
        } catch (IOException ex) {
            fatal(ex.getMessage());
        }
    }

    private char specialChar(char input) {
        switch(input) {
            case 'n':
                return '\n';
            case 't':
                return '\t';
            case '\\':
                return '\\';
            case '\"':
                return '\"';
            default:
                return '\0';
        }
    }

    private Lexeme lexString() {
        StringBuilder builder = new StringBuilder();
        char ch;

        while((ch = readChar()) != '\"') {
            if(ch == '\\') {
                builder.append(specialChar(readChar()));
            } else {
                builder.append(ch);
            }
        }

        return new Lexeme(Types.STRING, builder.toString());
    }

    private Lexeme lexNumber(char c, int multiplier) {
        StringBuilder builder = new StringBuilder();
        // append the first character to the lexeme.
        builder.append(c);

        while(isNumber(c = readChar())) builder.append(c);
        // push the last one that was read.
        pushChar(c);

        return new Lexeme(Types.INTEGER, multiplier * new Integer(builder.toString()));
    }

    private Lexeme lexWord(char c) {
        StringBuilder builder = new StringBuilder();

        for( ; isAlphaOrOp(c) || isNumber(c) || c == '_' ; c = readChar()) {
            builder.append(c);
        }

        pushChar(c);

        String word = builder.toString();

        if(word.equals(Types.TRUE.toString())) return new Lexeme(Types.TRUE, "#t");
        if(word.equals(Types.FALSE.toString())) return new Lexeme(Types.FALSE, "#f");
        if(word.equals(Types.NIL.toString())) return new Lexeme(Types.NIL, "nil");
        return new Lexeme(Types.ID, builder.toString());
    }
}
