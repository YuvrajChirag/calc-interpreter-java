package evaluator;

import environment.Environment;
import parser.Expression;

// Instruction to print evaluated expression result
public class PrintInstruction implements Instruction {

    // Expression whose value will be printed
    private final Expression expression;

    // Initializes print instruction
    public PrintInstruction(Expression expression) {
        this.expression = expression;
    }

    // Evaluates expression and prints result
    @Override
    public void execute(Environment env) {
        Object value = expression.evaluate(env);

        // Print integer format if number has no decimal part
        if (value instanceof Number) {
            double d = ((Number) value).doubleValue();
            if (d == Math.rint(d)) {
                System.out.println((long) d);
                return;
            }
        }

        // Otherwise print normally
        System.out.println(value);
    }
}