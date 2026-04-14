package evaluator;

import environment.Environment;
import parser.Expression;

import java.util.List;

// Instruction representing conditional execution (if block)
public class IfInstruction implements Instruction {

    // Condition to evaluate before executing body
    private final Expression condition;

    // Instructions to execute if condition is true
    private final List<Instruction> body;

    // Initializes if-instruction with condition and body
    public IfInstruction(Expression condition, List<Instruction> body) {
        this.condition = condition;
        this.body = body;
    }

    // Executes body only when condition evaluates to true
    @Override
    public void execute(Environment env) {
        Object result = condition.evaluate(env);

        // Ensure condition result is boolean
        if (!(result instanceof Boolean)) {
            throw new RuntimeException("If condition must evaluate to Boolean but got: " + result);
        }

        if ((Boolean) result) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }
}