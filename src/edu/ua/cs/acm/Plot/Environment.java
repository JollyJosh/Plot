package edu.ua.cs.acm.Plot;

/**
 * Created by jzarobsky on 12/1/16.
 */

import java.util.Map;

public class Environment {

    private static int environmentNumbers = 0;
    /**
     * A simple driver to test the implementation of environments.
     * @param args the arugments to test the environment. (none required).
     */
    public static void main(String[] args) {
        System.out.println("Creating environment");
        Lexeme e = createEnvironment();
        printEnvironment(e);

        insert(new Lexeme(Types.ID, "x"), new Lexeme(Types.STRING, "test"), e);

        printEnvironment(e);

        Lexeme ex = extendEnvironment(
                new Lexeme(Types.GLUE, new Lexeme(Types.ID, "y"), null),
                new Lexeme(Types.GLUE, new Lexeme(Types.INTEGER, 4), null),
                e);

        printEnvironment(ex);

        System.out.println("Lookup in current environment: ");
        System.out.printf(
                "%10s : %-10s in environment: %s\n",
                "y",
                lookup(new Lexeme(Types.ID, "y"), ex).getValue(),
                ex.getValue());

        System.out.println("lookup in extended environment: ");
        System.out.printf("%10s : %-10s in environment: %s\n",
                "x",
                lookup(new Lexeme(Types.ID, "x"), ex).getValue(),
                ex.getValue());

        System.out.println("Update in extended environment: ");
        System.out.printf(
                "%10s : %-10s in environment: %s\n",
                "x",
                update(new Lexeme(Types.ID, "x"), new Lexeme(Types.INTEGER, 5), ex).getValue(),
                ex.getValue());
    }


    /***
     * Creates a new environment by creating an existing environment.
     * @param paramList The list of parameters to be initalized.
     * @param evaluatedArgs What to initialize the the parameters to be.
     * @param parent The environment to extend.
     * @return A brand new environment with the parent that was passed in.
     */
    public static Lexeme extendEnvironment(Lexeme paramList, Lexeme evaluatedArgs, Lexeme parent) {
        Lexeme environment = new Lexeme(Types.ENVIRONMENT, parent, null);

        populateEnvironment(paramList, evaluatedArgs, environment);

        environment.setValue("<environment " + (++ environmentNumbers) + ">");

        insert(new Lexeme(Types.ID, "this"), environment, environment);

        return environment;
    }

    private static void populateEnvironment(Lexeme params, Lexeme eargs, Lexeme env) {

        if(params == null ^ eargs == null) {
            throw new RuntimeException(
                    "Error number of arguments do not match parameters of function");
        }

        if(params != null) {
            env.getVariables().put(params.getLeft(), eargs.getLeft());
            populateEnvironment(params.getRight(), eargs.getRight(), env);
        }
    }

    /**
     * @return a brand new environment.
     */
    public static Lexeme createEnvironment() {
        return extendEnvironment(null, null, null);
    }

    /**
     *
     * @param environment environment to get parent of.
     * @return the parent of the environment passed in.
     */
    public static Lexeme getParent(Lexeme environment) {
        return environment.getLeft();
    }


    /**
     *
     * @param id the id to lookup
     * @param environment the environment to look up in.
     * @return The Lexeme that contains the value of the id passed in. Exits the program otherwise.
     */
    public static Lexeme lookup(Lexeme id, Lexeme environment) {

        for(; environment != null; environment = getParent(environment) ) {
            if(environment.getVariables().containsKey(id)) {
                return environment.getVariables().get(id);
            }
        }

        throw new RuntimeException(
                String.format(
                        "Undefined variable: %s",  id.getValue()));
    }

    /**
     *
     * @param id the id to insert
     * @param value the value to insert.
     * @param environment the environment to put them in.
     * @return the value that was passed in.
     */
    public static Lexeme insert(Lexeme id, Lexeme value, Lexeme environment) {
        if(id.getType() != Types.ID)
            throw new RuntimeException("Unable to insert non-id into environment");

        environment.getVariables().put(id, value);
        return value;
    }

    /**
     *
     * @param id the id to update.
     * @param val the value to change it to.
     * @param environment the environment to march up.
     * @return the value that was passed in.
     */
    public static Lexeme update(Lexeme id, Lexeme val, Lexeme environment) {

        for (; environment != null; environment = getParent(environment)) {
            if(environment.getVariables().containsKey(id)) {
                environment.getVariables().put(id, val);
                return val;
            }
        }

        throw new RuntimeException(
                String.format(
                        "Undefined variable: %s",
                        id.getValue()));
    }

    /**
     * @param environment the environment to print.
     */
    public static void printEnvironment(Lexeme environment) {
        System.out.printf(
                "%10s : %-10s\n", "env",
                environment.hashCode());

        Lexeme parent = getParent(environment);

        if(parent != null) {
            System.out.printf(
                    "%10s : %-10s\n", "parentenv",
                    getParent(environment).hashCode());
        }

        for(Map.Entry<Lexeme, Lexeme> entry :
                environment.getVariables().entrySet()) {

            if (entry.getValue() != null) {
                System.out.printf(
                        "%10s : %-10s\n",
                        entry.getKey().getValue(),
                        entry.getValue().getValue());
            }
        }
    }
}
