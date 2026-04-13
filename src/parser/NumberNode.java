package parser; // This class belongs to the parser package
import environment.Environment; // Import Environment to match evaluate method signature

// NumberNode represents a constant numeric value in the expression tree
public class NumberNode implements Expression { 
    // Stores the numeric value (e.g., 5, 10.5, etc.)
    private final double value; 

    public NumberNode(double value) { 
    this.value = value; 
    } 

    // This method returns the value of the number
    // No need to use environment because it's a constant
    @Override 
    public Object evaluate(Environment env) { 
    return value; // simply return the stored number
    }
}