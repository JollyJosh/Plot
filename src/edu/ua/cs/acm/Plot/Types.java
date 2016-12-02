package edu.ua.cs.acm.Plot;

/**
 * Created by jzarobsky on 12/1/16.
 */
public enum Types {

    GLUE("GLUE"),
    INTEGER("INT"),
    OPAREN("("),
    CPAREN(")"),
    EOF("EOF"),
    STRING("string"),
    TRUE("#t"),
    FALSE("#f"),
    ID("id"),
    ENVIRONMENT("env"),
    CLOSURE("closure"),
    BUILT_IN("built in closure"),
    FUNCTION_CALL("functionCall"),
    NIL("nil");

    private String type;
    public String toString() { return type; }
    Types(String t) { type = t; }
}
