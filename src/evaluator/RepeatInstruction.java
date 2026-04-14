package evaluator;

import environment.Environment;
import parser.Expression;

import java.util.List;

// Instruction representing a repeat loop
public class RepeatInstruction implements Instruction {

    // Expression defining number of repetitions
    private final Expression countExpression;

    // Instructions to execute in each iteration
    private final List<Instruction> body;

    // Initializes repeat instruction with count and body
    public RepeatInstruction(Expression countExpression, List<Instruction> body) {
        this.countExpression = countExpression;
        this.body = body;
    }

    // Executes body repeatedly based on evaluated count
    @Override
    public void execute(Environment env) {
        Object countValue = countExpression.evaluate(env);

        // Ensure repeat count is numeric
        if (!(countValue instanceof Number)) {
            throw new RuntimeException("Repeat count must be numeric but got: " + countValue);
        }

        int count = (int) Math.floor(((Number) countValue).doubleValue());

        for (int i = 0; i < count; i++) {
            for (Instruction instruction : body) {
                instruction.execute(env);
            }
        }
    }
}