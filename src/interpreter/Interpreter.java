package interpreter;

import environment.Environment;
import evaluator.Instruction;
import parser.Parser;
import scanner.Token;
import scanner.Tokenizer;

import java.util.List;

// Runs the full pipeline: tokenize → parse → execute
public class Interpreter {

    // Executes given source code
    public void run(String sourceCode) {

        // Convert source code into tokens
        Tokenizer tokenizer = new Tokenizer(sourceCode);
        List<Token> tokens = tokenizer.tokenize();

        // Parse tokens into executable instructions
        Parser parser = new Parser(tokens);
        List<Instruction> instructions = parser.parse();

        // Execute instructions in environment
        Environment environment = new Environment();
        for (Instruction instruction : instructions) {
            instruction.execute(environment);
        }
    }
}