import interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Entry point of the program
public class Main {

    // Reads source file and runs interpreter
    public static void main(String[] args) throws IOException {

        // Ensure exactly one input file is provided
        if (args.length != 1) {
            System.err.println("Usage: java Main <file.calc>");
            System.exit(1);
        }

        // Read source code from file
        String sourceCode = Files.readString(Path.of(args[0]));

        // Execute program using interpreter
        Interpreter interpreter = new Interpreter();
        interpreter.run(sourceCode);
    }
}