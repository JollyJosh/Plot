package edu.ua.cs.acm.Plot;

/**
 * Created by jzarobsky on 12/1/16.
 */
import java.util.Arrays;

public class Evaluator {

    public static void main(String[] args) {
        try {
            new Evaluator(args[0]).evaluate();
        } catch (Exception ex) {
            System.err.print(String.format("Error in %s: %s\n", args[0], ex.getMessage()));
        }
    }

    private Recognizer recognizer;

    public Evaluator(String fileName) {
        recognizer = new Recognizer(fileName, true);
    }

    public void evaluate() {
        // Program parse tree
        Lexeme parseTree = recognizer.parse();

        // Create a Global Environment
        Lexeme globalEnv = Environment.createEnvironment();

        configBuiltIns(globalEnv);

        // Evaluate the program
        eval(parseTree, globalEnv);
    }

    private void configBuiltIns(Lexeme environment) {
        Lexeme define = new Lexeme(Types.BUILT_IN, "<built in define>");
        Lexeme print = new Lexeme(Types.BUILT_IN, "<built in print>");
        Lexeme plus = new Lexeme(Types.BUILT_IN, "<built in plus>");
        Lexeme lambda = new Lexeme(Types.BUILT_IN, "<built in lambda>");
        Lexeme ppEnv = new Lexeme(Types.BUILT_IN, "<built in ppEnv>");

        define.setBuiltIn((args, e) -> {
            if(args.getLeft().getType() == Types.ID) {
                // Variable Def
                Lexeme evaluated = eval(args.getRight().getLeft(), e);
                return Environment.insert(args.getLeft(), evaluated, e);
            } else if(args.getLeft().getType() == Types.FUNCTION_CALL) {
                // Function Def
                Lexeme closure = new Lexeme(Types.CLOSURE, "<user defined function>");
                closure.setRight(new Lexeme(Types.GLUE));

                closure.getRight().setRight(args.getRight());
                closure.getRight().setLeft(args.getLeft().getRight());
                Lexeme name = args.getLeft().getLeft();
                closure.setLeft(e);
                return Environment.insert(name, closure, e);
            } else {
                throw new RuntimeException("Illegal variable definition.");
            }
        });

        print.setBuiltIn((args, e) -> {
            Lexeme result = null;
            for(Lexeme curr = args; curr != null; curr = curr.getRight()) {
                result = eval(curr.getLeft(), e);
                System.out.print(result.getValue());
            }
            return result;
        });

        plus.setBuiltIn((args, e) -> {
            int result = 0;
            for(Lexeme curr = args; curr != null; curr = curr.getRight()) {
                result += (Integer)eval(curr.getLeft(), e).getValue();
            }

            return new Lexeme(Types.INTEGER, result);
        });

        lambda.setBuiltIn((args, e) -> {
            Lexeme closure = new Lexeme(Types.CLOSURE);
            closure.setRight(new Lexeme(Types.GLUE));

            closure.getRight().setRight(args.getRight());
            closure.getRight().setLeft(args.getLeft());
            closure.setLeft(e);
            return closure;
        });

        ppEnv.setBuiltIn((args, e) -> {
            Environment.printEnvironment(e);
            return e;
        });

        Environment.insert(new Lexeme(Types.ID, "define"), define, environment);
        Environment.insert(new Lexeme(Types.ID, "print"), print, environment);
        Environment.insert(new Lexeme(Types.ID, "+"), plus, environment);
        Environment.insert(new Lexeme(Types.ID, "lambda"), lambda, environment);
        Environment.insert(new Lexeme(Types.ID, "ppEnv"), ppEnv, environment);
    }

    private Lexeme eval(Lexeme parseTree, Lexeme environment) {
        if(parseTree == null) return null;

        switch(parseTree.getType()) {
            case INTEGER:
            case TRUE:
            case FALSE:
            case STRING:
            case NIL:
            case CLOSURE:
                return parseTree;
            case ID: return Environment.lookup(parseTree, environment);
            case FUNCTION_CALL:
                return evalFunctionCall(parseTree, environment);
            case GLUE:
                return evalGlue(parseTree, environment);
        }

        return null;
    }

    private Lexeme evalGlue(Lexeme pt, Lexeme env) {
        Lexeme lastEval = null;

        for(Lexeme curr = pt; curr != null; curr = curr.getRight())
            lastEval = eval(curr.getLeft(), env);

        return lastEval;
    }

    private Lexeme evalFunctionCall(Lexeme pt, Lexeme environment) {
        Lexeme closure = eval(pt.getLeft(), environment);
        Lexeme args = pt.getRight();

        if(closure.getType() != Types.BUILT_IN && closure.getType() != Types.CLOSURE)
            throw new RuntimeException("Attempted to call non-function");

        if(closure.getType() == Types.BUILT_IN) {
            return closure.getBuiltIn().apply(args, environment);
        }

        Lexeme eargs = evalArgList(pt.getRight(), environment);
        Lexeme defEnv = closure.getLeft();
        Lexeme params = closure.getRight().getLeft();
        Lexeme body = closure.getRight().getRight();

        Lexeme env = Environment.extendEnvironment(params, eargs, defEnv);

        return eval(body, env);
    }

    private Lexeme evalArgList(Lexeme pt, Lexeme env) {
        if(pt == null) return null;

        Lexeme evaluated = new Lexeme(Types.GLUE);
        evaluated.setLeft(eval(pt.getLeft(), env));
        evaluated.setRight(evalArgList(pt.getRight(), env));

        return evaluated;
    }
}