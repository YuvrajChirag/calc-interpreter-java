package parser;// This class belongs to parser package

import environment.Environment;// Import Environment (required for evaluate method)

// StringNode represents a constant string value in the expression tree
public class StringNode implements Expression {
    // Stores the string value (e.g., "hello", "abc", etc.)
    private final String val;

    // Constructor: initializes the string value
    public StringNode(String val) {
        this.val = val;
    }

    // Evaluate method: returns the stored string
    // No need to use environment because it's a constant value
    @Override
    public Object evaluate(Environment env) {
        return val;// simply return the string
    }
}
