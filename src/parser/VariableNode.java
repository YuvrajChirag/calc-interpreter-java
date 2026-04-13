package parser;

import environment.Environment;

// VariableNode represents a variable (like x, y, etc.) in the expression
public class VariableNode implements Expression {
    // Stores the variable name (e.g., "x", "y")
    private final String name;

    // Constructor: initializes the variable name
    public VariableNode(String name) {
        this.name = name;
    }

    // Evaluate method: fetches the value of the variable from environment
    @Override
    public Object evaluate(Environment env) {
        // Get the value associated with the variable name
        return env.get(name);
    }
}

