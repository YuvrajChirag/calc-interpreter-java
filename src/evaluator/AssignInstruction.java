package evaluator;

import environment.Environment;
import parser.Expression;

// Instruction for assigning evaluated value to a variable
public class AssignInstruction implements Instruction {

    // Name of the variable to assign
    private final String variableName;

    // Expression whose result will be stored
    private final Expression expression;

    // Initializes assignment instruction
    public AssignInstruction(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    // Evaluates expression and stores result in environment
    @Override
    public void execute(Environment env) {
        env.set(variableName, expression.evaluate(env));
    }
}