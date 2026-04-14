package evaluator;

import environment.Environment;

// Represents an executable instruction in the program
public interface Instruction {

    // Executes instruction using the given environment
    void execute(Environment env);
}