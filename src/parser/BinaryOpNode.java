package parser;// This class belongs to parser package

import environment.Environment;// Import Environment to access variable values

// BinaryOpNode represents a binary operation (like +, -, *, /, >, <, ==)
public class BinaryOpNode implements Expression {
    // Left side of the expression (e.g., x in x + y)
    private final Expression left;
    // Operator (e.g., "+", "-", "*", etc.)
    private final String operator;
    // Right side of the expression (e.g., y in x + y)
    private final Expression right;


    // Constructor: initializes left expression, operator, and right expression
    public BinaryOpNode(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    // Evaluate method: computes result based on operator
    @Override
    public Object evaluate(Environment env) {
        // Step 1: Evaluate left and right expressions recursively
        Object leftValue = left.evaluate(env);
        Object rightValue = right.evaluate(env);
        // Step 2: Perform operation based on operator
        switch (operator) {
            case "+":
                // If either value is String → perform concatenation
                if (leftValue instanceof String || rightValue instanceof String) {
                    return String.valueOf(leftValue) + rightValue;
                }
                // Otherwise perform numeric addition
                return toDouble(leftValue) + toDouble(rightValue);

            case "-":
                // Subtraction
                return toDouble(leftValue) - toDouble(rightValue);

            case "*":
                // Multiplication
                return toDouble(leftValue) * toDouble(rightValue);

            case "/":
                // Division
                return toDouble(leftValue) / toDouble(rightValue);

            case ">":
                // Greater than comparison (returns boolean)
                return toDouble(leftValue) > toDouble(rightValue);

            case "<":
                // Less than comparison (returns boolean)
                return toDouble(leftValue) < toDouble(rightValue);

            case "==":
                // Equality check
                // If both are numbers → compare numerically
                if (leftValue instanceof Number && rightValue instanceof Number) {
                    return Double.compare(toDouble(leftValue), toDouble(rightValue)) == 0;
                }
                // Otherwise use object equality
                return leftValue == null ? rightValue == null : leftValue.equals(rightValue);

            default:
                // If operator is unknown → throw error
                throw new RuntimeException("Unknown operator: " + operator);
                
        }
    }

    // Helper method: converts Object to double (only if it's a Number)
    private double toDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
         // Throw error if value is not numeric
        throw new RuntimeException("Expected numeric value but got: " + value);
    }
}

