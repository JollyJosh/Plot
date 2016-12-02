package edu.ua.cs.acm.Plot;

import java.util.function.BiFunction;
import java.util.HashMap;

public class Lexeme implements Comparable {

    private Types type;
    private Object value;

    private Lexeme left;
    private Lexeme right;

    private HashMap<Lexeme, Lexeme> variables;
    private BiFunction<Lexeme, Lexeme, Lexeme> builtIn;

    /**
     * @return A key, value mapping of the variables in an environment.
     */
    public HashMap<Lexeme, Lexeme> getVariables() {
        if (variables == null)
            variables = new HashMap<>();
        return variables;
    }

    public BiFunction<Lexeme, Lexeme, Lexeme> getBuiltIn() {
        return builtIn;
    }

    public void setBuiltIn(BiFunction<Lexeme, Lexeme, Lexeme> bi) {
        this.builtIn = bi;
    }

    public Lexeme(Types t) {
        type = t;
    }

    public Lexeme(Types t, int value) {
        type = t;
        this.value = value;
    }

    public Lexeme(Types t, String value) {
        type = t;
        this.value = value;
    }

    public Lexeme(Types t, Lexeme l, Lexeme r) {
        type = t;
        left = l;
        right = r;
    }

    public Types getType() {
        return type;
    }
    public void setType(Types t) { this.type = t; }

    public Object getValue() {
        return value;
    }

    public void setValue(Object object) {
        this.value = object;
    }

    public Lexeme getLeft() {
        return left;
    }

    public void setLeft(Lexeme l) {
        left = l;
    }

    public Lexeme getRight() {
        return right;
    }

    public void setRight(Lexeme r) {
        right = r;
    }

    public String toString() {
        return String.format(
                "Type: %-10s Value: %s",
                type.toString(),
                getValue());
    }

    /**
     * Implements a hashcode based on the type and the string value.
     *
     * @return The hash code of this Lexeme
     */
    public int hashCode() {
        return getType().hashCode();
    }

    /**
     * Determins if the two lexemes are equal.
     *
     * @param obj The Lexeme to check for equality
     * @return if the lexemes are equivalent.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Lexeme)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Lexeme object = (Lexeme) obj;
            return object.getType() == getType() && object.getValue().equals(getValue());
        }
    }

    /**
     * Compares one lexeme to another.
     *
     * @param o The other lexeme
     * @return -a if this lexeme is less than o, 0 if they are equal, and a if this Lexeme is greater than o
     */
    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Lexeme)) {
            throw new RuntimeException("Unable to compare Lexeme types");
        }

        Lexeme other = (Lexeme) o;

        if (getType() == Types.NIL && other.getType() == Types.NIL) {
            return 0;
        } else if (getType() == Types.NIL && other.getType() == Types.INTEGER) {
            return -1; // Null should be less than. Just so it's never equal.
        } else if (getType() == Types.INTEGER && other.getType() == Types.NIL) {
            return 1;
        } else if (getType() == Types.NIL && other.getType() == Types.STRING) {
            return -1;
        } else if (getType() == Types.STRING && other.getType() == Types.NIL) {
            return 1;
        } else if (getType() == Types.INTEGER && other.getType() == Types.INTEGER) {
            if ((Integer) getValue() == (Integer) other.getValue()) {
                return 0;
            } else if ((Integer) getValue() > (Integer) other.getValue()) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return ((String)getValue()).compareTo((String)other.getValue());
        }
    }
}