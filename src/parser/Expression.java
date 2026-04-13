package parser;   // This interface belongs to 'parser' package

import environment.Environment;     // Import Environment class to access variables

// Expression interface represents any expression (number, variable, operation, etc.)
public interface Expression{

    //This method evaluates the expression using given environment (variables)
    // It returns the result as an Object (can be number, string, boolean, etc.)    
    Object evaluate(Environment env);
}
