package parser;

import environment.Environment;

public class StringNode implements Expression {
    private final String val;

    public StringNode(String val) {
        this.val = val;
    }

    @Override
    public Object evaluate(Environment env) {
        return val;
    }
}
